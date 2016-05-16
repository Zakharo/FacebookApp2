package com.example.vladzakharo.facebookapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng marker;
    Double latitude;
    String lat;
    Double longitude;
    String longit;
    String message;
    ArrayList<String> latitudeList;
    ArrayList<String> longitudeList;
    ArrayList<String> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        //Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();
        messageList = new ArrayList<>();

        if (getIntent().getStringExtra("message") != null ) {
            latitude = getIntent().getExtras().getDouble("latitude");
            longitude = getIntent().getExtras().getDouble("longitude");
            message = getIntent().getStringExtra("message");
            lat = String.valueOf(latitude);
            longit = String.valueOf(longitude);

            latitudeList.add(lat);
            longitudeList.add(longit);
            messageList.add(message);
        }

        for (int i = 0; i < messageList.size(); i++){
            latitude = Double.parseDouble(latitudeList.get(i));
            longitude = Double.parseDouble(longitudeList.get(i));
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                .title(message));
        }
    }

}
