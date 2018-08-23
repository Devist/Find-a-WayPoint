package io.ssenbabies.findawaypoint.views.adapters;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import io.ssenbabies.findawaypoint.R;

/**
 * Created by xowns on 2018-08-09.
 */

public class GooglePlaceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<GooglePlace> placesArrayList;
    private Context context;

    public GooglePlaceAdapter(Context context, ArrayList<GooglePlace> placesArrayList) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.placesArrayList = placesArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.placesArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return placesArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            //  LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_place, viewGroup, false);
        }

        //화면에 표시될 View(Layout이 inflate된) 으로 부터 위젝에 대한 참조 획득
        final TextView tv_name = (TextView) view.findViewById(R.id.name);
        TextView tv_addr = (TextView) view.findViewById(R.id.addr);
        TextView tv_type = (TextView) view.findViewById(R.id.type);
        Button btn_search = (Button) view.findViewById(R.id.btn_search);

        // Data Set에서 position에 위치한 데이터 참조 획득
        GooglePlace place = placesArrayList.get(i);

        //아이템 내 각 위젝에 데이터 반영
        tv_name.setText(place.getName());
        tv_addr.setText(place.getAddr());
        tv_type.setText(place.getType());

        btn_search.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btn_search:
                        Intent intent = new Intent();
                        intent.setAction(intent.ACTION_WEB_SEARCH);
                        intent.putExtra(SearchManager.QUERY, tv_name.getText());
                        context.startActivity(intent);
                        break;
                }
            }
        });

        return view;
    }
}
