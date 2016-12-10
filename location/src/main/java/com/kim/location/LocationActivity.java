package com.kim.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private TextView text;
    private Button button;
    private Button button1;
    private Button button2;
    private EditText edit1;
    private EditText edit2;
    private Button button3;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        edit1 = (EditText) findViewById(R.id.edit1);
        edit2 = (EditText) findViewById(R.id.edit2);
        button3 = (Button) findViewById(R.id.button3);

        locationManager = (LocationManager) LocationActivity.this.getSystemService(Context.LOCATION_SERVICE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> providers = locationManager.getAllProviders();
                String provider = "";
                for (Iterator iterator = providers.iterator(); iterator.hasNext(); ) {
                    provider += (String) iterator.next() + "\n";
                }
                text.setText(provider);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAltitudeRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(false);
                String provider = locationManager.getBestProvider(criteria, false);
                text.setText("Best provider:" + provider);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocation();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = edit1.getText().toString().trim();
                String text2 = edit2.getText().toString().trim();
                if (!text1.equals("")) {
                    new GeocodingTask().execute();
                } else if (!text2.equals("")) {
                    new ReverseGeocodingTask().execute();
                }
            }
        });
    }

    public void showLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, new MyLocationListener());
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            String loc = "x:" + location.getLongitude() + ",y:" + location.getLatitude();
            Log.d("MyLocationListener", "onLocationChanged: " + loc);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private class GeocodingTask extends AsyncTask<Integer, Integer, List<Address>> {

        @Override
        protected List<Address> doInBackground(Integer... params) {
            try {
                Geocoder geocoder = new Geocoder(LocationActivity.this);
                List<Address> addresses = geocoder.getFromLocationName("SFO", 1);
                return addresses;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            super.onPostExecute(addresses);
            text.setText(addresses.get(0).getCountryName());
        }
    }

    private class ReverseGeocodingTask extends AsyncTask<Integer, Integer, List<Address>> {

        @Override
        protected List<Address> doInBackground(Integer... params) {
            try {
                Geocoder geocoder = new Geocoder(LocationActivity.this);
                return geocoder.getFromLocation(40.714224, -79.961452, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            super.onPostExecute(addresses);
            text.setText(addresses.get(0).getCountryName());
        }
    }
}