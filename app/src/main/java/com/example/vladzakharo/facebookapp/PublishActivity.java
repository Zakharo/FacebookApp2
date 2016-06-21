package com.example.vladzakharo.facebookapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PublishActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    Button btnSubmit;
    Button btnPlace;
    EditText etMessage;
    EditText etName;
    EditText etCaption;
    EditText etDescription;
    EditText etPicture;
    EditText etLink;
    EditText etPlace;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    DBHelper dbHelper;

    public String latitude;
    public String longitude;
    public String placeID;
    public String placeName;

    StringBuilder lat;
    StringBuilder longit;
    public String latLang;

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
        btnPlace = (Button) findViewById(R.id.btnPlace);
        etMessage = (EditText) findViewById(R.id.etMessage);
        etName = (EditText) findViewById(R.id.etName);
        etCaption = (EditText) findViewById(R.id.etCaption);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etPicture = (EditText) findViewById(R.id.etPicture);
        etLink = (EditText) findViewById(R.id.etLink);
        etPlace = (EditText) findViewById(R.id.etPlace);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPlace();
            }
        });

        dbHelper = new DBHelper(this);

    }

    public void GetPlace(){
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/search",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try{
                            JSONArray jArray = response.getJSONObject().getJSONArray("data");
                            if(jArray.getJSONObject(0).has("id")){
                                placeID = jArray.getJSONObject(0).get("id").toString();
                            }
                            if(jArray.getJSONObject(0).has("name")){
                                placeName = jArray.getJSONObject(0).get("name").toString();
                                etPlace.setText(placeName);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

        lat = new StringBuilder(latitude);
        lat.append(',');
        longit = new StringBuilder(longitude);
        lat.append(longit);
        latLang = lat.toString();

        String center = "";
        center = latLang;
        Bundle parameters = new Bundle();
        parameters.putString("type", "place");
        parameters.putString("center", center);
        parameters.putString("distance", "300");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void Submit(){
        Bundle params = new Bundle();
        if (etMessage.getText().length() != 0) {params.putString("message", etMessage.getText().toString());}
        if (etName.getText().length() != 0) {params.putString("name", etName.getText().toString());}
        if (etCaption.getText().length() != 0) {params.putString("caption", etCaption.getText().toString());}
        if (etDescription.getText().length() != 0) {params.putString("description", etDescription.getText().toString());}
        if (etPicture.getText().length() != 0) {params.putString("picture", etPicture.getText().toString());}
        if (etLink.getText().length() != 0) {params.putString("link", etLink.getText().toString());}
        params.putString("place", placeID);

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
                                ContentValues cv = new ContentValues();
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                if (etMessage.getText().length() != 0){
                                    cv.put("message", etMessage.getText().toString());
                                    cv.put("latitude", mLastLocation.getLatitude());
                                    cv.put("longitude", mLastLocation.getLongitude());
                                }
                                long rowID = db.insert("mytable", null, cv);
                                dbHelper.close();
                            }
                        }
                        Intent setMarker = new Intent(PublishActivity.this, MapsActivity.class);
                        startActivity(setMarker);
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
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(PublishActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(PublishActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                latitude = String.valueOf(mLastLocation.getLatitude());
                longitude = String.valueOf(mLastLocation.getLongitude());
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
