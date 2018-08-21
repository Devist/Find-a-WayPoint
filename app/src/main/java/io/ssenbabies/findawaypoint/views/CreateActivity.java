package io.ssenbabies.findawaypoint.views;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.ssenbabies.findawaypoint.R;
import io.ssenbabies.findawaypoint.network.WaySocket;

public class CreateActivity extends AppCompatActivity {

    private TextView btnCancel;
    private Button btnShare;
    private EditText editAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setLayout();
    }

    private void setLayout(){
        Toolbar cancelToolbar = (Toolbar) findViewById(R.id.toolbarCancle);
        setSupportActionBar(cancelToolbar);

        btnCancel = (TextView)findViewById(R.id.btnCancle);
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

        editAppointment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //final int DRAWABLE_LEFT = 0;
                //final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                //final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getX() >= (editAppointment.getRight() - editAppointment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Log.d("소켓","방 생성 시도");
                        WaySocket.getInstance().requestCreateRoom(editAppointment.getText().toString(),"");

                        return true;
                    }
                }
                return false;
            }
        });

        editAppointment.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void afterTextChanged(Editable editable) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editAppointment.getText().length()>2){
                    btnShare.setVisibility(View.VISIBLE);
                }else{
                    btnShare.setVisibility(View.GONE);
                }
            }
        });

//        btnCreateName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(editAppointmentName.getText().length()>2) {
//
//                    Intent intent = new Intent(getApplication(), ShareActivity.class);
//                    intent.putExtra("room_name", editAppointmentName.getText().toString());
//                    startActivity(intent);
//                }
//                else
//                    Snackbar.make(view, "먼저 상단의 모임 이름을 입력해 주세요!", Snackbar.LENGTH_LONG).show();
//            }
//        });
//
//        editAppointmentName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus)
//                    hideKeyboard(v);
//            }
//        });
//
//        editAppointmentName.addTextChangedListener(new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//            @Override public void afterTextChanged(Editable editable) { }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (editAppointmentName.getText().length()>2){
//                    btnCreateName.setBackgroundColor(Color.DKGRAY);
//                }else{
//                    btnCreateName.setBackgroundColor(Color.LTGRAY);
//                }
//            }
//        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
