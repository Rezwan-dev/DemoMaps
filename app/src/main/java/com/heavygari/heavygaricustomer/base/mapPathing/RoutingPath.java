package com.heavygari.heavygaricustomer.base.mapPathing;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.SquareCap;
import com.google.maps.android.ui.IconGenerator;
import com.heavygari.heavygaricustomer.R;
import com.heavygari.heavygaricustomer.base.utils.Print;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class RoutingPath {

    private Activity context;
    private final LatLng origin;
    private final LatLng dest;
    private GoogleMap map;
    private final boolean willAnimate;
    private ValueAnimator animator;
    private ArrayList<LatLng> points;
    private Polyline blackPolyLine, greyPolyLine;

    public RoutingPath(Activity context, LatLng origin, LatLng dest, GoogleMap map, boolean willAnimate){
        this.context = context;
        this.origin = origin;
        this.dest = dest;
        this.map = map;
        this.willAnimate = willAnimate;
        String url = getDirectionsUrl(origin, dest);
        map.clear();
        new DownloadTask().execute(url);
    }

    public void destroy(){
        if(animator != null) {
            animator.removeAllUpdateListeners();
            animator.cancel();
        }
        map.clear();
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // API key
        String key = "key="+context.getResources().getString(R.string.google_maps_key);
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + key;
        // Output format
        String output = "json";
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    private void animatePolyLine() {
        //greyPolyLine.setPoints(points);

        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(800);
        //animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {

                List<LatLng> latLngList = blackPolyLine.getPoints();
                int initialPointSize = latLngList.size();
                int animatedValue = (int) animator.getAnimatedValue();
                int newPoints = (animatedValue * points.size()) / 100;

                if (initialPointSize < newPoints ) {
                    latLngList.addAll(points.subList(initialPointSize, newPoints));
                    blackPolyLine.setPoints(latLngList);
                }


            }
        });


        Animator.AnimatorListener polyLineAnimationListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                addMarkerFirst(null);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                addMarkerLast(null);


                //RoutingPath.this.animator.st();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                //Print.e("REPEAT");
                List<LatLng> blackLatLng = blackPolyLine.getPoints();
                List<LatLng> greyLatLng = greyPolyLine.getPoints();

                greyLatLng.clear();
                greyLatLng.addAll(blackLatLng);
                blackLatLng.clear();

                blackPolyLine.setPoints(blackLatLng);
                greyPolyLine.setPoints(greyLatLng);
                blackPolyLine.setZIndex(2);

            }
        };

        animator.addListener(polyLineAnimationListener);
        animator.start();

    }

    public void addMarker(LatLng destination, String title) {
        MarkerOptions options = new MarkerOptions();
        options.position(destination);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        map.addMarker(options);

    }
    public void addMarkerFirst(String title) {
        if(points.size() < 1){
            return;
        }
        MarkerOptions options = new MarkerOptions();
        IconGenerator tc = new IconGenerator(context);
        Bitmap bmp = tc.makeIcon("Pick Up"); // pass the text you want.
        options.position(points.get(0));
        options.icon(BitmapDescriptorFactory.fromBitmap(bmp));
        map.addMarker(options);

    }

    public void addMarkerLast(String title) {
        if(points.size() < 1){
            return;
        }
        MarkerOptions options = new MarkerOptions();
        IconGenerator tc = new IconGenerator(context);
        Bitmap bmp = tc.makeIcon("Drop"); // pass the text you want.
        options.position(points.get(points.size() - 1));
        options.icon(BitmapDescriptorFactory.fromBitmap(bmp));
        map.addMarker(options);

    }

        @SuppressLint("StaticFieldLeak")
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.e("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            HttpURLConnection urlConnection;
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            try (InputStream iStream = urlConnection.getInputStream()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {
                Log.e("Exception while ", e.toString());
            } finally {
                urlConnection.disconnect();
            }
            return data;
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                Log.e("Background Task", e.toString());
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            try {


                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    List<HashMap<String, String>> path = result.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                }
                if (map != null && !willAnimate && points.size() > 0) {
                    PolylineOptions lineOptions = new PolylineOptions();
                    lineOptions.addAll(points);
                    lineOptions.width(7);
                    lineOptions.color(context.getResources().getColor(R.color.path_color));
                    map.addPolyline(lineOptions);
                }

                if (map != null&& willAnimate && points.size() > 0){
                    PatternItem DOT = new Dot();
                    PatternItem DASH = new Dash(10);
                    PatternItem GAP = new Gap(12);

                   // List<PatternItem> PATTERN_POLYGON_BETA = Arrays.asList(DOT, GAP, DASH, GAP);
                    List<PatternItem> PATTERN_POLYGON_BETA = Arrays.asList(DOT, GAP);


                    PolylineOptions greyOptions = new PolylineOptions();
                    greyOptions.width(12);
                    greyOptions.color(Color.GRAY);
                    /*greyOptions.startCap(new RoundCap());
                    greyOptions.endCap(new RoundCap());*/
                    //greyOptions.jointType(ROUND);
                    greyOptions.pattern(PATTERN_POLYGON_BETA);
                    greyPolyLine = map.addPolyline(greyOptions);

                    PolylineOptions blackOptions = new PolylineOptions();
                    blackOptions.width(12);
                    blackOptions.color(Color.BLACK);
                   /* blackOptions.startCap(new RoundCap());
                    blackOptions.endCap(new RoundCap());*/
                    //blackOptions.jointType(ROUND);
                    blackOptions.pattern(PATTERN_POLYGON_BETA);
                    blackPolyLine = map.addPolyline(blackOptions);

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            animatePolyLine();
                        }
                    });

                }
            } catch (Exception ex) {
                Print.e("map error", String.valueOf(ex));
            }
        }
    }
}
