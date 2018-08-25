package io.ssenbabies.findawaypoint.views;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.ssenbabies.findawaypoint.databases.DBHelper;
import io.ssenbabies.findawaypoint.network.WaySocket;
import io.ssenbabies.findawaypoint.utils.Location;
import io.ssenbabies.findawaypoint.views.adapters.Friend;
import io.ssenbabies.findawaypoint.views.adapters.FriendAdapter;
import io.ssenbabies.findawaypoint.R;
import io.ssenbabies.findawaypoint.views.adapters.Room;
import io.ssenbabies.findawaypoint.views.adapters.RoomAdapter;

/* Use this to get SHA1 Key using CMD in Windows
 * keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
 */

public class RoomActivity extends AppCompatActivity implements OnMapReadyCallback {

    private double lat, lng; // 마커의 위도 경도

    Marker mark; // 시작 마커
    MarkerOptions markerOptions_start; // 시작 마커 설정값
    GoogleMap map; // 구글 맵
    private TextView tvPlaceDetails;
    private FloatingActionButton fabPickPlace;
    private String currentRoomCode;

    private LinearLayoutManager mFriendsTopViewManager;
    private RecyclerView mFriendsView;

    private EditText myPlace;

    private Button goPlace;

    private DBHelper dbHelper;
    private String[] myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        currentRoomCode = intent.getExtras().getString("room_code");
        try{
            lat = intent.getExtras().getDouble("lat");
            lng = intent.getExtras().getDouble("lng");
        }catch(Exception e){
            e.printStackTrace();
        }
        dbHelper = new DBHelper(getApplicationContext(), "MyInfo.db", null, 1);

        setLayout();
        setListener();
    }


    private void setLayout() {
        myPlace = (EditText) findViewById(R.id.my_appointment);
        myPlace.setText(getIntent().getStringExtra("place"));
        mFriendsView = (RecyclerView) findViewById(R.id.friendView);

        // init LayoutManager
        mFriendsTopViewManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);

        mFriendsView.setLayoutManager(mFriendsTopViewManager);
        mFriendsView.setHasFixedSize(true);

        myPlace = (EditText) findViewById(R.id.my_appointment);
        goPlace = (Button)findViewById(R.id.btn_go_place);

        Friend[] friend = new Friend[4];
        myList = new String[]{"오동환","김태준","이산하","성락원"};
        friend[0] = new Friend(1,"오동환");
        friend[1] = new Friend(0,"김태준");
        friend[2] = new Friend(1,"이산하");
        friend[3] = new Friend(0,"성락원");


        List<Friend> friends = new ArrayList<>();

        friends.add(friend[0]);
        friends.add(friend[1]);
        friends.add(friend[2]);
        friends.add(friend[3]);

        mFriendsView.setAdapter(new FriendAdapter(getApplicationContext(), friends, R.layout.activity_room));

        //지도 레이아웃 설정
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map_choice);
        mapFragment.getMapAsync(this);
    }

    private void setListener(){
        goPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(v.getId()) {
                    case R.id.btn_go_place:

                        LayoutInflater dialog = LayoutInflater.from(RoomActivity.this);
                        final View dialogLayout = dialog.inflate(R.layout.item_dialog, null);
                        final Dialog myDialog = new Dialog(RoomActivity.this);

                        myDialog.setTitle("알림");
                        myDialog.setContentView(dialogLayout);
                        myDialog.show();

                        String result, result2, result3;
                        result = "     나를 포함하여" + "\n";
                        int count = 3; // 방에 참여한 사람 수

                        result2 = "총 " + String.valueOf(count) + "명의 약속장소를" + "\n";
                        result3 = "  추천 받으시겠어요?";

                        result += result2;
                        result += result3;

                        TextView tv_result = (TextView) dialogLayout.findViewById(R.id.tv_text);
                        tv_result.setText(result);

                        Button btn_ok = (Button)dialogLayout.findViewById(R.id.btn_ok);
                        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.btn_cancel);

                        btn_ok.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {     //OK 누르면 할거 -> 장소 추천화면으로 이동

                                dbHelper.updateFriends(currentRoomCode,myList);
                                Toast.makeText(RoomActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                                myDialog.cancel();

                                Location.requestSingleUpdate(RoomActivity.this,
                                        new Location.LocationCallback() {
                                            @Override
                                            public void onNewLocationAvailable(Location.GPSCoordinates location) {

                                                float lat = location.latitude;
                                                float lng = location.longitude;

                                                Intent intent = new Intent(getApplication(), MapActivity.class);
                                                intent.putExtra("room_code",currentRoomCode);
                                                intent.putExtra("MidLat", lat);
                                                intent.putExtra("MidLng", lng);
                                           //     intent.putExtra("room_name", room);
                                                startActivity(intent);
                                            }
                                        });
                            }
                        });

                        btn_cancel.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Toast.makeText(RoomActivity.this, "NO", Toast.LENGTH_SHORT).show();
                                myDialog.cancel();
                            }
                        });
                        break;
                }
            }
        });

        myPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.my_appointment:

                        //위치 입력
                        Toast.makeText(RoomActivity.this, "위치 수정", Toast.LENGTH_SHORT).show();
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        try {
                            startActivityForResult(builder.build(RoomActivity.this), 1);
                        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Log.d("테스트","진입");
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placeName = String.format("%s", place.getName());
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;
              //  WaySocket.getInstance().requestPick(currentRoomCode, latitude,longitude,"");

                //임시 코드. 나중에 onPickResultReceived 로 옮겨야함
             //   dbHelper.updatePickStateToDone(currentRoomCode);
                //임시코드
                Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                intent.putExtra("place", placeName);
                intent.putExtra("lat",latitude);
                intent.putExtra("lng",longitude);
                Log.d("테스트",Double.toString(latitude));
                Log.d("테스트",Double.toString(longitude));

                startActivity(intent);
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Test","들어왔나유");
        Log.d("Test",Double.toString(lat));
        Log.d("Test",Double.toString(lng));

        map = googleMap;

        LatLng My = new LatLng(lat, lng);
        String my_address = MapActivity.getAddress(this, lat, lng);

        //나의 위치
        MarkerOptions markerOptions_my = new MarkerOptions();
        markerOptions_my.position(My);
        markerOptions_my.title("나의 위치");
        markerOptions_my.draggable(true); // 드래그 할 수 있게함
        markerOptions_my.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.now)));
        markerOptions_my.snippet(my_address);

        map.addMarker(markerOptions_my);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(My,15));


    }
}