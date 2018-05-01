/*  eGuard Application to detect fall and raise alerts in case of emergencies
    Check the GitHub README file for more information on the application
*/

package eguard.android.hari.com.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mbientlab.metawear.Data;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.android.BtleService;
import com.mbientlab.metawear.builder.RouteBuilder;
import com.mbientlab.metawear.builder.RouteComponent;
import com.mbientlab.metawear.builder.filter.Comparison;
import com.mbientlab.metawear.builder.filter.ThresholdOutput;
import com.mbientlab.metawear.builder.function.Function1;
import com.mbientlab.metawear.data.Acceleration;
import com.mbientlab.metawear.module.Accelerometer;

import java.util.Random;

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends Activity implements ServiceConnection {

    //connect app to Bluetooth Service
    private BtleService.LocalBinder serviceBinder;

    /* Device MAC Address. As the bluetooth scan is weak and not reliable, passing the device MAC address
       to ensure simple connectivity for now
    */
    private final String MW_MAC_ADDRESS = "E5:7A:73:82:24:CE";

    //MetaWear object to connect app and device
    private MetaWearBoard board;

    // Object to start and stop collecting acceleration data
    Accelerometer accelerometer;

    // Object to create an alert dialog when a fall is detected
    AlertDialog.Builder builder;

    // Variables to store data from 3-axes of accelerometer of MetaWear
    private LineGraphSeries<DataPoint> seriesX;
    private LineGraphSeries<DataPoint> seriesY;
    private LineGraphSeries<DataPoint> seriesZ;
    private int lastX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);

        // Create a graph object and refer it to the graph from the layout
        GraphView graph = (GraphView) findViewById(R.id.graph);
        seriesX = new LineGraphSeries<DataPoint>();
        seriesY = new LineGraphSeries<DataPoint>();
        seriesZ = new LineGraphSeries<DataPoint>();

        // Set the different color for each axes on the graph
        seriesX.setColor(Color.BLUE);
        seriesY.setColor(Color.GREEN);
        seriesZ.setColor(Color.RED);

        // Add each series to the graph
        graph.addSeries(seriesX);
        graph.addSeries(seriesY);
        graph.addSeries(seriesZ);

        // Styling for the graph
        graph.setTitle("Acceleration vs Time");
        graph.setTitleColor(Color.BLACK);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        // Set Axis titles
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.setVerticalAxisTitle("Acceleration");
        Viewport viewport = graph.getViewport();
        viewport.setScrollable(true);
        graph.onDataChanged(true, true);

        /* Event Listener for Start Button
           When clicked, accelerometer will start monitoring the user acceleration
        */
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("eGuard", "start");
                accelerometer.acceleration().start();
                accelerometer.start();
            }
        });

        /* Event Listener for Stop Button
           When clicked, accelerometer will stop monitoring the user acceleration */
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("eGuard", "stop");
                accelerometer.stop();
                accelerometer.acceleration().stop();
            }
        });
        // Reset the data collection from metaWear device
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.tearDown();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unbind the service when the activity is destroyed
        getApplicationContext().unbindService(this);
    }

    // Once the App is up and running, it will try and connect to the board using its MAC address
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        serviceBinder = (BtleService.LocalBinder) service;

        Log.i("eGuard", "Service Connected");

        // Connect to the MetaWear device using it's MAC address
        retrieveBoard(MW_MAC_ADDRESS);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    /*
      Data is monitored and processed as part of this function and threshold value is checked
    */

    // Function will connect to the MetaWear device
    public void retrieveBoard(final String macAddr) {
        final BluetoothManager btManager=
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothDevice remoteDevice=
                btManager.getAdapter().getRemoteDevice(macAddr);

        // Create a MetaWear board object for the Bluetooth Device
        board = serviceBinder.getMetaWearBoard(remoteDevice);

        // Make a connection to the MetaWear device using its MAC address
        board.connectAsync().onSuccessTask(new Continuation<Void, Task<Route>>() {
            @Override
            public Task<Route> then(Task<Void> task) throws Exception {

                Log.i("eGuard", "Connected to " + macAddr);

                // Configure the accelerometer object and stream data from MetaWear device
                accelerometer = board.getModule(Accelerometer.class);
                accelerometer.configure()
                        .odr(12.5f)
                        .commit();

                /*
                    Check the acceleration data if threshold value has been reached
                    The threshold value is set to be 3.5g
                    This means when there is acceleration equal or more than 3.5g, the
                    app will identify this as a fall scenario and raise an alert
                    Threshold value was obtained after collecting and monitoring
                    multiple scenarios and transitions in user movements
                    Check the data-source folder README file in the repo for more information on this
                    Using Comparator to check for Threshold output.
                    Input value is passed through and checked for its RSS (Root Sum Squared) value
                */

                return accelerometer.acceleration().addRouteAsync(new RouteBuilder() {
                    @Override
                    public void configure(RouteComponent source) {
                        source.stream(new Subscriber() {
                            @Override
                            public void apply(Data data, Object... env) {
                                double accMag = calculateAcceleration(data);
                                Log.i("eGuard", Double.toString(accMag));
                                addEntry(data);
                            }
                        });


                        source.map(Function1.RSS).filter(Comparison.GT, 2f).multicast().to().stream(new Subscriber() {
//                        source.map(Function1.RSS).filter(ThresholdOutput.BINARY, 1.5f)
//                                .multicast()
//                                .to().filter(Comparison.EQ, -1).stream(new Subscriber() {
                            @Override
                            /*
                               If the user acceleration has gone beyond the threshold, log a fall message
                               Call the function to create and send a notification
                               Also create an alert pop up on the screen to confirm sent notification
                            */
                            public void apply(Data data, Object... env) {
                                Log.i("eGuard", "There has been a fall: " + data.toString());
                                sendNotification();
                                createAlert();
                                }
                            })
                                .to().filter(Comparison.EQ, 1).stream(new Subscriber() {
                            @Override
                            // If the user movement is normal, log a message
                            public void apply(Data data, Object... env) {
                                Log.i("eGuard", "Normal user movement: " );
                                }
                            })
                        .end();
                    }
                });
            }
        }).continueWith(new Continuation<Route, Void>() {
            @Override
            // When the app has successfully connected to the MetaWear device, log a success message
            public Void then(Task<Route> task) throws Exception {
                if(task.isFaulted()) {
                    Log.w("eGuard", "Failed to configure app ", task.getError());
                } else {
                    Log.i("eGuard", "App configured");
                }
                return null;
            }
        });
    }

    /*
    * Calculate the magnitude of Acceleration at every moment and stream application home page
    */
    public double calculateAcceleration(Data data) {
        double xMag = ((double) data.value(Acceleration.class).x());
        double yMag = ((double) data.value(Acceleration.class).y());
        double zMag = ((double) data.value(Acceleration.class).z());
        double magnitude = Math.sqrt((Math.pow(xMag,2)) + Math.pow(yMag, 2) + Math.pow(zMag, 2));
        Log.i("eGuard", "Magnitude is: " + magnitude);
        return magnitude;

    }

    /*
    * When a fall scenario occurs, i.e threshold has been reached, this function will be called
    * This function will create a default notification and notify the user
    * An improved application of this function would be to send an SMS to an emergency contact*/
    public void sendNotification() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icons8_notification_50)
                        .setContentTitle("Alert! In Danger")
                        .setContentText("There has been a fall");

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());

    }

    /*
    * This function creates an alert pop up once a notification is sent
    */
    public void createAlert() {

        runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Alert")
                        .setMessage("Alert has been sent, Help Arriving Soon!")
                        .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }));

    }

    /*
    * This function is called when data is streamed from the accelerometer and called to add data points
    * to the graph
    * Provides added visualization on the user movements and their acceleration
    */
    public void addEntry(Data data) {
        // Function is run on a separate UI thread and data is added to each series corresponding to the axes of the accelerometer
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Add data points to each of the axes for the data streamed
                // Data is stored for a max of 300 points after which it is deleted to avoid memory loss
                seriesX.appendData(new DataPoint(lastX, ((double) data.value(Acceleration.class).x())), false, 300);
                seriesY.appendData(new DataPoint(lastX, ((double) data.value(Acceleration.class).y())), false, 300);
                seriesZ.appendData(new DataPoint(lastX, ((double) data.value(Acceleration.class).z())), false, 300);
                lastX++;
            }
        });
    }
}
