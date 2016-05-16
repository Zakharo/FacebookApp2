package com.example.vladzakharo.facebookapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class PublishActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    Button btnSubmit;
    EditText etMessage;
    EditText etName;
    EditText etCaption;
    EditText etDescription;
    EditText etPicture;
    EditText etLink;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_publish);

        //Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        etMessage = (EditText) findViewById(R.id.etMessage);
        etName = (EditText) findViewById(R.id.etName);
        etCaption = (EditText) findViewById(R.id.etCaption);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etPicture = (EditText) findViewById(R.id.etPicture);
        etLink = (EditText) findViewById(R.id.etLink);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });
    }

    public void Submit(){
        Bundle params = new Bundle();
        if (etMessage.getText().length() != 0) {params.putString("message", etMessage.getText().toString());}
        if (etName.getText().length() != 0) {params.putString("name", etName.getText().toString());}
        if (etCaption.getText().length() != 0) {params.putString("caption", etCaption.getText().toString());}
        if (etDescription.getText().length() != 0) {params.putString("description", etDescription.getText().toString());}
        if (etPicture.getText().length() != 0) {params.putString("picture", etPicture.getText().toString());}
        if (etLink.getText().length() != 0) {params.putString("link", etLink.getText().toString());}
        params.putString("place", "");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Toast.makeText(getApplicationContext(), "Отправлено", Toast.LENGTH_SHORT).show();
                        if (ContextCompat.checkSelfPermission(PublishActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(PublishActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if (mLastLocation != null){
                                //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
                                //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));

                                Intent setMarker = new Intent(PublishActivity.this, MapsActivity.class);
                                setMarker.putExtra("latitude", mLastLocation.getLatitude());
                                setMarker.putExtra("longitude", mLastLocation.getLongitude());
                                setMarker.putExtra("message", etMessage.getText().toString());
                                startActivity(setMarker);
                            }
                        }
                    }
                }
        ).executeAsync();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnected(Bundle bundle) {/*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null){
                //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
                //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));

                Intent setMarker = new Intent(PublishActivity.this, MapsActivity.class);
                setMarker.putExtra("latitude", mLastLocation.getLatitude());
                setMarker.putExtra("longitude", mLastLocation.getLongitude());
                setMarker.putExtra("message", etMessage.getText().toString());
                startActivity(setMarker);
            }
        }
*/
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
