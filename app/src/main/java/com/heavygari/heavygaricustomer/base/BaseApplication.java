package com.heavygari.heavygaricustomer.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.heavygari.heavygaricustomer.base.broadcastListeners.ReceiverConnectivity;
import com.heavygari.heavygaricustomer.base.broadcastListeners.ReceiverLocation;
import com.heavygari.heavygaricustomer.base.utils.Print;

public class BaseApplication extends Application {

    private static BaseApplication mInstance;
    private static String CONNECTIVITY_CHANGE = "heavygari.connectivity";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        setUpForInternetActivity();
        setUpForLocationActivity();
    }

    private void setUpForLocationActivity() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        ReceiverLocation receiverLocation = new ReceiverLocation();
        registerReceiver(receiverLocation, filter);
    }

    private void setUpForInternetActivity() {
        IntentFilter filter = new IntentFilter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createChangeConnectivityMonitor();
            filter.addAction(CONNECTIVITY_CHANGE);
            Print.e("setUpForInternetActivity", "Equal or Above N");
        } else {
            //noinspection deprecation
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        }
        ReceiverConnectivity receiverConnectivity = new ReceiverConnectivity();
        registerReceiver(receiverConnectivity, filter);
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ReceiverConnectivity.ConnectivityReceiverListener listener) {
        ReceiverConnectivity.connectivityReceiverListener = listener;
    }

    public void setLocationListener(ReceiverLocation.LocationReceiverListener listener){
        ReceiverLocation.locationReceiverListener = listener;
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private void createChangeConnectivityMonitor() {
        final Intent intent = new Intent(CONNECTIVITY_CHANGE);
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder().build(),
                    new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            sendBroadcast(intent);
                        }

                        @Override
                        public void onLost(Network network) {
                            sendBroadcast(intent);
                        }
                    });
        }
    }


}
