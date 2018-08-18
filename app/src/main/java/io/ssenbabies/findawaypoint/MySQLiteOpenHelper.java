package io.ssenbabies.findawaypoint;

/**
 * Created by xowns on 2018-08-18.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

/**
 * Created by xowns on 2018-08-10.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    int db_version;
    //안드로이드에서 SQLite 데이터 베이스를 쉽게 사용할 수 있도록 도와주는 클래스
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //최초에 데이터베이스가 없는 경우, 데이터베이스 생성을 위해 호출됨
        //테이블을 생성하는 코드를 작성한다.
        String sql = "CREATE TABLE search_table( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
        db.execSQL(sql);
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

