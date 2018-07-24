package io.ssenbabies.findawaypoint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
public class MainActivity extends AppCompatActivity {

    private Button btnAddRoom;
    String Tag = "Android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayout();
    }

    private void setLayout(){
        btnAddRoom = (Button) findViewById(R.id.btnAddRoom);
        setListener();
    }

    private void setListener(){
        Log.d("Android", "haha");
        btnAddRoom.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RoomActivity.class));
            }
        });
    }
}
