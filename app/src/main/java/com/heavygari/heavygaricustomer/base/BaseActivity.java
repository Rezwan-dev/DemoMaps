package com.heavygari.heavygaricustomer.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.heavygari.heavygaricustomer.R;
import com.heavygari.heavygaricustomer.base.broadcastListeners.ReceiverConnectivity;
import com.heavygari.heavygaricustomer.base.broadcastListeners.ReceiverLocation;
import com.heavygari.heavygaricustomer.base.utils.LoadingView;
import com.heavygari.heavygaricustomer.base.utils.Print;
import com.heavygari.heavygaricustomer.network.ListenerResponse;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements ReceiverConnectivity.ConnectivityReceiverListener, ReceiverLocation.LocationReceiverListener, ListenerResponse {

    private static final int LOCATION_CODE = 1001;

    private CoordinatorLayout fullLayout;
    private LoadingView loadingView;
    private View noInternetView, noLocationPermission, noLocationSettingOn;
    protected boolean performNetworkCheck = true;
    protected boolean performLocationCheck = true;
    private boolean isBlockingView = false;

    @SuppressLint("InflateParams")
    @Override
    public void setContentView(int layoutResID) {
        fullLayout = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.activity_base, null);  // The base layout
        FrameLayout subActivityContent = fullLayout.findViewById(R.id.activityView);
        getLayoutInflater().inflate(layoutResID, subActivityContent, true);            // Places the activity layout inside the activity content frame.
        super.setContentView(fullLayout);                                                       // Sets the content view as the merged layouts.
        init();
    }


    private void init() {
        loadingView = findViewById(R.id.loadingView);
        noInternetView = findViewById(R.id.noInternetView);
        noLocationPermission = findViewById(R.id.noLocationPermission);
        noLocationSettingOn = findViewById(R.id.noLocationSettingOn);
        if(performNetworkCheck){
            BaseApplication.getInstance().setConnectivityListener(this);
        }
        if(performLocationCheck){
            BaseApplication.getInstance().setLocationListener(this);
        }
    }

    private void validation(){
        if(performNetworkCheck){
            onNetworkConnectionChanged(ReceiverConnectivity.isNetworkAvailable());
        }
        if(performLocationCheck) {
            checkLocationPermission();
        }
    }

    @Override
    protected void onResume() {
        validation();
        super.onResume();
    }

    public boolean checkLocationPermission() {
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    LOCATION_CODE);
            return false;
        }else{

            return checkLocationSettings();
        }
    }

    private boolean checkLocationSettings(){
        if(ReceiverLocation.isLocationAvailable()){
            hideLocationSettingOn();
            return true;
        }else{
            showLocationSettingsOn();
            return false;
        }
    }

    private void showLocationSettingsOn() {
        if(isBlockingView){
            return;
        }
        hideKeyboard();
        isBlockingView = true;
        noLocationSettingOn.setVisibility(View.VISIBLE);
        Print.e("showLocationSettingsOn");
    }

    private void hideLocationSettingOn() {
        if(noLocationSettingOn.getVisibility() == View.VISIBLE) {
            noLocationSettingOn.setVisibility(View.GONE);
            isBlockingView = false;
            Print.e("hideLocationSettingOn");
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_CODE:{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hideLocationPermission();
                    checkLocationSettings();
                } else {
                    showLocationPermission();
                }
                break;
            }
        }
    }



    private void showLocationPermission() {
        if(isBlockingView){
            return;
        }
        hideKeyboard();
        isBlockingView = true;
        noLocationPermission.setVisibility(View.VISIBLE);
        Print.e("showLocationPermission");
    }

    private void hideLocationPermission() {
        if(noLocationPermission.getVisibility() == View.VISIBLE){
            isBlockingView = false;
            noLocationPermission.setVisibility(View.GONE);
            Print.e("hideLocationPermission");
        }
    }

    public void showLoading(){
        if(isBlockingView){
            return;
        }
        hideKeyboard();
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startLoading();
        isBlockingView = true;
        Print.e("showLoading");
    }

    public void hideLoading(){
        if(loadingView.getVisibility() == View.VISIBLE) {
            loadingView.stopLoading();
            loadingView.setVisibility(View.GONE);
            isBlockingView = false;
            Print.e("hideLoading");
        }

    }

    public void showError(String msg){
        hideKeyboard();
        Snackbar snackbar;
        snackbar = Snackbar.make(fullLayout, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.red));
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();
    }

    public void showInfo(String msg){
        hideKeyboard();
        Snackbar snackbar;
        snackbar = Snackbar.make(fullLayout, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.green));
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected) {
            hideLoading();
        }
        if(!isConnected && !isBlockingView && noInternetView.getVisibility() == View.GONE){
            isBlockingView = true;
            hideKeyboard();
            noInternetView.setVisibility(View.VISIBLE);
        }else if(isConnected && isBlockingView && noInternetView.getVisibility() == View.VISIBLE){
            isBlockingView = false;
            noInternetView.setVisibility(View.GONE);
            if(performLocationCheck) {
                checkLocationPermission();
            }
        }

    }



    public void openNetworkSettings(View v){
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    public void openLocationSettings(View v){
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void openAppSettings(View v){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onLocationConnectionChanged(boolean isConnected) {
        if(!isConnected) {
            hideLoading();
        }
        if(!isConnected && !isBlockingView && noLocationSettingOn.getVisibility() == View.GONE){
            hideKeyboard();
            showLocationSettingsOn();
        }else if(isConnected && isBlockingView && noLocationSettingOn.getVisibility() == View.VISIBLE){
            hideLocationSettingOn();
            if(performNetworkCheck) {
                onNetworkConnectionChanged(ReceiverConnectivity.isNetworkAvailable());
            }
        }

    }

    @Override
    public void onResponse(Object response) {

    }

    @Override
    public void onError(Object error) {

    }
}
