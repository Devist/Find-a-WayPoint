package io.ssenbabies.findawaypoint.views;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONObject;

import io.ssenbabies.findawaypoint.R;
import io.ssenbabies.findawaypoint.databases.DBHelper;
import io.ssenbabies.findawaypoint.network.WaySocket;

public class MyLocationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private ImageButton btnCancel;
    private EditText editMyLocation;

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;

    private String currentRoomCode, latitude, longitude, userName;

    private SharedPreferences prefs;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylocation);

        init();
        setLayout();
    }

    private void init(){

        dbHelper = new DBHelper(getApplicationContext(), "MyInfo.db", null, 1);

        prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        currentRoomCode = getIntent().getStringExtra("roomCode");
        userName = prefs.getString("name","anonymous");

        Log.d("테스트_룸코드",currentRoomCode + "a");
        WaySocket.getInstance().requestEntrance(currentRoomCode, userName);

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
        editMyLocation.setFocusable(false);
        setListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MyLocationActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        WaySocket.getInstance().setWaySocketListener(new WaySocket.WaySocketListener() {
            @Override public void onCreateResultReceived(JSONObject result) { }
            @Override public void onPickResultReceived(JSONObject result) { }
            @Override public void onRoomListReceived(JSONObject result) { }
            @Override public void onCompleteResultReceived(JSONObject reulst) { }
            @Override public void onConnectionEventReceived() { }
            @Override public void onReloadEventReceived(JSONObject result) { }
            @Override
            public void onEntranceEventReceived(JSONObject result) {
                Log.d("테스트 방 입장 : ","성공");
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
                Log.d("테스트","진입");
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placeName = String.format("%s", place.getName());
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;
                WaySocket.getInstance().requestPick(currentRoomCode, latitude,longitude,"");

                //임시 코드. 나중에 onPickResultReceived 로 옮겨야함
                dbHelper.updatePickStateToDone(currentRoomCode);
                //임시코드
                Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                intent.putExtra("place", placeName);
                intent.putExtra("room_code",currentRoomCode);
                intent.putExtra("lat",latitude);
                intent.putExtra("lng",longitude);
                Log.d("테스트",Double.toString(latitude));
                Log.d("테스트",Double.toString(longitude));

                startActivity(intent);
                finish();
            }
        }
    }
}
