package io.ssenbabies.findawaypoint.databases;

/**
 * Created by xowns on 2018-08-18.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import io.ssenbabies.findawaypoint.views.adapters.Room;

/**
 * Created by xowns on 2018-08-10.
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

        db.execSQL("CREATE TABLE APPOINTMENTS (_id TEXT PRIMARY KEY, _name TEXT, _place TEXT, _station TEXT, _ongoing INT(1) DEFAULT 0);");
        db.execSQL("CREATE TABLE search_table( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
    }

    public void insertSampleRoom(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO APPOINTMENTS(_id,_name,_place,_station,_ongoing) VALUES('60','샌애기팀 모임','서울 강남구 역삼동','강남역,역삼역',1);");
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
                   ,cursor.getString(2)
                   ,cursor.getString(3)
                   ,cursor.getInt(4)
           ));
        }
        db.close();
        return rooms;
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

