package com.example.bunfei.location_project;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class FinalActivity extends Activity implements
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener {

    static final String API_URL = "http://220.123.184.109:8080/KISTI_Web/sensor/whole.do";

    LatLng position0;
    LatLng position1;
    LatLng position2;
    LatLng position3;
    LatLng position4;
    LatLng position5;
    LatLng position6;
    LatLng position7;
    LatLng position8;
    LatLng position9;

    String[] latlong = new String[20];
    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new FinalActivity.RetrieveFeedTask().execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        //MapFragment mapFragment = (MapFragment) getFragmentManager()
        //        .findFragmentById(R.id.
        //                map10);
        //mapFragment.getMapAsync(this);
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();

                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            //progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            //responseView.setText(response);

            //double lng = 128.0;
            //double lat = 35.0;
            final double[] attributes = getIntent().getExtras().getDoubleArray("attributes");
            double lng = attributes[4];
            double lat = attributes[5];
            double[] scores = new double[1];

            try {
                JSONArray array = new JSONArray(response);
                int length = array.length();

                scores = new double[length];
                Arrays.fill(scores, -5);

                for (int j = 0; j<length; j++){
                    JSONObject obj1 = array.getJSONObject(j);
                    double LNG = obj1.getDouble("LNG");
                    double LAT = obj1.getDouble("LAT");

                    if (abs(LNG-lng)<0.05 && abs(LAT-lat)<0.05){
                        double pm25val = 0.0;
                        String[] pm25 = obj1.getString("PM2.5").split(";");
                        if (pm25.length==1){
                            pm25val = Double.parseDouble(pm25[0]);
                        } else{
                            pm25val = (Double.parseDouble(pm25[0])+ Double.parseDouble(pm25[1])/2.0);
                        }
                        double pm10val = 0.0;
                        String[] pm10 = obj1.getString("PM10").split(";");
                        if (pm10.length==1){
                            pm10val = Double.parseDouble(pm10[0]);
                        } else{
                            pm10val = (Double.parseDouble(pm10[0])+ Double.parseDouble(pm10[1])/2.0);
                        }
                        double particulate_score = ((15.0-pm25val)/15.0)*(2.0/3.0)+((30.0-pm10val)/30.0)*(1.0/3.0);

                        double COval = Double.parseDouble(obj1.getString("CO"));
                        double NO2val = Double.parseDouble(obj1.getString("NO2"));
                        double SO2val = Double.parseDouble(obj1.getString("SO2"));
                        double gaseous_score = ((2.0-COval)/2.0+(0.03-NO2val)/0.03+(0.02-SO2val)/0.02)/3.0;

                        double humidity_score = (70.0-Double.parseDouble(obj1.getString("HUM")))/70.0;
                        double noise_score = (45.0-Double.parseDouble(obj1.getString("MCP")))/45.0;

                        scores[j] = particulate_score*attributes[0]+gaseous_score*attributes[1]
                                +humidity_score*attributes[2]+noise_score*attributes[3];
                    }

                }

                for (int i=0;i<10;i++){
                    double highest = -5.0;
                    int index = 0;
                    for (int j = 0; j<length; j++) {
                        if (scores[j]>highest){
                            highest = scores[j];
                            index = j;
                        }
                    }
                    scores[index] = -5.0;
                    JSONObject obj1 = array.getJSONObject(index);
                    String LNG = obj1.getString("LNG");
                    String LAT = obj1.getString("LAT");
                    latlong[2*i] = LAT;
                    latlong[2*i+1] = LNG;
                }
            } catch (JSONException e) {
                scores[0] = 1;// Appropriate error handling code
            }
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latlong,
                    getApplicationContext(), new FinalActivity.GeocoderHandler());

            information(latlong);
        }
    }

    public void information(String[] latlong){
        position0 = new LatLng(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
        position1 = new LatLng(Double.parseDouble(latlong[2]), Double.parseDouble(latlong[3]));
        position2 = new LatLng(Double.parseDouble(latlong[4]), Double.parseDouble(latlong[5]));
        position3 = new LatLng(Double.parseDouble(latlong[6]), Double.parseDouble(latlong[7]));
        position4 = new LatLng(Double.parseDouble(latlong[8]), Double.parseDouble(latlong[9]));
        position5 = new LatLng(Double.parseDouble(latlong[10]), Double.parseDouble(latlong[11]));
        position6 = new LatLng(Double.parseDouble(latlong[12]), Double.parseDouble(latlong[13]));
        position7 = new LatLng(Double.parseDouble(latlong[14]), Double.parseDouble(latlong[15]));
        position8 = new LatLng(Double.parseDouble(latlong[16]), Double.parseDouble(latlong[17]));
        position9 = new LatLng(Double.parseDouble(latlong[18]), Double.parseDouble(latlong[19]));
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            address = bundle.getString("address");
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.
                            map10);
            mapFragment.getMapAsync(FinalActivity.this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        String[] addresses = address.split("\n");
        String[] output = new String[10];
        for (int i = 0; i<10; i++){
            String line = addresses[2+5*i]+" "+addresses[3+5*i]+", "+addresses[5+5*i];
            output[i] = line;
        }

        double longitude = 0.0;
        longitude += position0.longitude;
        longitude += position1.longitude;
        longitude += position2.longitude;
        longitude += position3.longitude;
        longitude += position4.longitude;
        longitude += position5.longitude;
        longitude += position6.longitude;
        longitude += position7.longitude;
        longitude += position8.longitude;
        longitude += position9.longitude;
        longitude = longitude/10.0;
        double latitude = 0.0;
        latitude += position0.latitude;
        latitude += position1.latitude;
        latitude += position2.latitude;
        latitude += position3.latitude;
        latitude += position4.latitude;
        latitude += position5.latitude;
        latitude += position6.latitude;
        latitude += position7.latitude;
        latitude += position8.latitude;
        latitude += position9.latitude;
        latitude = latitude/10.0;


        LatLng position_avg = new LatLng(latitude,longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position_avg, 13));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
        map.animateCamera(zoom);

        map.addMarker(new MarkerOptions()
                .title("Address:")
                .snippet(output[0])
                .position(position0))
                .setDraggable(true);
        map.addMarker(new MarkerOptions()
                .title("Address:")
                .snippet(output[1])
                .position(position1))
                .setDraggable(true);
        map.addMarker(new MarkerOptions()
                .title("Address:")
                .snippet(output[2])
                .position(position2))
                .setDraggable(true);
        map.addMarker(new MarkerOptions()
                .title("Address:")
                .snippet(output[3])
                .position(position3))
                .setDraggable(true);
        map.addMarker(new MarkerOptions()
                .title("Address:")
                .snippet(output[4])
                .position(position4))
                .setDraggable(true);
        map.addMarker(new MarkerOptions()
                .title("Address:")
                .snippet(output[5])
                .position(position5))
                .setDraggable(true);
        map.addMarker(new MarkerOptions()
                .title("Address:")
                .snippet(output[6])
                .position(position6))
                .setDraggable(true);
        map.addMarker(new MarkerOptions()
                .title("Address:")
                .snippet(output[7])
                .position(position7))
                .setDraggable(true);
        map.addMarker(new MarkerOptions()
                .title("Address:")
                .snippet(output[8])
                .position(position8))
                .setDraggable(true);
        map.addMarker(new MarkerOptions()
                .title("Address:")
                .snippet(output[9])
                .position(position9))
                .setDraggable(true);

        // map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerDragListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        double[] latlong = new double[2];
        latlong[0] = marker.getPosition().latitude;
        latlong[1] = marker.getPosition().longitude;
        String address = marker.getSnippet();

        Intent intent = new Intent(FinalActivity.this, AnalysisActivity.class);
        intent.putExtra("latlong", latlong);
        intent.putExtra("address", address);
        startActivity(intent);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position0 = marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
                position0.latitude,
                position0.longitude));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position0 = marker.getPosition();

        Log.d(getClass().getSimpleName(),
                String.format("Dragging to %f:%f", position0.latitude,
                        position0.longitude));

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        position0 = marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Dragged to %f:%f",
                position0.latitude,
                position0.longitude));
    }
}
