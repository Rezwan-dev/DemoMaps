package com.heavygari.heavygaricustomer.base.broadcastListeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.heavygari.heavygaricustomer.base.BaseApplication;
import com.heavygari.heavygaricustomer.base.utils.Print;

public class ReceiverLocation
        extends BroadcastReceiver {

    public static LocationReceiverListener locationReceiverListener;

    public ReceiverLocation() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = isLocationAvailable();
        if (locationReceiverListener != null) {
            locationReceiverListener.onLocationConnectionChanged(isConnected);
        }
    }

    public static boolean isLocationAvailable() {
        try {
            LocationManager locationManager = (LocationManager) BaseApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            return isGpsEnabled || isNetworkEnabled;
        }catch (Exception exception){
            Print.e(exception.getMessage());
        }
        return false;

    }



    public interface LocationReceiverListener {
        void onLocationConnectionChanged(boolean isConnected);
    }
}