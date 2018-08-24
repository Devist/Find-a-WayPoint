package io.ssenbabies.findawaypoint.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import org.json.JSONObject;

import java.util.List;

import io.ssenbabies.findawaypoint.R;
import io.ssenbabies.findawaypoint.network.WaySocket;
import io.ssenbabies.findawaypoint.views.adapters.Room;
import io.ssenbabies.findawaypoint.views.adapters.RoomAdapter;
import io.ssenbabies.findawaypoint.databases.DBHelper;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton btnAddRoom;
    private RecyclerView recyclerRoomView;
    private Dialog findDialog;
    private EditText editCode;

    private DBHelper dbHelper;

    private Button go;// 테스트를 위한 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSetting();
        setLayout();
        setListener();
    }

    private void initSetting(){
        dbHelper = new DBHelper(getApplicationContext(), "MyInfo.db", null, 1);
        requestPermission();
        WaySocket.getInstance();
        //dbHelper.insertSampleRoom();
    }

    private void setLayout(){
        List<Room> rooms = dbHelper.getAppointments();

        go = (Button) findViewById(R.id.btn_go);    //테스트용 버튼

        btnAddRoom = (FloatingActionButton) findViewById(R.id.btnAddRoom);
        editCode = (EditText) findViewById(R.id.edit_code);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerRoomView = (RecyclerView) findViewById(R.id.recyclerRoomView);
        recyclerRoomView.setHasFixedSize(true);
        recyclerRoomView.setLayoutManager(layoutManager);
        recyclerRoomView.setAdapter(new RoomAdapter(getApplicationContext(), rooms, R.layout.activity_main));
    }

    @Override
    public void onResume(){
        super.onResume();

        setListener(); // Edittext 내 코드 찾기 버튼이 다시 눌리지 않아 적용
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener(){

        go.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(v.getId()) {
                    case R.id.btn_go:
                        Intent intent = new Intent(MainActivity.this, ShareActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

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
                        // 아래의 setWaySocketListener의 onReloadEventReceived 로 결과값 들어옴
                        WaySocket.getInstance().requestReloadRoom(editCode.getText().toString());
                        return false;
                    }
                }
                return false;
            }
        });

        WaySocket.getInstance().setWaySocketListener(new WaySocket.WaySocketListener() {

            @Override public void onCreateResultReceived(JSONObject result) { }
            @Override public void onPickEventReceived(JSONObject result) { }
            @Override public void onConnectionEventReceived() { }
            @Override public void onEntranceEventReceived(JSONObject result) { }

            /*방 코드 검색을 하고 나면, 결과값이 다음 코드로 들어온다.*/
            @Override
            public void onReloadEventReceived(JSONObject result) {
                try{
                    int status = result.getInt("status");
                    Log.d("테스트", result.getString("msg"));

                    if(status==WaySocket.SUCCESS){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this,MyLocationActivity.class);
                                intent.putExtra("room_code",editCode.getText().toString());
                                startActivity(intent);
                            }
                        });

                    }else if(status==WaySocket.FAIL){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(btnAddRoom, "존재하지 않는 코드인가 봐요", Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }

                }catch(Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(btnAddRoom, "통신에 실패했습니다.", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void requestPermission(){
        //위치 권한 다이얼로그
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 위치 정보 접근 요청
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
    }

}
