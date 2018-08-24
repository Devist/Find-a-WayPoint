package io.ssenbabies.findawaypoint.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private Button start_go;// 테스트를 위한 버튼(출발지 설정)

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
        try{
            dbHelper.insertSampleRoom();
        }catch (Exception e){

        }

    }

    private void setLayout(){
        List<Room> rooms = dbHelper.getAppointments();

        go = (Button) findViewById(R.id.btn_go);    //테스트용 버튼
        start_go = (Button) findViewById(R.id.btn_start); //테스트용 버튼

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
        setLayout();
        setListener(); // Edittext 내 코드 찾기 버튼이 다시 눌리지 않아 적용
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener(){

        go.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(v.getId()) {
                    case R.id.btn_go: //지도 테스트
                        Intent intent = new Intent(MainActivity.this, ShareActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        //출발지 팝업
        start_go.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(v.getId()) {
                    case R.id.btn_start:

                        LayoutInflater dialog = LayoutInflater.from(MainActivity.this);
                        final View dialogLayout = dialog.inflate(R.layout.item_dialog, null);
                        final Dialog myDialog = new Dialog(MainActivity.this);

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
                            {     //OK 누르면 할거
                                Toast.makeText(MainActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                                myDialog.cancel();
                            }
                        });

                        btn_cancel.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Toast.makeText(MainActivity.this, "NO", Toast.LENGTH_SHORT).show();
                                myDialog.cancel();
                            }
                        });
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
                //final int DRAWABLE_BOTTOM = line_3;

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
            @Override public void onPickResultReceived(JSONObject result) { }
            @Override public void onRoomListReceived(JSONObject result) { }
            @Override public void onCompleteResultReceived(JSONObject reulst) { }
            @Override public void onConnectionEventReceived() {
                Log.d("테스트","소켓 커넥트 ");
            }
            @Override public void onEntranceEventReceived(JSONObject result) { }

            /*방 코드 검색을 하고 나면, 결과값이 다음 코드로 들어온다.*/
            @Override
            public void onReloadEventReceived(JSONObject result) {
                Log.d("테스트", "응답값");
                try{
                    JSONObject data =(JSONObject)result.get("room_info");
                    Log.d("테스트", data.getString("room_name"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this,RoomActivity.class);
                            intent.putExtra("place","가산유미어스오피스텔");
                            intent.putExtra("lat", 37.481724);
                            intent.putExtra("lng", 126.8749569);
                            intent.putExtra("room_code", editCode.getText().toString());
                            startActivity(intent);
                        }
                    });
//                    if(status==WaySocket.SUCCESS){
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent intent = new Intent(MainActivity.this,MyLocationActivity.class);
//                                intent.putExtra("room_code",editCode.getText().toString());
//                                startActivity(intent);
//                            }
//                        });
//
//                    }else if(status==WaySocket.FAIL){
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Snackbar.make(btnAddRoom, "존재하지 않는 코드인가 봐요", Snackbar.LENGTH_LONG).show();
//                            }
//                        });
//                    //}

                }catch(Exception e){
                    e.printStackTrace();
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
