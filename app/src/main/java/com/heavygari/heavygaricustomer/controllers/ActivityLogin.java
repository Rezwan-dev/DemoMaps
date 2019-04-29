package com.heavygari.heavygaricustomer.controllers;


import android.os.Bundle;

import com.heavygari.heavygaricustomer.R;
import com.heavygari.heavygaricustomer.base.BaseActivity;

public class ActivityLogin extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performLocationCheck = false;
        setContentView(R.layout.activity_login);
    }
}
