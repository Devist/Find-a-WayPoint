package io.ssenbabies.findawaypoint.views;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.ssenbabies.findawaypoint.network.RetrofitService;
import io.ssenbabies.findawaypoint.views.adapters.Place;
import io.ssenbabies.findawaypoint.views.adapters.PlaceAdapter;
import io.ssenbabies.findawaypoint.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xowns on 2018-08-09.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {

    float lat, lng; // 마커의 위도 경도
    Marker mark; // 시작 마커
    MarkerOptions markerOptions_start; // 시작 마커 설정값
    GoogleMap map; // 구글 맵
    Circle circle; // 반경 1km
    CircleOptions circle1KM; // 반경 1km
    Circle circle_now; // 현재 위치 반경
    CircleOptions circle1Km_now; // 현재 위치 반경 1km
    Button btn_cafe, btn_study, btn_restaurant, btn_alchol, btn_funny, btn_home;
    ListView listView; // 장소 출력 리스트뷰
    ArrayList<Place> placesArrayList; // google place api로 받아온 장소 데이터
    PlaceAdapter adapter;
    TextView tv_station_name1, tv_station_name2, tv_station_name3, tv_station_number1, tv_station_number2, tv_station_number3, tv_location;
    String room;// 방 이름
    RetrofitService service;
    Spinner spinner;
    private ArrayAdapter<CharSequence> adspin;
    LinearLayout layout; // 지하철역 동적으로 생성하기 위한 레이아웃

    ArrayList<Marker> previous_marker = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        lat = intent.getExtras().getFloat("MyLat");
        lng = intent.getExtras().getFloat("MyLng");
        room = intent.getStringExtra("room_name");

        Log.d("Location_lat", String.valueOf(lat));
        Log.d("Location_lng", String.valueOf(lng));

        btn_cafe = (Button) findViewById(R.id.btn_cafe);
        btn_study = (Button) findViewById(R.id.btn_study);
        btn_restaurant = (Button) findViewById(R.id.btn_restaurant);
        btn_alchol = (Button) findViewById(R.id.btn_alchol);
        btn_funny = (Button) findViewById(R.id.btn_funny);
        btn_home = (Button) findViewById(R.id.btn_home);

        spinner = (Spinner) findViewById(R.id.spinner);
        adspin = ArrayAdapter.createFromResource(this, R.array.selected, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MapActivity.this, adspin.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();

                placesArrayList.clear();
                getPlaceData(1, position, lat, lng);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //지하철 역 정보 동적으로 생성
        //지하철 이미지와 TextView가 추가될 layout
        layout = (LinearLayout) findViewById(R.id.subway_layout);

        tv_location = (TextView) findViewById(R.id.tv_location);

        tv_location.setText(getAddress(MapActivity.this, lat, lng));

        btn_cafe.setOnClickListener(this);
        btn_study.setOnClickListener(this);
        btn_restaurant.setOnClickListener(this);
        btn_alchol.setOnClickListener(this);
        btn_funny.setOnClickListener(this);
        btn_home.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.place_list);
        placesArrayList = new ArrayList<Place>();

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        adapter = new PlaceAdapter(this, placesArrayList);
        listView.setAdapter(adapter); // 리스트뷰에 값을 넣음
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemClick", "아이템 클릭");
                Place item = (Place) adapter.getItem(position);

                Marker place_mark;
                double lat = item.getPlaceLatitude();
                double lng = item.getPlaceLongtitude();
                String name = item.getPlaceName();

                LatLng place = new LatLng(lat, lng);

                String place_address = getAddress(MapActivity.this, lat, lng);

                place_mark = map.addMarker(new MarkerOptions()
                        .position(place)
                        .title(name).snippet(place_address));

                place_mark.showInfoWindow(); // 말풍선 띄우기

                map.moveCamera(CameraUpdateFactory.newLatLng(place));
                map.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        });


        //주변 지하철역 정보
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://52.79.94.139:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RetrofitService.class);

        Call<JsonObject> staionCall = service.getStationData(
                lat,
                lng
        );

        //주변 역 정보 출력
        staionCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    Log.d("mylat", String.valueOf(lat));
                    Log.d("mylng", String.valueOf(lng));

                    JsonObject object = response.body();

                    if(object != null) {

                        JsonParser jsonParser = new JsonParser();

                        JsonObject stationObject = (JsonObject) jsonParser.parse(String.valueOf(object));

                        JsonArray arr = (JsonArray) stationObject.get("stationInfo");

                        for(int i=0; i<arr.size(); i++) {

                            JsonObject station = (JsonObject) arr.get(i);
                           // Log.d("역 이름 : ", String.valueOf(station.get("stationName")));

                            JsonArray lineNumberArray = (JsonArray) station.get("stationLineNumber");

                            String stationName = station.get("stationName").toString();
                            String[] result_name = stationName.split("\"");

                            ArrayList<String> line_number_list =  new ArrayList<>();
                            // " 를 기준으로 스트링 파싱
                            for(int j=0; j<lineNumberArray.size(); j++) {

                                String line_number = lineNumberArray.get(j).toString();
                                String[] result_line_number = line_number.split("\"");
                                line_number_list.add(String.valueOf(result_line_number[1]));
                            }

                            for(int j=0; j<line_number_list.size(); j++) {

                                if(line_number_list.get(j).equals("A")) line_number_list.set(j, "공항철도");
                                if(line_number_list.get(j).equals("B")) line_number_list.set(j, "분당선");
                                if(line_number_list.get(j).equals("E")) line_number_list.set(j, "용인경전철");
                                if(line_number_list.get(j).equals("G")) line_number_list.set(j, "경춘선");
                                if(line_number_list.get(j).equals("I")) line_number_list.set(j, "인천1호선");
                                if(line_number_list.get(j).equals("I2")) line_number_list.set(j, "인천2호선");
                                if(line_number_list.get(j).equals("K")) line_number_list.set(j,"경의중앙선");
                                if(line_number_list.get(j).equals("KK")) line_number_list.set(j,"경강선");
                                if(line_number_list.get(j).equals("S")) line_number_list.set(j, "신분당선");
                                if(line_number_list.get(j).equals("SU")) line_number_list.set(j, "수인선");
                                if(line_number_list.get(j).equals("U")) line_number_list.set(j,"의정부경전철");
                            }

                            //지하철역이 여러개의 호선일 경우 처리

                                for(int j=0; j<line_number_list.size(); j++) {

                                //지하철역이 해당되는 호선 이미지
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        ImageView img = new ImageView(MapActivity.this);

                                        if(line_number_list.get(j).equals("1")) img.setImageResource(R.drawable.line_1);
                                        if(line_number_list.get(j).equals("2")) img.setImageResource(R.drawable.line_2);
                                        if(line_number_list.get(j).equals("3")) img.setImageResource(R.drawable.line_3);
                                        if(line_number_list.get(j).equals("4")) img.setImageResource(R.drawable.line_4);
                                        if(line_number_list.get(j).equals("5")) img.setImageResource(R.drawable.line_5);
                                        if(line_number_list.get(j).equals("6")) img.setImageResource(R.drawable.line_6);
                                        if(line_number_list.get(j).equals("7")) img.setImageResource(R.drawable.line_7);
                                        if(line_number_list.get(j).equals("8")) img.setImageResource(R.drawable.line_8);
                                        if(line_number_list.get(j).equals("9")) img.setImageResource(R.drawable.line_9);
                                        if(line_number_list.get(j).equals("인천1호선")) img.setImageResource(R.drawable.line_10);
                                        if(line_number_list.get(j).equals("인천2호선")) img.setImageResource(R.drawable.line_11);
                                        if(line_number_list.get(j).equals("분당선")) img.setImageResource(R.drawable.line_12);
                                        if(line_number_list.get(j).equals("신분당선")) img.setImageResource(R.drawable.line_13);
                                        if(line_number_list.get(j).equals("경의중앙선")) img.setImageResource(R.drawable.line_14);
                                        if(line_number_list.get(j).equals("경춘선")) img.setImageResource(R.drawable.line_15);
                                        if(line_number_list.get(j).equals("공항철도")) img.setImageResource(R.drawable.line_16);
                                        if(line_number_list.get(j).equals("의정부경전철")) img.setImageResource(R.drawable.line_17);
                                        if(line_number_list.get(j).equals("수인선")) img.setImageResource(R.drawable.line_18);
                                        if(line_number_list.get(j).equals("용인경전철")) img.setImageResource(R.drawable.line_19);
                                        if(line_number_list.get(j).equals("자기부상")) img.setImageResource(R.drawable.line_20);
                                        if(line_number_list.get(j).equals("경강선")) img.setImageResource(R.drawable.line_21);
                                        if(line_number_list.get(j).equals("우아신설")) img.setImageResource(R.drawable.line_22);
                                        if(line_number_list.get(j).equals("서해")) img.setImageResource(R.drawable.line_23);

                                        lp.setMargins(0,0,3,0);
                                        img.setLayoutParams(lp);
                                        layout.addView(img);
                                }

                                //지하철 역 이름
                                TextView tv = new TextView(MapActivity.this);

                                result_name[1] = result_name[1].substring(0, result_name[1].length() - 1);
                                tv.setText(result_name[1]);
                                // layout param 설정
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                lp.setMargins(0,0,15,0);
                                tv.setLayoutParams(lp);

                                //부모 뷰에 추가
                                layout.addView(tv);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        //디폴트 -> 카페, 거리순
        getPlaceData(1,0, lat, lng);
        btn_cafe.setTextColor(Color.parseColor("#414042"));
    }

    //현재 위치 받아오기
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    //Google Map Api
    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        LatLng My = new LatLng(lat, lng);
        String my_address = getAddress(this, lat, lng);

        //나의 위치
        MarkerOptions markerOptions_my = new MarkerOptions();
        markerOptions_my.position(My);
        markerOptions_my.title("나의 위치");
        // markerOptions_my.draggable(true); // 드래그 할 수 있게함
        markerOptions_my.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.now)));
        markerOptions_my.snippet(my_address);

        map.addMarker(markerOptions_my);
        map.moveCamera(CameraUpdateFactory.newLatLng(My));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));

        // 반경 1KM원
        circle1Km_now = new CircleOptions().center(new LatLng(lat, lng)) //원점
                .radius(500)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#880000ff")); //배경색

        circle_now = map.addCircle(circle1Km_now);
    }

    // 위도, 경도 -> 주소값
    public static String getAddress(Context context, double lat, double lon) {

        Geocoder geocoder;geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() != 0 ) {

            String address = "";

         //   String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getLocality();
            String subCity = addresses.get(0).getSubLocality();
            String subsubCity = addresses.get(0).getThoroughfare();
            String FeatureName = addresses.get(0).getFeatureName();

            //국가
            /*
           if (country != null && !address.toLowerCase().contains(country.toLowerCase())) { // 나라
                if(address.equals("") || address.equals(" ")|| address.equals("  ")|| address.equals("   ")){
                    address += country;
                }else{
                    address += " " + country;
                }
            }
            */

            //지역
            if(city != null && !address.toLowerCase().contains(city.toLowerCase())){
                if(address.equals("") || address.equals(" ")|| address.equals("  ")|| address.equals("   ")){
                    address += city;
                }else{
                    address += " " + city;
                }
            }

            //구
            if(subCity != null && !address.toLowerCase().contains(subCity.toLowerCase())){ address += " " + subCity;;}

            //동
            if(subsubCity != null && !address.toLowerCase().contains(subsubCity.toLowerCase())){ address += " " + subsubCity;;}

            //번지
            if(FeatureName != null && !address.toLowerCase().contains(FeatureName.toLowerCase())){ address += " " + FeatureName;;}

            return address;
        } else {
            return "";
        }
    }

    //버튼 클릭
    @Override
    public void onClick(View view) {

        int type = 1; // 카페, 음식점, 스터디..
        final int[] sort = new int[1]; // 거리순(0), 별점순(1)
        sort[0] = 0; // default는 거리순

        placesArrayList.clear();

        btn_cafe.setTextColor(Color.parseColor("#d7d7d7"));
        btn_study.setTextColor(Color.parseColor("#d7d7d7"));
        btn_restaurant.setTextColor(Color.parseColor("#d7d7d7"));
        btn_alchol.setTextColor(Color.parseColor("#d7d7d7"));
        btn_funny.setTextColor(Color.parseColor("#d7d7d7"));

        switch(view.getId()) {

            //홈버튼
            case R.id.btn_home:
                startActivity(new Intent(MapActivity.this, MainActivity.class));
                break;

            case R.id.btn_cafe:
                type = 1;
                spinner.setSelection(0);
                break;

            case R.id.btn_study:
                type = 2;
                spinner.setSelection(0);
                break;

            case R.id.btn_restaurant:
                type = 3;
                spinner.setSelection(0);
                break;

            case R.id.btn_alchol:
                type = 4;
                spinner.setSelection(0);
                break;

            case R.id.btn_funny:
                type = 5;
                spinner.setSelection(0);
                break;
        }

        //디폴트 거리순
        if(type == 1) btn_cafe.setTextColor(Color.parseColor("#414042"));
        if(type == 2) btn_study.setTextColor(Color.parseColor("#414042"));
        if(type == 3) btn_restaurant.setTextColor(Color.parseColor("#414042"));
        if(type == 4) btn_alchol.setTextColor(Color.parseColor("#414042"));
        if(type == 5) btn_funny.setTextColor(Color.parseColor("#414042"));

        getPlaceData(type, sort[0], lat, lng);

        //spinner 선택 가능
        final int finalType = type;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MapActivity.this, adspin.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_SHORT).show();
                sort[0] = position;

                placesArrayList.clear();
                getPlaceData(finalType, sort[0], lat, lng);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    // 주변 장소 출력
    void getPlaceData(int type,  int sort, double lat, double lng) {

        Call<JsonObject> placeCall = service.getPlaceData(
                type,
                sort,
                lat,
                lng
        );

        placeCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    JsonObject object = response.body();

                    if(object != null) {

                        JsonParser jsonParser = new JsonParser();

                        JsonObject placeObject = (JsonObject) jsonParser.parse(String.valueOf(object));

                        JsonArray arr = (JsonArray) placeObject.get("data");

                        for(int i=0; i<arr.size(); i++) {

                            JsonObject place = (JsonObject) arr.get(i);

                            String placeName = place.get("placeName").toString();

                            String[] result_name = placeName.split("\"");
                            placeName = result_name[1];

                            double placeLatitude = Double.parseDouble(place.get("placeLatitude").toString());
                            double placeLongtitude = Double.parseDouble(place.get("placeLongtitude").toString());
                            double placeRating = Double.parseDouble(place.get("placeRating").toString());

                            placesArrayList.add(new Place(placeName, placeLatitude, placeLongtitude, placeRating));
                            //    Log.d("장소 이름 : ", String.valueOf(place.get("placeName")));
                            //    Log.d("위도  : ", String.valueOf(place.get("placeLatitude")));
                           //     Log.d("경도  : ", String.valueOf(place.get("placeLongtitude")));
                          //   Log.d("평점  : ", String.valueOf(place.get("placeRating")));
                        }

                        adapter.notifyDataSetChanged(); // 리스뷰 갱신
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}

