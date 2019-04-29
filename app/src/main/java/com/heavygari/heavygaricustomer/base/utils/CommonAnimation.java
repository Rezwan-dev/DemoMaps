package com.heavygari.heavygaricustomer.base.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class CommonAnimation {

    public static synchronized void slideUpHide(final View view, final mAnimationListener animationListener ){
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF, 0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, -1f);
        animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                    if(animationListener!= null) {
                        animationListener.finished();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        view.startAnimation(animation);


    }

    public static synchronized void slideDownShow(final View view, final mAnimationListener animationListener ){
        view.setVisibility(View.VISIBLE);
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF, 0,Animation.RELATIVE_TO_SELF,-1f,Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
               // view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(animationListener!= null) {
                    animationListener.finished();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);


    }

    public static synchronized void slideUpShow(View view, final mAnimationListener animationListener){
        view.setVisibility(View.VISIBLE);
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF, 0,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(animationListener!= null) {
                    animationListener.finished();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public static synchronized void slideDownHide(final View view, final mAnimationListener animationListener){
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF, 0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF, 1f);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                if(animationListener!= null) {
                    animationListener.finished();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }



    public interface mAnimationListener{
        void finished();
    }
}
