package com.example.vladzakharo.facebookapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CentralActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    Button btnPublish;
    Button btnUpdate;
    Button btnMap;
    RecyclerView recyclerView;
    List<Post> postList = new ArrayList<>();
    PostsAdapter mAdapter;
    public String Message;
    public String Name;
    public String Caption;
    public String Description;
    public String Picture;
    public String Link;
    public String Id;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_central);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));
        btnPublish = (Button) findViewById(R.id.btnPublish);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnMap = (Button) findViewById(R.id.btnMap);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Publish();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList();
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(CentralActivity.this, MapsActivity.class);
                startActivity(mapIntent);
            }
        });

        getFeed();

    }

    private void updateList(){
        getFeed();
    }

    private void Publish(){
        Intent goPublish = new Intent(CentralActivity.this, PublishActivity.class);
        startActivity(goPublish);
    }

    protected void getFeed(){
        Bundle params = new Bundle();
        params.putString("fields", "message, name, caption, description, picture, link, id");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response){
                        postList.clear();
                        try{
                            JSONArray jArray = response.getJSONObject().getJSONArray("data");
                            for(int i = 0; i < jArray.length(); i++){
                                Message = "";
                                Name = "";
                                Caption = "";
                                Description = "";
                                Picture = "";
                                Link = "";
                                Id = "";

                                if (jArray.getJSONObject(i).has("message")){Message = jArray.getJSONObject(i).get("message").toString();}
                                if (jArray.getJSONObject(i).has("name")){Name = jArray.getJSONObject(i).get("name").toString();}
                                if (jArray.getJSONObject(i).has("caption")){Caption = jArray.getJSONObject(i).get("caption").toString();}
                                if (jArray.getJSONObject(i).has("description")){Description = jArray.getJSONObject(i).get("description").toString();}
                                if (jArray.getJSONObject(i).has("picture")){Picture = jArray.getJSONObject(i).get("picture").toString();}
                                if (jArray.getJSONObject(i).has("link")) {Link = jArray.getJSONObject(i).get("link").toString();}
                                if (jArray.getJSONObject(i).has("id")){Id = jArray.getJSONObject(i).get("id").toString();}

                                Post post = new Post (Message, Name, Caption, Description, Picture, Link, Id);
                                postList.add(post);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mAdapter = new PostsAdapter(postList);
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(llm);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new DividerItemDecoration(CentralActivity.this, LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null){
                Toast.makeText(getApplicationContext(), "Please, enable GPS", Toast.LENGTH_SHORT).show();
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
