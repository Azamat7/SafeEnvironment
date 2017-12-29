package com.example.bunfei.location_project;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationAddress {
    private static final String TAG = "LocationAddress";

    public static void getAddressFromLocation(final String[] latlong,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String[] result = new String[10];
                try {
                    for (int i = 0; i<10; i++) {
                        double latitude = Double.parseDouble(latlong[2*i]);
                        double longitude = Double.parseDouble(latlong[2*i+1]);
                        List<Address> addressList = geocoder.getFromLocation(
                                latitude, longitude, 10);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            for (int j = 0; j < address.getMaxAddressLineIndex(); j++) {
                                sb.append(address.getAddressLine(j)).append("\n");
                            }
                            sb.append(address.getLocality()).append("\n");
                            //sb.append(address.getPostalCode()).append("\n");
                            sb.append(address.getCountryName());
                            result[i] = sb.toString();
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    String result1 = "These are 10 best address suggestions";

                    for (int i = 0; i<10; i++) {
                        result1 = result1 + "\nAddress:\n" + result[i];
                    }

                    bundle.putString("address", result1);
                    message.setData(bundle);
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
