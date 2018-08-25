package io.ssenbabies.findawaypoint.databases;

/**
 * Created by xowns on 2018-08-18.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.ssenbabies.findawaypoint.views.adapters.Room;

/**
 * Created by xowns on 2018-08-line_10.
 */

public class DBHelper extends SQLiteOpenHelper {

    int db_version;
    //안드로이드에서 SQLite 데이터 베이스를 쉽게 사용할 수 있도록 도와주는 클래스
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //최초에 데이터베이스가 없는 경우, 데이터베이스 생성을 위해 호출됨
        //테이블을 생성하는 코드를 작성한다.

        db.execSQL("CREATE TABLE APPOINTMENTS (_id TEXT PRIMARY KEY, _name TEXT, _place TEXT, _station TEXT, _date TEXT, _members TEXT, _ongoing INT(1) DEFAULT 1, _ready INT(1) DEFAULT 0);");
        db.execSQL("CREATE TABLE search_table( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
    }

    public void insertSampleRoom(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO APPOINTMENTS VALUES('70','샌애기팀 모임','서울 강남구 역삼동','강남역,역삼역','2018. 04. 21','이윤희, 이은솔, 성락',0,0);");
        db.execSQL("INSERT INTO APPOINTMENTS VALUES('60','오늘저녁은 치킨이닭','서울 강남구 역삼동','강남역,역삼역','2018. 04. 21','이윤희, 이은솔, 성락',1,0);");
        db.close();
    }

    //약속장소 정하기 완료되면 INSERT
    public void setAppointment(String roomCode, String roomName){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd");
        String getTime = sdf.format(date);

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO APPOINTMENTS (_id, _name,_date) VALUES(?,?,?);",new String[]{roomCode,roomName,getTime});
        db.close();
    }

    //내 위치 입력 완료하면 내 위치 입력 완료했다고 UPDATE - 메인에서 진행중인 약속 리스트 클릭시,
    //내 위치 입력 페이지로 갈지, 약속 장소 정하기 페이지로 갈지 결정하기 위함
    public void updatePickStateToDone(String roomCode){

        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE APPOINTMENTS SET _ready = 1 WHERE _id= ?;",new String[]{roomCode});
        db.close();
    }

    //출발위치정하기 팝업에서, '네'를 눌렀을 때 친구 목록 한꺼번에 저장
    public void updateFriends(String roomCode, String[] friends){
        String _members = "";
        for(int i = 0 ;i<friends.length ; i++){
            if((i+1) != friends.length)
                _members+=(friends[i]+", ");
            else
                _members+=friends[i];
        }
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE APPOINTMENTS SET _members = ? WHERE _id= ?;",new String[]{ _members,roomCode});
        db.close();
    }

    public void updatePlaceList(String roomCode, String place, String stations){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE APPOINTMENTS SET _place = ?, _station = ?, _ongoing=0 WHERE _id= ?;",new String[]{ place,stations, roomCode});
        db.close();
    }

    public List<Room> getAppointments(){
        SQLiteDatabase db = getReadableDatabase();
        List<Room> rooms = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM APPOINTMENTS", null);

        Room[] room = new Room[cursor.getColumnCount()];
        while (cursor.moveToNext()) {
           rooms.add(new Room( cursor.getString(0)
                   ,cursor.getString(1)
                   ,cursor.getString(4)
                   ,cursor.getInt(6)
                   ,cursor.getInt(7)
           ));
        }
        db.close();
        return rooms;
    }

    public String[] getDetailAppointment(String room_code){
        SQLiteDatabase db = getReadableDatabase();
        String[] result = new String[7];
        Cursor cursor = db.rawQuery("SELECT * FROM APPOINTMENTS WHERE _id='"+room_code+"';", null);
        cursor.moveToFirst();
        for(int i = 0 ;i<7; i++){
            result[i] = cursor.getString(i);
        }

        db.close();
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //데이터베이스의 버전이 바뀌었을 때 호출되는 콜벡 메서드
        //버전이 바뀌었을 때 기존데이터베이스를 어떻게 변경할 것인지 작성한다.
        //각 버전의 변경 내용들을 버전마다 작성해야됨
    }
    
    //등록
    public void insert(String query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    //값 출력
    public String print_content( ) {

        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("select * from search_table", null);
        while(cursor.moveToNext()) {
            result += " id : " + cursor.getInt(0)
                    + ", place name : " + cursor.getString(1)
                    + "\n";
        }

        return result;
    }

    //값 삭제
    public void delete( ) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from search_table");
        db.close();
    }


    void nameList(ArrayList<String> arrayList) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select name from search_table", null);
        if (cursor != null)
        {
            while(cursor.moveToNext()) {
                String place_name = cursor.getString(0);
                arrayList.add(place_name);
            }
            cursor.close();
        }
    }
}

