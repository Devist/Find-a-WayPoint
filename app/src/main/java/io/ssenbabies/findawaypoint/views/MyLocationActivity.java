package io.ssenbabies.findawaypoint.views;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.ssenbabies.findawaypoint.R;
import io.ssenbabies.findawaypoint.network.WaySocket;

public class MyLocationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private ImageButton btnCancel;
    private EditText editMyLocation;

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylocation);

        init();
        setLayout();
    }

    private void init(){
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    private void setLayout(){
        Toolbar cancelToolbar = (Toolbar) findViewById(R.id.toolbarCancle);
        setSupportActionBar(cancelToolbar);

        btnCancel = (ImageButton)findViewById(R.id.btnCancle);
        editMyLocation = (EditText)findViewById(R.id.edit_mylocation);

        setListener();
    }

    private void setListener(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editMyLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MyLocationActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(getCurrentFocus(), connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;
                String address = String.format("%s", place.getAddress());
                String phoneNumber = String.format("%s", place.getPhoneNumber());
                String webSite = String.format("%s", place.getWebsiteUri());
                String Star = String.format("%s", place.getRating());
                String price = String.format("%s", place.getPriceLevel());
                String type = String.format("%s", place.getPlaceTypes());

                LatLng latLng = new LatLng(latitude, longitude);
            }
        }
    }
}
