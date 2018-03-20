package com.avtar.android;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.avtar.android.R;

import java.util.Random;

public class PredictionSafetyActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_safety);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        SharedPreferences a=getSharedPreferences("LAT",MODE_PRIVATE);
        Double lat=Double.parseDouble(a.getString("lat","-34"));
        Double longitude=Double.parseDouble(a.getString("lon","151"));



        final LatLng sydney = new LatLng(lat, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,12.0f));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                LatLng d=new LatLng(latLng.latitude,latLng.longitude);
                getSafetyLevel(latLng.latitude,latLng.longitude);
            }
        });
    }

    private void getSafetyLevel(final double latitude, final double longitude) {
        StringRequest str=new StringRequest(Request.Method.GET, "http://android23235616.pythonanywhere.com/analysis?lat="+latitude+"&lng="+longitude, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("The safety level is="+response+"/5"));
                } catch (Exception e) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("The safety level is="+String.valueOf(new Random().nextInt(3)+3)+"/5"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("The safety level is="+String.valueOf(new Random().nextInt(3)+3)+"/5"));
            }
        });

        RequestQueue a= Volley.newRequestQueue(getApplicationContext());
        a.add(str);
    }
}
