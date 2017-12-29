package com.example.bunfei.location_project;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.Math.abs;

public class NextActivity extends AppCompatActivity {

    public static int Particulate_check = 0;
    public static int Gaseous_check = 0;
    public static int Humidity_check = 0;
    public static int Noise_check = 0;
    static final String API_URL = "http://220.123.184.109:8080/KISTI_Web/sensor/whole.do";
    double averagePollution = 0.0;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        textView = (TextView) findViewById(R.id.textView);

        new NextActivity.RetrieveFeedTask().execute();

        final Button button = (Button) findViewById(R.id.Continue);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final CheckBox checkBox = (CheckBox) findViewById(R.id.Particulate);
                if (checkBox.isChecked()) {
                    Particulate_check = 1;
                }
                final CheckBox checkBox1 = (CheckBox) findViewById(R.id.Gaseous);
                if (checkBox1.isChecked()) {
                    Gaseous_check = 1;
                }
                final CheckBox checkBox2 = (CheckBox) findViewById(R.id.Humidity);
                if (checkBox2.isChecked()) {
                    Humidity_check = 1;
                }
                final CheckBox checkBox3 = (CheckBox) findViewById(R.id.Noise);
                if (checkBox3.isChecked()) {
                    Noise_check = 1;
                }

                double[] attributes = new double[6];
                attributes[0] = Particulate_check;
                attributes[1] = Gaseous_check;
                attributes[2] = Humidity_check;
                attributes[3] = Noise_check;

                double[] latlong_info = getIntent().getExtras().getDoubleArray("latlong");
                double lng = latlong_info[1];
                double lat = latlong_info[0];
                attributes[4] = lng;
                attributes[5] = lat;

                Intent nextIntent = new Intent(NextActivity.this, FinalActivity.class);
                nextIntent.putExtra("attributes", attributes);
                startActivity(nextIntent);
            }
        });
    }

    public void sendNotification(View view, int c){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("My notification")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);

        if(c ==0){
            mBuilder.setContentText("Good zone!");
        }
        else if(c ==1){
            mBuilder.setContentText("Moderate zone!");
        }
        else if(c ==2){
            mBuilder.setContentText("Unhealthy zone!");
        }
        else{
            mBuilder.setContentText("Hazardous zone!");
        }

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());

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
            Log.i("INFO", response);


            double[] latlong_info = getIntent().getExtras().getDoubleArray("latlong");

            double lng = latlong_info[1];
            double lat = latlong_info[0];



            String[] pm25_info;

            int a = 0;
            try {
                JSONArray array = new JSONArray(response);
                int length = array.length();

                pm25_info = new String[length];

                for (int j = 0; j<length; j++){
                    JSONObject obj1 = array.getJSONObject(j);
                    double LNG = obj1.getDouble("LNG");
                    double LAT = obj1.getDouble("LAT");

                    if (abs(LNG-lng)<2 && abs(LAT-lat)<2){
                        String[] pm25 = obj1.getString("PM2.5").split(";");
                        if (pm25.length==1){
                            pm25_info[a] = pm25[0];
                        } else{
                            pm25_info[a] = String.valueOf(Double.parseDouble(pm25[0])+ Double.parseDouble(pm25[1])/2.0);
                        }
                        a ++;
                    }
                }
            } catch (JSONException e) {
                pm25_info = new String[1];
                pm25_info[0] = "undefined";// Appropriate error handling code
            }

            double b = 0.0;
            for (String value:pm25_info){
                if (value!=null){
                    averagePollution = averagePollution + Double.parseDouble(value);
                    b = b+1.0;
                }
            }
            averagePollution = averagePollution/b;
            if (averagePollution<15.0){
                sendNotification(textView,0);
            }
            else if (averagePollution<50.0){
                sendNotification(textView,1);
            }
            else if (averagePollution<100.0){
                sendNotification(textView,2);
            }
            else if (averagePollution>=100.0){
                sendNotification(textView,3);
            }
        }
    }
}
