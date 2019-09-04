package com.heavygari.heavygaricustomer.controllers;


import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.heavygari.heavygaricustomer.R;
import com.heavygari.heavygaricustomer.base.BaseActivity;
import com.heavygari.heavygaricustomer.base.BottomFramgent;
import com.heavygari.heavygaricustomer.base.mapPathing.RoutingPath;
import com.heavygari.heavygaricustomer.base.utils.CommonAnimation;
import com.heavygari.heavygaricustomer.base.utils.GPSTracker;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ActivityHome extends BaseActivity implements OnMapReadyCallback, FragmentDrawer.OnDrawerInteractionListener {

    private GoogleMap mMap;
    private RoutingPath routingPath;
    private DrawerLayout drawerLayout;
    private View back_overlay;
    protected BottomSheetBehavior<View> sheetBehavior;

    private BottomFramgent currentBottomFramment;
    private boolean willShowOverlay = true;
    private CoordinatorLayout bottom_sheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performNetworkCheck = false;
        performLocationCheck = false;
        setContentView(R.layout.activity_home);
        final SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        initDrawer();
        initBottomPromotionPanel();
        initBottomLocationPanel();
        findViewById(R.id.startBooking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View view = findViewById(R.id.service_card_holder);
                ImageView view1 = findViewById(R.id.service_plus_btn);
                AnimatedVectorDrawableCompat drawableCompat;

                if (view.getVisibility() == View.VISIBLE) {
                    view.setVisibility(View.GONE);
                    drawableCompat = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.cross_to_vehicle);

                } else {
                    view.setVisibility(View.VISIBLE);
                    drawableCompat = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.vehicle_to_cross);
                }
                view1.setImageDrawable(drawableCompat);
                drawableCompat.start();

            }
        });
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultCameraMap(true);
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                //findViewById(R.id.pickLocationMarker).setVisibility(View.GONE);
                CommonAnimation.slideDownHide(findViewById(R.id.pickLocationMarker), null);
                CommonAnimation.slideUpHide(findViewById(R.id.locationHome), new CommonAnimation.mAnimationListener() {
                    @Override
                    public void finished() {
                        CommonAnimation.slideDownShow(findViewById(R.id.mainHome), null);
                        bottom_sheet.getLayoutParams().height = CoordinatorLayout.LayoutParams.MATCH_PARENT;
                        bottom_sheet.requestLayout();
                        currentBottomFramment = new Messages();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, currentBottomFramment)
                                .commit();

                        //sheetBehavior.setHideable(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                sheetBehavior.setHideable(false);
                                willShowOverlay = true;
                            }
                        }, 500);
                    }
                });
            }
        });

        findViewById(R.id.showVehicleList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVehicleList();
            }
        });

        findViewById(R.id.topBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (findViewById(R.id.pickServceType).getVisibility() != View.GONE) {
                    CommonAnimation.slideDownHide(findViewById(R.id.pickServceType), null);
                }
                if (findViewById(R.id.vehiclePicker).getVisibility() != View.GONE) {
                    CommonAnimation.slideDownHide(findViewById(R.id.vehiclePicker), null);
                    mMap.setPadding(0, 0, 0, 0);
                }
                if (findViewById(R.id.fullBookingOptions).getVisibility() != View.GONE) {
                    CommonAnimation.slideDownHide(findViewById(R.id.fullBookingOptions), null);
                    mMap.setPadding(0, 0, 0, 0);
                }
                if (routingPath != null) {
                    routingPath.destroy();
                    routingPath = null;
                    setDefaultCameraMap(true);
                }
                CommonAnimation.slideUpHide(findViewById(R.id.topBackPanel), new CommonAnimation.mAnimationListener() {
                    @Override
                    public void finished() {
                        CommonAnimation.slideDownShow(findViewById(R.id.mainHome), null);
                        bottom_sheet.getLayoutParams().height = CoordinatorLayout.LayoutParams.MATCH_PARENT;
                        bottom_sheet.requestLayout();
                        currentBottomFramment = new Messages();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, currentBottomFramment)
                                .commit();

                        //sheetBehavior.setHideable(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                sheetBehavior.setHideable(false);
                                willShowOverlay = true;
                            }
                        }, 500);
                    }
                });
            }
        });

        findViewById(R.id.fullBookingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVehicleList();
            }
        });

        findViewById(R.id.fullBookingConfirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showInfo("Booking successfully made");
                //findViewById(R.id.topBackBtn).performClick();
                ConfirmDialog confirmDialog = new ConfirmDialog(ActivityHome.this);
                confirmDialog.show();

            }
        });

        findViewById(R.id.particularDetailsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choose Particular Details");

// add a list
                final String[] animals = {"iron", "coal", "furniture", "generator", "goat", "option1", "option2", "option3", "option4",
                        "option5", "option6", "option7", "option8", "option9", "option10"};
                builder.setItems(animals, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TextView) findViewById(R.id.panel_2_tv)).setText(animals[which]);
                        Animatable drawableCompat = (Animatable) ((ImageView) findViewById(R.id.panel_2_imv)).getDrawable();
                        drawableCompat.start();
                    }
                });

// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        findViewById(R.id.place_1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                if (findViewById(R.id.pickLocationMarker).getVisibility() == View.VISIBLE &&
                        findViewById(R.id.showVehicleList).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.showVehicleList).setVisibility(View.GONE);
                }
                return false;
            }
        });


        findViewById(R.id.place_2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                if (findViewById(R.id.pickLocationMarker).getVisibility() == View.VISIBLE &&
                        findViewById(R.id.showVehicleList).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.showVehicleList).setVisibility(View.GONE);
                }
                return false;
            }
        });

        findViewById(R.id.recp_de_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipientDialog recipientDialog = new RecipientDialog(ActivityHome.this);
                recipientDialog.show();
            }
        });

        findViewById(R.id.inst_panel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstructionDialog instructionDialog = new InstructionDialog(ActivityHome.this);
                instructionDialog.show();
            }
        });

        findViewById(R.id.wait_t_panel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                new SingleDateAndTimePickerDialog.Builder(ActivityHome.this)
                        .bottomSheet()
                        //.curved()
                        .minutesStep(10)
                        //.displayDaysOfMonth(true)
                        .displayHours(true)
                        .displayMinutes(true)
                        .displayAmPm(false)
                        .displayDays(false)
                        .displayYears(false)
                        .displayMonth(false)
                        .defaultDate(calendar.getTime())
                        .mainColor(getResources().getColor(R.color.black))
                        //.todayText("aujourd'hui")
                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                            @Override
                            public void onDisplayed(SingleDateAndTimePicker picker) {
                                //retrieve the SingleDateAndTimePicker
                            }
                        })
                        .title("Waiting time \"HOUR\" : \"MINUTE\"")
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
                               /* SimpleDateFormat sm = new SimpleDateFormat("dd MMM yy, hh:mm a", Locale.getDefault());
                                Print.e(sm.format(date));
                                Print.e(sm.format(calendar.getTime()));*/
                                long diff = date.getTime() - calendar.getTime().getTime();//as given
                                long hours = TimeUnit.MILLISECONDS.toHours(diff);
                                long mins = TimeUnit.MILLISECONDS.toMinutes(diff);
                                if (mins >= 60) {
                                    mins = mins % 60;
                                }
                                String h = hours > 1 ? " hours : " : " hour : ";
                                String m = mins > 1 ? " mins" : " min";
                                //Print.e(""+hours+"  "+mins);
                                ((TextView) findViewById(R.id.wait_t_panel_tv)).setText(hours + h + mins + m);
                                Animatable drawableCompat = (Animatable) ((ImageView) findViewById(R.id.wait_t_panel_imv)).getDrawable();
                                drawableCompat.start();
                            }
                        }).display();
            }
        });

        findViewById(R.id.add_field).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLocationField(v);
            }
        });
        View.OnClickListener service_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPickup();
            }
        };
        findViewById(R.id.service_1).setOnClickListener(service_click);
        findViewById(R.id.service_2).setOnClickListener(service_click);
        findViewById(R.id.service_3).setOnClickListener(service_click);
        findViewById(R.id.service_4).setOnClickListener(service_click);

    }

    private void openPickup() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(23.796163, 90.402558))
                .zoom(17)
                .build();

        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        View view = findViewById(R.id.service_card_holder);
        ImageView view1 = findViewById(R.id.service_plus_btn);
        AnimatedVectorDrawableCompat drawableCompat;
        view.setVisibility(View.GONE);
        drawableCompat = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.cross_to_vehicle);
        view1.setImageDrawable(drawableCompat);
        drawableCompat.start();
        sheetBehavior.setHideable(true);
        findViewById(R.id.showVehicleList).setVisibility(View.GONE);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        willShowOverlay = false;
        CommonAnimation.slideUpHide(findViewById(R.id.mainHome), new CommonAnimation.mAnimationListener() {
            @Override
            public void finished() {
                CommonAnimation.slideDownShow(findViewById(R.id.locationHome), new CommonAnimation.mAnimationListener() {
                    @Override
                    public void finished() {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, new SearchLocation())
                                .commit();
                        adjustHeightOfBottomSheet();
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        findViewById(R.id.pickLocationMarker).setVisibility(View.VISIBLE);
                        //CommonAnimation.slideUpShow(findViewById(R.id.pickLocationMarker), null);
                    }
                });
            }
        });
    }

    private void addLocationField(View vim) {
        LinearLayout loc_holder = findViewById(R.id.loc_holder);
        int visible_count = 0;
        for (int i = 0; i < loc_holder.getChildCount(); i++) {
            View v = loc_holder.getChildAt(i);
            if (v.getVisibility() != View.VISIBLE) {
                v.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adjustHeightOfBottomSheet();
                    }
                }, 500);
                break;
            }
        }
        for (int i = 0; i < loc_holder.getChildCount(); i++) {
            View v = loc_holder.getChildAt(i);
            if (v.getVisibility() == View.VISIBLE) {
                visible_count++;
            }
        }
        if (visible_count == 5) {
            View add = findViewById(R.id.add_field);
            add.setVisibility(View.INVISIBLE);
        }

    }

    public void removeLocationField(View v) {
        ((View) v.getParent()).setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adjustHeightOfBottomSheet();
            }
        }, 600);
        View vim = findViewById(R.id.add_field);
        if (vim.getVisibility() != View.VISIBLE) {
            vim.setVisibility(View.VISIBLE);
        }

    }

    private void adjustHeightOfBottomSheet() {
        View view = findViewById(R.id.locationHome);
        View view1 = findViewById(R.id.drawer_layout);
        int paddingDp = 23;
        float density = getResources().getDisplayMetrics().density;
        int etra = (int) (paddingDp * density);
        bottom_sheet.getLayoutParams().height = (view1.getHeight() - view.getHeight() + etra);
        bottom_sheet.invalidate();
        bottom_sheet.requestLayout();
    }

    public void setRecipient(String name, String number) {
        ((TextView) findViewById(R.id.rec_panel_tv)).setText(name + "(" + number + ")");
        Animatable drawableCompat = (Animatable) ((ImageView) findViewById(R.id.rec_panel_imv)).getDrawable();
        drawableCompat.start();
    }

    public void setInstruction(String instruction) {
        ((TextView) findViewById(R.id.ins_pane_tv)).setText("I wanna do this, that, bla bla...");
        Animatable drawableCompat = (Animatable) ((ImageView) findViewById(R.id.ins_pane_img)).getDrawable();
        drawableCompat.start();
    }

  /*  private void showServiceType() {
        LatLng start = new LatLng(23.799185, 90.402057);
        LatLng end = new LatLng(22.559636, 91.687415);
        routingPath = new RoutingPath(this, start, end, mMap, true);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(start);
        builder.include(end);


        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        CommonAnimation.slideDownHide(findViewById(R.id.pickLocationMarker), new CommonAnimation.mAnimationListener() {
            @Override
            public void finished() {
                CommonAnimation.slideUpShow(findViewById(R.id.pickServceType), null);
            }
        });

        CommonAnimation.slideUpHide(findViewById(R.id.locationHome), new CommonAnimation.mAnimationListener() {
            @Override
            public void finished() {
                CommonAnimation.slideDownShow(findViewById(R.id.topBackPanel), null);
            }
        });

    }*/

    private void showVehicleList() {

        LatLng start = new LatLng(23.799185, 90.402057);
        LatLng end = new LatLng(22.559636, 91.687415);
        routingPath = new RoutingPath(this, start, end, mMap, true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.vehiclePicker, new VehiclePicker())
                .commit();

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        CommonAnimation.slideUpHide(findViewById(R.id.locationHome), new CommonAnimation.mAnimationListener() {
            @Override
            public void finished() {
                CommonAnimation.slideDownShow(findViewById(R.id.topBackPanel), null);
            }
        });
        CommonAnimation.slideDownHide(findViewById(R.id.pickLocationMarker), new CommonAnimation.mAnimationListener() {
            @Override
            public void finished() {
                int paddingDp = 268;
                float density = getResources().getDisplayMetrics().density;
                int etra = (int) (paddingDp * density);
                mMap.setPadding(0, 0, 0, etra);

                LatLng start = new LatLng(23.799185, 90.402057);
                LatLng end = new LatLng(22.559636, 91.687415);
                // routingPath = new RoutingPath(this, start, end, mMap, false);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(start);
                builder.include(end);


                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                mMap.animateCamera(cu);
                CommonAnimation.slideUpShow(findViewById(R.id.vehiclePicker), null);
            }
        });


    }

    public void showFullBookingOptions() {

        CommonAnimation.slideDownHide(findViewById(R.id.vehiclePicker), new CommonAnimation.mAnimationListener() {
            @Override
            public void finished() {
                int paddingDp = 360;
                float density = getResources().getDisplayMetrics().density;
                int etra = (int) (paddingDp * density);
                mMap.setPadding(0, 0, 0, etra);

                LatLng start = new LatLng(23.799185, 90.402057);
                LatLng end = new LatLng(22.559636, 91.687415);
                // routingPath = new RoutingPath(this, start, end, mMap, false);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(start);
                builder.include(end);
                AnimatedVectorDrawableCompat drawableCompat = AnimatedVectorDrawableCompat.create(ActivityHome.this, R.drawable.arrow_to_checked_2);
                ((ImageView) findViewById(R.id.panel_2_imv)).setImageDrawable(drawableCompat);
                AnimatedVectorDrawableCompat drawableCompat1 = AnimatedVectorDrawableCompat.create(ActivityHome.this, R.drawable.arrow_to_checked_2);
                ((ImageView) findViewById(R.id.panel_1_imv)).setImageDrawable(drawableCompat1);

                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 120);
                mMap.animateCamera(cu);
                CommonAnimation.slideUpShow(findViewById(R.id.fullBookingOptions), new CommonAnimation.mAnimationListener() {
                    @Override
                    public void finished() {
                        Animatable drawableCompat = (Animatable) ((ImageView) findViewById(R.id.panel_1_imv)).getDrawable();
                        drawableCompat.start();
                    }
                });


            }
        });

    }

    private void initBottomLocationPanel() {
        //sheetBehavior2 = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_2));
        // sheetBehavior2.setHideable(true);
        //sheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);


    }

    private void initBottomPromotionPanel() {
        //toolBarBottom = findViewById(R.id.toolBarBottom);
        back_overlay = findViewById(R.id.back_overlay);

        bottom_sheet = findViewById(R.id.bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from((View) bottom_sheet);

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //btnBottomSheet.setText("Expand Sheet");
                        int paddingDp = 8;
                        float density = getResources().getDisplayMetrics().density;
                        paddingPixel = (int) (paddingDp * density);
                        bottomSheet.setPadding(paddingPixel, 0, paddingPixel, 0);

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            int paddingPixel;
            int heightPixel;

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //Print.e(""+slideOffset);
                if (slideOffset <= 0.5) {
                    int paddingDp = 8 - (int) (8 * slideOffset * 2);
                    float density = getResources().getDisplayMetrics().density;
                    paddingPixel = (int) (paddingDp * density);
                    bottomSheet.setPadding(paddingPixel, 0, paddingPixel, 0);
                    if (willShowOverlay) {
                        back_overlay.setAlpha(slideOffset * 2);
                    }
                    //Print.e(""+paddingPixel);
                }
                if (slideOffset > 0.5 && paddingPixel != 0) {
                    paddingPixel = 0;
                    bottomSheet.setPadding(paddingPixel, 0, paddingPixel, 0);
                }
                if (slideOffset > 0.5 && back_overlay.getAlpha() != 1 && willShowOverlay) {
                    back_overlay.setAlpha(1);
                }
                if (back_overlay.getAlpha() != 0 && !willShowOverlay) {
                    back_overlay.setAlpha(0);
                }
                //toolBarBottom.setScaleY();
                /*if(slideOffset >= 0.5) {
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
                }*/
                //back_overlay.setAlpha(slideOffset);

                if (currentBottomFramment != null) {
                    currentBottomFramment.onSlide(slideOffset);
                }

            }
        });

       /* findViewById(R.id.bottomSheetClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
*/

        currentBottomFramment = new Messages();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, currentBottomFramment)
                .commit();
    }

    private void initDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        findViewById(R.id.drawerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(20);
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style_version_2);
        mMap.setMapStyle(style);
        mMap.getUiSettings().setCompassEnabled(false);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (findViewById(R.id.pickLocationMarker).getVisibility() == View.VISIBLE &&
                        findViewById(R.id.showVehicleList).getVisibility() != View.VISIBLE) {
                    findViewById(R.id.showVehicleList).setVisibility(View.VISIBLE);
                }
            }
        });
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (findViewById(R.id.pickLocationMarker).getVisibility() == View.VISIBLE &&
                        findViewById(R.id.showVehicleList).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.showVehicleList).setVisibility(View.GONE);
                }
                if (findViewById(R.id.pickLocationMarker).getVisibility() == View.VISIBLE && sheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });
        setDefaultCameraMap(false);
    }

    private void setDefaultCameraMap(boolean animate) {
        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
        }

        GPSTracker gpsTracker = new GPSTracker(this);
        Location location = gpsTracker.getLocation();
        if (location != null && false) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(17)
                    .build();

            if (animate) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
            // addOverlay(new LatLng(location.getLatitude(), location.getLongitude()));
        }else{
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(23.796163, 90.402558))
                    .zoom(12)
                    .build();

            if (animate) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }

        addRandomMarkers();
    }

    private void addRandomMarkers() {
        final Marker []markers  = new Marker[4];

        MarkerOptions options = new MarkerOptions();
        IconGenerator tc = new IconGenerator(this);
        //tc.setStyle(IconGenerator.STYLE_GREEN);
        //tc.setBackground(getResources().getDrawable(R.drawable.bus_example));
        tc.setColor(Color.parseColor("#68BB59"));
        tc.setTextAppearance(this, R.style.Marker_Ongoing);
        Bitmap bmp = tc.makeIcon("HG-8456\nOngoing"); // pass the text you want.
        options.position(new LatLng(23.798792, 90.393757));
        options.icon(BitmapDescriptorFactory.fromBitmap(bmp));
        markers[0] = mMap.addMarker(options);

        MarkerOptions options1 = new MarkerOptions();
        IconGenerator tc1 = new IconGenerator(this);
        //tc.setStyle(IconGenerator.STYLE_GREEN);
        //tc.setBackground(getResources().getDrawable(R.drawable.bus_example));
        tc1.setColor(Color.parseColor("#577590"));
        tc1.setTextAppearance(this, R.style.Marker_Ongoing);
        Bitmap bmp1 = tc1.makeIcon("HG-8456\nAccepted"); // pass the text you want.
        options1.position(new LatLng(23.755641, 90.441163));
        options1.icon(BitmapDescriptorFactory.fromBitmap(bmp1));
        markers[1] = mMap.addMarker(options1);

        MarkerOptions options2 = new MarkerOptions();
        IconGenerator tc2 = new IconGenerator(this);
        //tc.setStyle(IconGenerator.STYLE_GREEN);
        //tc.setBackground(getResources().getDrawable(R.drawable.bus_example));
        tc2.setColor(Color.parseColor("#FFA500"));
        tc2.setTextAppearance(this, R.style.Marker_Ongoing);
        Bitmap bmp2 = tc2.makeIcon("HG-8456\nArriving"); // pass the text you want.
        options2.position(new LatLng(23.762554, 90.353186));
        options2.icon(BitmapDescriptorFactory.fromBitmap(bmp2));
        markers[2] = mMap.addMarker(options2);


        MarkerOptions options3 = new MarkerOptions();
        IconGenerator tc3 = new IconGenerator(this);
        //tc.setStyle(IconGenerator.STYLE_GREEN);
        //tc.setBackground(getResources().getDrawable(R.drawable.bus_example));
        tc3.setColor(Color.parseColor("#808080"));
        tc3.setTextAppearance(this, R.style.Marker_Ongoing);
        Bitmap bmp3 = tc3.makeIcon("HG-8456\nOpen"); // pass the text you want.
        options3.position(new LatLng(23.818659, 90.420880));
        options3.icon(BitmapDescriptorFactory.fromBitmap(bmp3));
        markers[3] = mMap.addMarker(options3);

       final Handler handler  =  new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
                animateMarker(markers[(int) (Math.random() * 4)]);
                handler.postDelayed(this, 3000 + (int)(Math.random() * ((7000 - 3000) + 1)));
           }
       }, 3000);

    }

    private void animateMarker(final Marker m){
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(500);
        animator.setRepeatCount(3);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int t = (int) animator.getAnimatedValue();
                m.setAnchor(0.5f, 1f + (0.2f * (t < 50 ? t : (100 - t)) / 100));
                //Print.e(""+(1.0f + 1.2f * t));

            }
        });
        animator.start();
    }


    @Override
    public void onFragmentInteraction() {

    }


}
