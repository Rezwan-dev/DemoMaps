package com.heavygari.heavygaricustomer.controllers;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.heavygari.heavygaricustomer.R;

public class RecipientDialog extends Dialog implements
        View.OnClickListener {

    public ActivityHome c;
    public Dialog d;
    public Button yes, no;

    public RecipientDialog(ActivityHome a) {
        super(a,R.style.ConfirmDialog);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.fragment_recipient);
        findViewById(R.id.fullBookingSubmitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipientDialog.this.dismiss();
                c.setRecipient("Rezwan", "01717230976");
               // c.findViewById(R.id.topBackBtn).performClick();
               // c.showInfo("Booking created successfully");
            }
        });
        //CommonAnimation.slideDownShow(findViewById(R.id.discount_tv), null);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
        dismiss();
    }
}