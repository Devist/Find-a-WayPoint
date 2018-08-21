package io.ssenbabies.findawaypoint.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import io.ssenbabies.findawaypoint.R;

public class SplashActivity extends AppCompatActivity {

    // 앱이 최초 실행인지 확인하기 위한 변수
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        //SharedPreferences의 초기값을 세팅합니다.
        prefs = getSharedPreferences("Pref", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(checkFirstRun())             //앱이 최초 실행인지 확인
                    goNamePage();
                else                            //최초 실행 아닐 경우 [init() 초기세팅] => [setLayout() 화면세팅] => [setListener() 이벤트처리]
                    goMainPage();

            }
        }, 1500);
    }

    private void goMainPage(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void goNamePage(){
        startActivity(new Intent(getApplicationContext(), NameActivity.class));
        finish();
    }

    //앱이 최초 실행인지 검출하여, 최초 페이지로 이동시키거나 메인 페이지에 머무릅니다.
    private boolean checkFirstRun(){
        prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        return prefs.getBoolean("isFirstRun",true);
    }

}
