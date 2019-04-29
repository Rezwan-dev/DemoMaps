package com.heavygari.heavygaricustomer.base.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import static android.content.Context.LOCATION_SERVICE;

public class GPSTracker implements LocationListener {

    private static final String TAG = "GPSTracker";

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    // The minimum accuracy of location (no accuracy = -1f)
    private static final float MIN_ACCURACY = -1f;

    private Context mContext;
    private LocationUpdateListener locationUpdateListener;

    // flag for GPS status
    private boolean isGPSEnabled = false;
    // flag for network status
    private boolean isNetworkEnabled = false;
    // flag for GPS status
    private boolean canGetLocation = false;

    private Location location; // location
    private double latitude; // latitude
    private double longitude; // longitude



    // Declaring a Location Manager
    private LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public GPSTracker(Context context, LocationUpdateListener locationUpdateListener){
        this.mContext = context;
        this.locationUpdateListener = locationUpdateListener;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Print.e(TAG, "getLocation - step 1 isGPSEnabled - "+isGPSEnabled+" isNetworkEnabled - "+isNetworkEnabled);

            if (isGPSEnabled || isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Print.e(TAG, "getLocation - step 2");
                    return null;
                }
                this.canGetLocation = true;
                if (isNetworkEnabled) {


                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Print.e(TAG, "Location from network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Print.e(TAG, "Location from GPS");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                Print.e(TAG, "Last Known Location "+location.toString());
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app.
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }


    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }



    @Override
    public void onLocationChanged(Location location) {
        if ((location.getAccuracy() != 0.0f
                && (location.getAccuracy() < MIN_ACCURACY) || MIN_ACCURACY == -1f) && locationUpdateListener == null) {
            locationManager.removeUpdates(this);
        }
        if(locationUpdateListener != null){
            locationUpdateListener.onLocationUpdate(location);
        }
        Print.e(TAG, "onLocationChanged "+location.toString());
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }



    public float getAccurecy()
    {
        return location.getAccuracy();
    }

    interface LocationUpdateListener{
        Location onLocationUpdate(Location location);
    }

}