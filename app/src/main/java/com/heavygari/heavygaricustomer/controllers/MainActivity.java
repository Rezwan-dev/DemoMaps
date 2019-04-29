package com.heavygari.heavygaricustomer.controllers;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.heavygari.heavygaricustomer.R;
import com.heavygari.heavygaricustomer.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.showLoading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
            }
        });
        findViewById(R.id.hideLoading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLoading();
            }
        });
        findViewById(R.id.showInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo("this is a info that will be shown");
            }
        });
        findViewById(R.id.showError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showError("this is a error that will be shown");
            }
        });
    }



}
