package io.ssenbabies.findawaypoint.views;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import io.ssenbabies.findawaypoint.R;
import io.ssenbabies.findawaypoint.network.WaySocket;
import io.ssenbabies.findawaypoint.views.adapters.Room;
import io.ssenbabies.findawaypoint.views.adapters.RoomAdapter;
import io.ssenbabies.findawaypoint.databases.DBHelper;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton btnAddRoom;
    private Button btnFindRoom;
    private Button btnFindAppointment;
    private RecyclerView recyclerRoomView;
    private Dialog findDialog;
    private EditText editCode;

    private DBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WaySocket.getInstance();

        //위치 권한 다이얼로그
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 위치 정보 접근 요청
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

        dbHelper = new DBHelper(getApplicationContext(), "MyInfo.db", null, 1);

        setLayout();
    }

    private void setLayout(){
        btnAddRoom = (FloatingActionButton) findViewById(R.id.btnAddRoom);
        editCode = (EditText) findViewById(R.id.edit_code);

        recyclerRoomView = (RecyclerView) findViewById(R.id.recyclerRoomView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerRoomView.setHasFixedSize(true);
        recyclerRoomView.setLayoutManager(layoutManager);


        //dbHelper.insertSampleRoom();

        List<Room> rooms = dbHelper.getAppointments();
        Room[] room = new Room[5];

        recyclerRoomView.setAdapter(new RoomAdapter(getApplicationContext(), rooms, R.layout.activity_main));


        setListener();

    }

    private void setListener(){
        btnAddRoom.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateActivity.class));
            }
        });

        editCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //final int DRAWABLE_LEFT = 0;
                //final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                //final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editCode.getRight() - editCode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {


                        return true;
                    }
                }
                return false;
            }
        });

//        btnFindRoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                findDialog = new Dialog(MainActivity.this);
//                findDialog.setContentView(R.layout.dialog_find);
//                findDialog.show();
//                final EditText tagOfRoom = (EditText)findDialog.findViewById(R.id.tag_of_room);
//                Button btnEnterRoom = (Button)findDialog.findViewById(R.id.btn_enter_room);
//                Button btnEnterCancle = (Button)findDialog.findViewById(R.id.btn_enter_cancle);
//
//                btnEnterCancle.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        findDialog.cancel();
//                    }
//                });
//
//                btnEnterRoom.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d("Test",tagOfRoom.getText().toString());
//                        startActivity(new Intent(MainActivity.this, RoomActivity.class));
//                    }
//                });
//
//            }
//        });
    }


}
