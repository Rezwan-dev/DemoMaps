package com.heavygari.heavygaricustomer.controllers;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.heavygari.heavygaricustomer.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VehiclePicker extends Fragment {

    public BottomSheetBehavior<View> sheetBehavior;
    public View back_overlay;
    private View toolBarBottom;
    private TextView time_tv;
    private ImageView time_imv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vehicle_picker, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.rv);
        MyListAdapter adapter = new MyListAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        root.findViewById(R.id.gotoBookingOptions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityHome)VehiclePicker.this.getActivity()).showFullBookingOptions();
            }
        });
        root.findViewById(R.id.date_time_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        time_tv = root.findViewById(R.id.time_tv);
        time_imv = root.findViewById(R.id.time_imv);
        AnimatedVectorDrawableCompat drawableCompat  = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.arrow_to_checked_2);
        time_imv.setImageDrawable(drawableCompat);
    }

    private void pickDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 4);
        calendar.add(Calendar.MINUTE, 10);

        new SingleDateAndTimePickerDialog.Builder(getContext())
                .bottomSheet()
                //.curved()
                .minutesStep(10)
                //.displayDaysOfMonth(true)
                .displayHours(true)
                .displayMinutes(true)
                .displayAmPm(true)
                .displayDays(true)
                .displayYears(false)
                .displayMonth(false)
                .defaultDate(calendar.getTime())
                .minDateRange(calendar.getTime())
                .mainColor(getResources().getColor(R.color.black))
                //.todayText("aujourd'hui")
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        //retrieve the SingleDateAndTimePicker
                    }
                })
                .title("Pick Up Time")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                      /*  long diff = date.getTime() - Calendar.getInstance().getTime().getTime();//as given
                        long hours = TimeUnit.MILLISECONDS.toHours(diff);*/
                        SimpleDateFormat sm = new SimpleDateFormat("dd MMM yy, hh:mm a", Locale.getDefault());
                        time_tv.setText(sm.format(date));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Animatable drawableCompat =  (Animatable) time_imv.getDrawable();
                                drawableCompat.start();

                            }
                        }, 500);
                    }
                }).display();
    }

    class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

        List<Test_item> items;
        int selected_index = -1;


        MyListAdapter() {
            items = new ArrayList<>();
            for(int i = 0; i < 20; i ++){
                Test_item item = new Test_item();
                item.index = i;
                items.add(item);
            }

        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.test_item_1, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if(items.get(position).is_selected){
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(1);

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                holder.imageView.getDrawable().setColorFilter(filter);
                holder.cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.light_gray));
            }else {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                holder.imageView.getDrawable().setColorFilter(filter);
                holder.cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected_index == position){
                        return;
                    }
                    if(selected_index > -1){
                        items.get(selected_index).is_selected = false;
                        MyListAdapter.this.notifyItemChanged(selected_index);
                    }

                    final ColorMatrix matrix = new ColorMatrix();

                    ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
                    animation.setDuration(500);
                   // animation.setInterpolator(new AccelerateInterpolator());
                    animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            matrix.setSaturation(animation.getAnimatedFraction());
                            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                            holder.imageView.getDrawable().setColorFilter(filter);
                        }
                    });
                    selected_index = position;
                    items.get(position).is_selected = true;
                    holder.cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.light_gray));
                    animation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            MyListAdapter.this.notifyItemChanged(selected_index);
                        }
                    });
                    animation.start();

                }
            });

        }


        @Override
        public int getItemCount() {
            return 20;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            CardView cardView;

            public ViewHolder(View itemView) {
                super(itemView);
                 imageView = itemView.findViewById(R.id.vehicle_iv);
                 cardView = itemView.findViewById(R.id.card);
            }
        }

        class Test_item {
            boolean is_selected = false;
            int index;
        }
    }
}
