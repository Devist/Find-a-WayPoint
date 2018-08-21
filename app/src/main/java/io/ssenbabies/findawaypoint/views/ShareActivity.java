package io.ssenbabies.findawaypoint.views;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import io.ssenbabies.findawaypoint.R;
import io.ssenbabies.findawaypoint.utils.Location;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView btnCancle, roomName;
    private Button btnBack;
    private Button shareKakao, shareSns, shareCopy, choice_start, goMap;
    String room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        room = intent.getStringExtra("room_name");

        setLayout();

        shareKakao.setOnClickListener(this);
        shareSns.setOnClickListener(this);
        shareCopy.setOnClickListener(this);
        choice_start.setOnClickListener(this);
        goMap.setOnClickListener(this);
    }

    private void setLayout(){

        Toolbar cancelToolbar = (Toolbar) findViewById(R.id.toolbarCancle);
        setSupportActionBar(cancelToolbar);

        btnCancle = (TextView) findViewById(R.id.btnCancle);
        btnBack = (Button) findViewById(R.id.btnBack);
        roomName = (TextView) findViewById(R.id.tv_room_name);
        shareKakao = (Button) findViewById(R.id.btn_kakao);
        shareSns = (Button) findViewById(R.id.btn_sns);
        shareCopy = (Button) findViewById(R.id.btn_copy);
        choice_start = (Button) findViewById(R.id.btn_start);
        goMap = (Button) findViewById(R.id.btn_map);

        roomName.setText(room);
        setListener();
    }

    private void setListener(){

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShareActivity.this, MainActivity.class));
                finishAffinity();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_kakao:
                Toast.makeText(this, "KAKAO 공유", Toast.LENGTH_LONG).show();
                TextTemplate params = TextTemplate.newBuilder("중간지점 찾기 방코드", LinkObject.newBuilder().setWebUrl("https://www.naver.com/").setMobileWebUrl("https://www.naver.com/").build()).setButtonTitle("중간지점 찾기").build();

                KakaoLinkService.getInstance().sendDefault(getApplicationContext(), params, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.d("flag", errorResult.toString());
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                        Log.d("flag", result.toString());

                    }
                });
                break;

            case R.id.btn_sns:
                Toast.makeText(this, "sns 공유", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 0);
                break;

            case R.id.btn_copy:
                Toast.makeText(this, "링크 복사", Toast.LENGTH_LONG).show();
                break;

            case R.id.btn_start:
                Location.requestSingleUpdate(this.getApplicationContext(),
                        new Location.LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(Location.GPSCoordinates location) {

                                float lat = location.latitude;
                                float lng = location.longitude;

                                Toast.makeText(ShareActivity.this, "출발지 지정", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ShareActivity.this, RoomActivity.class);

                                intent.putExtra("MyLat", lat);
                                intent.putExtra("MyLng", lng);
                                startActivity(intent);
                            }
                        });
                break;

            case R.id.btn_map:

                Location.requestSingleUpdate(this.getApplicationContext(),
                        new Location.LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(Location.GPSCoordinates location) {

                                float lat = location.latitude;
                                float lng = location.longitude;

                                Intent intent = new Intent(getApplication(), MapActivity.class);

                                intent.putExtra("MyLat", lat);
                                intent.putExtra("MyLng", lng);
                                intent.putExtra("room_name", room);
                                startActivity(intent);
                            }
                        });
                break;
        }
    }

    //sns 보내기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK)
        {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String name = cursor.getString(0);        //0은 이름을 얻어옵니다.
            String number = cursor.getString(1);   //1은 번호를 받아옵니다.

            //특정 사람에게 보내기
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            String smsBody = "삼겹살 구우셈";
            sendIntent.putExtra("sms_body", smsBody); // 보낼 문자
            sendIntent.putExtra("address", number); // 받는사람 번호
            sendIntent.setType("vnd.android-dir/mms-sms");
            startActivity(sendIntent);

            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
