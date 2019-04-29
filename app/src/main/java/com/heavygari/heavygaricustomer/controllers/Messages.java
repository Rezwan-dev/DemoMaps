package com.heavygari.heavygaricustomer.controllers;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heavygari.heavygaricustomer.R;
import com.heavygari.heavygaricustomer.base.BottomFramgent;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Messages extends BottomFramgent {


    private View toolBarBottom, upArrow;
    private int heightPixel;
    private boolean isAttached = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        init(root);
        return root;
    }

    private void init(View root) {

        toolBarBottom = root.findViewById(R.id.toolBarBottom);
        //upArrow = root.findViewById(R.id.up_arrow);
        root.findViewById(R.id.bottomSheetClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ((ActivityHome)getActivity()).sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

    }

    @Override
    public void onSlide(float slideOffset) {
        if(!isAttached){
            return;
        }
        if(slideOffset >= 0.5) {
            float heightDP = 56 - (56 * (2 - slideOffset*2));
            float density = getResources().getDisplayMetrics().density;
            heightPixel = (int) (heightDP * density);
            ViewGroup.LayoutParams layoutParams = toolBarBottom.getLayoutParams();
            layoutParams.height = heightPixel;
            toolBarBottom.setLayoutParams(layoutParams);
        }
        if(slideOffset < 0.5 && heightPixel != 0){
            heightPixel = 0;
            ViewGroup.LayoutParams layoutParams = toolBarBottom.getLayoutParams();
            layoutParams.height = heightPixel;
            toolBarBottom.setLayoutParams(layoutParams);
        }
       /* if (slideOffset < 0.5){
            float heightDP = 24 - (24 * slideOffset*2);
            float density = getResources().getDisplayMetrics().density;
            heightPixel = (int) (heightDP * density);
            ViewGroup.LayoutParams layoutParams = upArrow.getLayoutParams();
            layoutParams.height = heightPixel;
            upArrow.setLayoutParams(layoutParams);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false;
    }
}
