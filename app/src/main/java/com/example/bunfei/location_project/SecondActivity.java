package com.example.bunfei.location_project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.Math.abs;


public class SecondActivity extends AppCompatActivity {

    TextView responseView;
    ProgressBar progressBar;
    static final String API_URL = "http://220.123.184.109:8080/KISTI_Web/sensor/whole.do";
    double averagePollution = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        responseView = (TextView) findViewById(R.id.responseView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });
    }


    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            progressBar.setVisibility(View.GONE);
            responseView.setText(locationAddress);
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {

            // Do some validation here

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
            String[] latlong = new String[20];


            try {
                JSONArray array = new JSONArray(response);
                int length = array.length();

                scores = new double[length];

                for (int j = 0; j<length; j++){
                    JSONObject obj1 = array.getJSONObject(j);
                    double LNG = obj1.getDouble("LNG");
                    double LAT = obj1.getDouble("LAT");

                    if (abs(LNG-lng)<2 && abs(LAT-lat)<2){
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
                responseView.setText("No enough information");// Appropriate error handling code
            }

            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latlong,
                    getApplicationContext(), new GeocoderHandler());

        }
    }
}