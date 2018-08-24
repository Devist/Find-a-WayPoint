package io.ssenbabies.findawaypoint.views.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import io.ssenbabies.findawaypoint.R;

public class DetailDialog extends Dialog {
    TextView tvTitle, tvDate, tvMember, tvPlace, tvStation;
    ImageButton btnClear;

    public DetailDialog(Context context,String title, String date, String member, String place, String station) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.dialog_detail_info);     //다이얼로그에서 사용할 레이아웃입니다.


        tvTitle = (TextView)findViewById(R.id.tv_title);
        tvDate = (TextView)findViewById(R.id.tv_date);
        tvMember = (TextView)findViewById(R.id.tv_members);
        tvPlace = (TextView)findViewById(R.id.tv_place);
        tvStation = (TextView)findViewById(R.id.tv_station);

        tvTitle.setText(title);
        tvDate.setText(date);
        tvMember.setText(member);
        tvPlace.setText(place);
        tvStation.setText(station);

        btnClear = (ImageButton) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();   //다이얼로그를 닫는 메소드입니다.
            }
        });

    }
}