package io.ssenbabies.findawaypoint.pages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.ssenbabies.findawaypoint.R;
import io.ssenbabies.findawaypoint.Room;
import io.ssenbabies.findawaypoint.RoomAdapter;

public class MainActivity extends AppCompatActivity {

    private Button btnAddRoom;
    private RecyclerView recyclerRoomView;
    String Tag = "Android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //위치 권한 다이얼로그
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 위치 정보 접근 요청
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

        setLayout();
    }

    private void setLayout(){
        btnAddRoom = (Button) findViewById(R.id.btnAddRoom);

        recyclerRoomView = (RecyclerView) findViewById(R.id.recyclerRoomView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerRoomView.setHasFixedSize(true);
        recyclerRoomView.setLayoutManager(layoutManager);

        List<Room> rooms = new ArrayList<>();
        Room[] room = new Room[5];
        room[0] = new Room("취업스터디모임","서울 강남구 역삼동","강남역,역삼역,신논현역");
        room[1] = new Room("넥스터즈 샌애기팀","서울 강남구 역삼동","강남역,역삼역,신논현역");
        room[2] = new Room("넥스터즈 샌애기팀","서울 강남구 역삼동","강남역,역삼역,신논현역");
        room[3] = new Room("넥스터즈 샌애기팀","서울 강남구 역삼동","강남역,역삼역,신논현역");
        room[4] = new Room("넥스터즈 샌애기팀","서울 강남구 역삼동","강남역,역삼역,신논현역");

        for (int i = 0; i < 5; i++) {
            rooms.add(room[i]);
        }

        recyclerRoomView.setAdapter(new RoomAdapter(getApplicationContext(), rooms, R.layout.activity_main));


        setListener();

    }

    private void setListener(){
        Log.d("Android", "haha");
        btnAddRoom.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateActivity.class));
            }
        });
    }


}
