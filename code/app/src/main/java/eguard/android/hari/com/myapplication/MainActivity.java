/*  eGuard Application to detect fall and raise alerts in case of emergencies
    Check the repo README file for more information on the application
*/

package eguard.android.hari.com.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends Activity implements ServiceConnection {

    //connect app to Bluetooth Service
    private BtleService.LocalBinder serviceBinder;

    /* Device MAC Address. As the bluetooth scan is weak and not reliable, passing the device MAC address
       to ensure simple connectivity for now
       TODO: Probably not a secure way to expose device MAC Address, change it and test for Bluetooth Scan
    */
    private final String MW_MAC_ADDRESS = "D0:58:8E:AA:76:8D";

    //MetaWear object to connect app and device
    private MetaWearBoard board;

    // Object to start and stop collecting acceleration data
    Accelerometer accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);

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
                        .odr(25f)
                        .commit();

                /*
                    Check the acceleration data if threshold value has been reached across 4 samples
                    The threshold value is set to be 0.4g
                    This means when there is acceleration equal or more than 0.4g, the
                    app will identify this as a fall scenario and raise an alert
                    Threshold value was obtained after collecting and monitoring
                    multiple scenarios and transitions in user movements
                    Check the data-source folder README file in the repo for more information on this
                    Using Binary Mode to check for Threshold output, Return value will be 1 when threshold is reached
                    and -1 when within threshold
                */

                return accelerometer.acceleration().addRouteAsync(new RouteBuilder() {
                    @Override
                    public void configure(RouteComponent source) {
                        source.stream(new Subscriber() {
                            @Override
                            public void apply(Data data, Object... env) {
                                Log.i("eGuard", data.value(Acceleration.class).toString());
                            }
                        });
                        source.map(Function1.RSS).average((byte) 4).filter(ThresholdOutput.BINARY, 3.5f)
                                .multicast()
                                .to().filter(Comparison.EQ, -1).stream(new Subscriber() {
                            @Override
                            /*
                               If the user acceleration has gone beyond the threshold, log a fall message
                               TODO: And raise an alert to send a message
                            */
                            public void apply(Data data, Object... env) {
                                Log.i("eGuard", "There has been a fall");
                                }
                            })
                                .to().filter(Comparison.EQ, 1).stream(new Subscriber() {
                            @Override
                            // If the user movement is normal, log a message
                            public void apply(Data data, Object... env) {
                                Log.i("eGuard", "Normal user movement");
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
}
