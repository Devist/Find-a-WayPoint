package io.ssenbabies.findawaypoint.views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import io.ssenbabies.findawaypoint.R;

public class NameActivity extends AppCompatActivity {

    private EditText editName;
    private Button btnCreateName;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_name);

        btnCreateName = (Button)findViewById(R.id.btn_create_name);
        editName = (EditText)findViewById(R.id.edit_name);

        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    hideKeyboard(v);
            }
        });

        editName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void afterTextChanged(Editable editable) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editName.getText().length()>1){
                    btnCreateName.setBackgroundColor(Color.DKGRAY);
                }else{
                    btnCreateName.setBackgroundColor(Color.LTGRAY);
                }
            }
        });

        btnCreateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("진입","진입");

                String name = editName.getText().toString();
                if(name.length()>1) {
                    prefs = getSharedPreferences("Pref", MODE_PRIVATE);
                    prefs.edit().putBoolean("isFirstRun",false).apply();        //앱 최초 실행 값을 false로 변경하여, 다음 접속시 바로 MainActivity 가 열리도록 함.
                    prefs.edit().putString("name",name).apply();        //앱 최초 실행 값을 false로 변경하여, 다음 접속시 바로 MainActivity 가 열리도록 함.


                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else{
                    Snackbar.make(v, "두 글자 이상의 이름을 입력해 주세요.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
