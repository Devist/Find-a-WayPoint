package io.ssenbabies.findawaypoint.pages;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import io.ssenbabies.findawaypoint.R;

public class ShareActivity extends AppCompatActivity {

    private TextView btnCancle;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        setLayout();
    }

    private void setLayout(){

        Toolbar cancelToolbar = (Toolbar) findViewById(R.id.toolbarCancle);
        setSupportActionBar(cancelToolbar);

        btnCancle = (TextView) findViewById(R.id.btnCancle);
        btnBack = (Button) findViewById(R.id.btnBack);

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
}
