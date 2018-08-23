package io.ssenbabies.findawaypoint.views;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.ssenbabies.findawaypoint.R;

/* Use this to get SHA1 Key using CMD in Windows
 * keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
 */

public class RoomActivity extends AppCompatActivity implements OnMapReadyCallback {

    float lat, lng; // 마커의 위도 경도
    Marker mark; // 시작 마커
    MarkerOptions markerOptions_start; // 시작 마커 설정값
    GoogleMap map; // 구글 맵
    private TextView tvPlaceDetails;
    private FloatingActionButton fabPickPlace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        try{
            lat = intent.getExtras().getFloat("MyLat");
            lng = intent.getExtras().getFloat("MyLng");
        }catch(Exception e){
            e.printStackTrace();
        }

        setLayout();

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map_choice);

        mapFragment.getMapAsync(this);
    }


    private void setLayout() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        LatLng My = new LatLng(lat, lng);
        String my_address = MapActivity.getAddress(this, lat, lng);

        //나의 위치
        MarkerOptions markerOptions_my = new MarkerOptions();
        markerOptions_my.position(My);
        markerOptions_my.title("나의 위치");
        // markerOptions_my.draggable(true); // 드래그 할 수 있게함
        markerOptions_my.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.now)));
        markerOptions_my.snippet(my_address);

        map.addMarker(markerOptions_my);
        map.moveCamera(CameraUpdateFactory.newLatLng(My));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}