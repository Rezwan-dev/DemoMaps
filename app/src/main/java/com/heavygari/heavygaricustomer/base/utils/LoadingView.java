package com.heavygari.heavygaricustomer.base.utils;


import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.heavygari.heavygaricustomer.R;

public class LoadingView extends FrameLayout {

    private ImageView bouncingIcon, shadow;
    private Handler handler;

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_loading, this, true);
        bouncingIcon = findViewById(R.id.bouncingIcon);
        shadow = findViewById(R.id.shadow);
        handler = new Handler();
    }


    public void startLoading(){


        final Animation shadow_scale_down = new ScaleAnimation(1f, 0.65f, 1f, 1f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        shadow_scale_down.setInterpolator(new DecelerateInterpolator());
        shadow_scale_down.setDuration(500);

        final Animation shadow_scale_up = new ScaleAnimation(0.65f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        shadow_scale_up.setInterpolator(new AccelerateInterpolator());
        shadow_scale_up.setDuration(500);


        Animation bouncingIcon_scale_up = new ScaleAnimation(1f, 1.10f, 1f, 1.10f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        bouncingIcon_scale_up.setInterpolator(new DecelerateInterpolator());
        bouncingIcon_scale_up.setDuration(500);

        Animation bouncingIcon_translate_up = new TranslateAnimation(0f, 0f, 0f, -100f);
        bouncingIcon_translate_up.setInterpolator(new DecelerateInterpolator());
        bouncingIcon_translate_up.setDuration(500);

        Animation bouncingIcon_rotate = new RotateAnimation(0f,720f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        bouncingIcon_rotate.setInterpolator(new DecelerateInterpolator());
        bouncingIcon_rotate.setDuration(500);

        Animation bouncingIcon_scale_down = new ScaleAnimation(1.10f, 1f, 1.10f, 1f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        bouncingIcon_scale_down.setInterpolator(new AccelerateInterpolator(1.4f));
        bouncingIcon_scale_down.setDuration(500);

        Animation bouncingIcon_translate_down = new TranslateAnimation(0f, 0f, -100f, 0f);
        bouncingIcon_translate_down.setInterpolator(new AccelerateInterpolator(1.4f));
        bouncingIcon_translate_down.setDuration(500);



        final AnimationSet bouncingIcon_animation_1 = new AnimationSet(false);
        bouncingIcon_animation_1.addAnimation(bouncingIcon_rotate);
        bouncingIcon_animation_1.addAnimation(bouncingIcon_scale_up);
        bouncingIcon_animation_1.addAnimation(bouncingIcon_translate_up);


        final AnimationSet bouncingIcon_animation_2 = new AnimationSet(false);
        bouncingIcon_animation_2.addAnimation(bouncingIcon_scale_down);
        bouncingIcon_animation_2.addAnimation(bouncingIcon_translate_down);

        shadow.startAnimation(shadow_scale_down);
        bouncingIcon.startAnimation(bouncingIcon_animation_1);



        bouncingIcon_animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bouncingIcon.startAnimation(bouncingIcon_animation_2);
                shadow.startAnimation(shadow_scale_up);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        bouncingIcon_animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bouncingIcon.startAnimation(bouncingIcon_animation_1);
                shadow.startAnimation(shadow_scale_down);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    public void stopLoading(){
        bouncingIcon.clearAnimation();
        shadow.clearAnimation();
        handler.removeCallbacksAndMessages(null);
    }



}
