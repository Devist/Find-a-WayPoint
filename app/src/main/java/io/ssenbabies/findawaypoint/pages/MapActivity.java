package io.ssenbabies.findawaypoint.pages;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.ssenbabies.findawaypoint.adapter.GooglePlace;
import io.ssenbabies.findawaypoint.adapter.GooglePlaceAdapter;
import io.ssenbabies.findawaypoint.R;
import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

/**
 * Created by xowns on 2018-08-09.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, PlacesListener, LocationListener, View.OnClickListener, AdapterView.OnItemClickListener {

    float lat, lng; // 마커의 위도 경도
    Marker mark; // 시작 마커
    MarkerOptions markerOptions_start; // 시작 마커 설정값
    GoogleMap map; // 구글 맵
    Circle circle; // 반경 1km
    CircleOptions circle1KM; // 반경 1km
    Circle circle_now; // 현재 위치 반경
    CircleOptions circle1Km_now; // 현재 위치 반경 1km
    Button btn_cafe, btn_restaurant, btn_alchol, btn_movie, btn_funny, btn_home;
    int type = 1; // 장소 종류 default = 1 ( 카페 )
    ListView listView; // 장소 출력 리스트뷰
    ArrayList<GooglePlace> placesArrayList; // google place api로 받아온 장소 데이터
    GooglePlaceAdapter adapter;
    TextView tv_station1, tv_station2, tv_station3, roomName;
    String room;// 방 이름

    ArrayList<Marker> previous_marker = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        lat = intent.getExtras().getFloat("MyLat");
        lng = intent.getExtras().getFloat("MyLng");
        room = intent.getStringExtra("room_name");

        btn_cafe = (Button) findViewById(R.id.btn_cafe);
        btn_restaurant = (Button) findViewById(R.id.btn_restaraunt);
        btn_funny = (Button) findViewById(R.id.btn_funny);
        btn_movie = (Button) findViewById(R.id.btn_movie);
        btn_alchol = (Button) findViewById(R.id.btn_alchol);
        btn_home = (Button) findViewById(R.id.btn_home);
        tv_station1 = (TextView) findViewById(R.id.tv_station1);
        tv_station2 = (TextView) findViewById(R.id.tv_station2);
        tv_station3 = (TextView) findViewById(R.id.tv_station3);
        roomName = (TextView) findViewById(R.id.tv_room_name);

        roomName.setText(room);

        btn_cafe.setOnClickListener(this);
        btn_restaurant.setOnClickListener(this);
        btn_funny.setOnClickListener(this);
        btn_movie.setOnClickListener(this);
        btn_alchol.setOnClickListener(this);
        btn_home.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.place_list);
        placesArrayList = new ArrayList<GooglePlace>();

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        showPlaceInformation(new LatLng(lat, lng), type); //력 디폴트 장소인 카페 출

        adapter = new GooglePlaceAdapter(this, placesArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
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


    // 인자로 타입 줘서 rankby("distance") -> 거리순, 그냥 default가 평점순
    public void showPlaceInformation(LatLng location, int type)
    {
        //  map.clear();//지도 클리어

        if (previous_marker.size() != 0) {

            for(int i=0; i < previous_marker.size(); i++) {
                previous_marker.get(i).remove();
            }

            previous_marker.clear();//지역정보 마커 클리어
        }

        placesArrayList.clear(); // 리스트뷰 클리어

        if(type == 1) { // 카페

            Log.d("cafe", "cafe");
            new NRPlaces.Builder()
                    .listener(MapActivity.this)
                    .key("AIzaSyC0IBPtDHrgMl-4VzCaTfo2TZmyniRVZ0Y")
                    .latlng(lat, lng)//현재 위치
                    .radius(500)
                    .type(PlaceType.CAFE)
                    .language("ko", "+82")
                    .build()
                    .execute();
        }

        else if(type == 2) { // 음식점

            Log.d("food", "food");
            new NRPlaces.Builder()
                    .listener(MapActivity.this)
                    .key("AIzaSyC0IBPtDHrgMl-4VzCaTfo2TZmyniRVZ0Y")
                    .latlng(lat, lng)//현재 위치
                    .radius(500) //500 미터 내에서 검색
                    .type(PlaceType.RESTAURANT)
                    .language("ko", "+82")
                    .build()
                    .execute();
        }

        else if(type == 3) { // 영화관

            Log.d("movie", "movie");
            new NRPlaces.Builder()
                    .listener(MapActivity.this)
                    .key("AIzaSyC0IBPtDHrgMl-4VzCaTfo2TZmyniRVZ0Y")
                    .latlng(lat, lng)//현재 위치
                    .radius(1000) //500 미터 내에서 검색
                    .type(PlaceType.MOVIE_THEATER)
                    .language("ko", "+82")
                    .build()
                    .execute();
        }

        else if(type == 4 ) {// 오락

            Log.d("station", "station");
            new NRPlaces.Builder()
                    .listener(MapActivity.this)
                    .key("AIzaSyC0IBPtDHrgMl-4VzCaTfo2TZmyniRVZ0Y")
                    .latlng(lat, lng)//현재 위치
                    .radius(2000)
                    .type(PlaceType.AMUSEMENT_PARK)
                    .language("ko", "+82")
                    .build()
                    .execute();
        }

        else { // 술집
            new NRPlaces.Builder()
                    .listener(MapActivity.this)
                    .key("AIzaSyC0IBPtDHrgMl-4VzCaTfo2TZmyniRVZ0Y")
                    .latlng(lat, lng)//현재 위치
                    .radius(500) //500 미터 내에서 검색
                    .type(PlaceType.NIGHT_CLUB)
                    .build()
                    .execute();
        }
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

            String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getLocality();
            String subCity = addresses.get(0).getSubLocality();
            String subsubCity = addresses.get(0).getThoroughfare();

            if (country != null && !address.toLowerCase().contains(country.toLowerCase())) { // 나라
                if(address.equals("") || address.equals(" ")|| address.equals("  ")|| address.equals("   ")){
                    address += country;
                }else{
                    address += " " + country;
                }
            }

            if(city != null && !address.toLowerCase().contains(city.toLowerCase())){ // 지역
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

            return address;
        } else {
            return "";
        }
    }


    //Google Place Api
    @Override
    public void onPlacesFailure(PlacesException e) {

        if (e.getMessage().equals("OVER_QUERY_LIMIT")) {

            //2초 쉬고

            //다시검색
            //showPlaceInformation(new LatLng(lat, lng), type); // 다시 마커를 띄운다.
        }

        Log.d("onPlacesFaileure", e.getMessage());
    }

    @Override
    public void onPlacesStart() {

    }

    //OVER_QUERY_LIMIT : 쿼리양이 하루 호출할 수 있는 횟수를 넘어감 없는것은 디비에 넣고 있는 것은 디비에서 가져오 도록 구현
    @Override
    public void onPlacesSuccess(final List<Place> places) {

        //   final LinkedHashSet set = new LinkedHashSet<>(); // 역이름 중복제거

        Log.d("onPlacesSuccess", "onPlacesSuccess");
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                //지도에 마커 찍기
                for (noman.googleplaces.Place place : places) {

                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    String address = getAddress(getApplicationContext(), latLng.latitude, latLng.longitude);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());

                    markerOptions.snippet(address);
                    Marker item = map.addMarker(markerOptions);
                    previous_marker.add(item);

                    //리스트뷰 에 담기
                    placesArrayList.add(new GooglePlace(place.getLatitude(), place.getLongitude(), place.getName(), address, place.getTypes()[0]));
                }

                //리스트뷰 갱신
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPlacesFinished() {

    }

    //버튼 클릭
    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            //홈버튼
            case R.id.btn_home:
                startActivity(new Intent(MapActivity.this, MainActivity.class));
                break;

            case R.id.btn_cafe:
                type = 1;
                showPlaceInformation(new LatLng(lat, lng), type);
                break;

            case R.id.btn_restaraunt:
                type = 2;
                showPlaceInformation(new LatLng(lat, lng), type);
                break;

            case R.id.btn_movie:
                type = 3;
                showPlaceInformation(new LatLng(lat, lng), type);
                break;

            case R.id.btn_funny:
                type = 4;
                showPlaceInformation(new LatLng(lat, lng), type);
                break;

            case R.id.btn_alchol:
                type = 5;
                showPlaceInformation(new LatLng(lat, lng), type);
                break;
        }
    }

    //리스트뷰 클릭
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        GooglePlace item = (GooglePlace) adapter.getItem(i);

        Marker place_mark;
        double lat = item.getLat();
        double lng = item.getLng();
        String name = item.getName();

        LatLng place = new LatLng(lat, lng);

        String place_address = getAddress(this, lat, lng);

        place_mark = map.addMarker(new MarkerOptions()
                .position(place)
                .title(name).snippet(place_address));

        place_mark.showInfoWindow(); // 말풍선 띄우기

        map.moveCamera(CameraUpdateFactory.newLatLng(place));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}

