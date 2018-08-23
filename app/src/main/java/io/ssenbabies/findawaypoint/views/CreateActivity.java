package io.ssenbabies.findawaypoint.views;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.ssenbabies.findawaypoint.R;
import io.ssenbabies.findawaypoint.network.WaySocket;

public class CreateActivity extends AppCompatActivity {

    private ImageButton btnCancel;
    private Button btnShare;
    private EditText editAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        WaySocket.currentView = getCurrentFocus();
        setLayout();
    }

    private void setLayout(){
        Toolbar cancelToolbar = (Toolbar) findViewById(R.id.toolbarCancle);
        setSupportActionBar(cancelToolbar);

        btnCancel = (ImageButton)findViewById(R.id.btnCancle);
        editAppointment = (EditText)findViewById(R.id.edit_appointment);
        btnShare = (Button)findViewById(R.id.btn_share);

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

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                if(editAppointment.isEnabled()){
                    editAppointment.setEnabled(false);
                    editAppointment.setFocusable(false);
                    btnShare.setText("약속하러 가기");
                }else{
                    startActivity(new Intent(getApplicationContext(),MyLocationActivity.class));
                }
                */

                //공유하는 코드(라인, 카카오, 문자)
                String subject = "방코드";
                String text = "abcde@@!@#";

                List targetedShareIntents = new ArrayList<>();

                // 카카오톡
                Intent kakaoIntent = getShareIntent("com.kakao.talk", subject, text);
                if(kakaoIntent != null)
                    targetedShareIntents.add(kakaoIntent);

                //라인
                Intent lineIntent = getShareIntent("jp.naver.line", subject, text);
                if(lineIntent != null)
                    targetedShareIntents.add(lineIntent);

                //문자

                Intent snsIntent = new Intent(Intent.ACTION_VIEW);
                snsIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                snsIntent.putExtra(Intent.EXTRA_TEXT, text);
                //  snsIntent.putExtra("subject", "방코드");
                //  snsIntent.putExtra("text", "asd123$@");
                snsIntent.setType("vnd.android-dir/mms-sms");

                if(snsIntent != null)
                    targetedShareIntents.add(snsIntent);

                Intent chooser = Intent.createChooser((Intent) targetedShareIntents.remove(0), "방코드 공유하기");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooser);

            }
        });

        //아이콘 클릭 시 방 생성 시도
        editAppointment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //final int DRAWABLE_LEFT = 0;
                //final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                //final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editAppointment.getRight() - editAppointment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        WaySocket.getInstance().requestCreateRoom(editAppointment.getText().toString(),"");
                        return true;
                    }
                }
                return false;
            }
        });

        //방 생성 결과에 대한 리스너
        WaySocket.getInstance().setWaySocketListener(new WaySocket.WaySocketListener() {
            @Override
            public void onCreateResultReceived(View v, JSONObject result) {
                hideKeyboard(getCurrentFocus());

                try{
                    int status = result.getInt("status");
                    if(status==WaySocket.SUCCESS){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(btnShare, "생성 완료! 공유하고 약속 장소를 정하러 가요", Snackbar.LENGTH_SHORT).show();
                                btnShare.setVisibility(View.VISIBLE);
                            }
                        });

                    }else if(status==WaySocket.SUCCESS){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(btnShare, "생성 실패! 서버 개발자가 잘못했습니다", Snackbar.LENGTH_SHORT).show();
                            }
                        });

                    }

                }catch(Exception e){
                    Snackbar.make(btnShare, "통신에 실패했습니다.", Snackbar.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    //방정보 공유를 위한 함수
    private Intent getShareIntent(String name, String subject, String text) {
        boolean found = false;

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(intent, 0);

        if(resInfos == null || resInfos.size() == 0)
            return null;

        for (ResolveInfo info : resInfos) {
            if (info.activityInfo.packageName.toLowerCase().contains(name) ||
                    info.activityInfo.name.toLowerCase().contains(name) ) {
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setPackage(info.activityInfo.packageName);
                found = true;
                break;
            }
        }

        if (found)
            return intent;

        return null;
    }
}
