package com.heavygari.heavygaricustomer.base.broadcastListeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.heavygari.heavygaricustomer.base.BaseApplication;

public class ReceiverConnectivity
        extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ReceiverConnectivity() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = isNetworkAvailable();
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }



    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}