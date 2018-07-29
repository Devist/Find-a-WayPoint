package io.ssenbabies.findawaypoint.pages;


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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.ssenbabies.findawaypoint.R;

public class CreateActivity extends AppCompatActivity {

    private TextView btnCancel;
    private Button btnCreateName;
    private EditText editAppointmentName;

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
        editAppointmentName = (EditText)findViewById(R.id.editAppointmentName);
        btnCreateName = (Button)findViewById(R.id.btnCreateName);
        btnCreateName.setEnabled(true);

        setListener();
    }

    private void setListener(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCreateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editAppointmentName.getText().length()>2)
                    startActivity(new Intent(getApplicationContext(), ShareActivity.class));
                else
                    Snackbar.make(view, "먼저 상단의 모임 이름을 입력해 주세요!", Snackbar.LENGTH_LONG).show();
            }
        });

        editAppointmentName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    hideKeyboard(v);
            }
        });

        editAppointmentName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void afterTextChanged(Editable editable) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editAppointmentName.getText().length()>2){
                    btnCreateName.setBackgroundColor(Color.DKGRAY);
                }else{
                    btnCreateName.setBackgroundColor(Color.LTGRAY);
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
