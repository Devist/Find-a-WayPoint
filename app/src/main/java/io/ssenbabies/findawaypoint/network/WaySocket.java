package io.ssenbabies.findawaypoint.network;

import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WaySocket {

    public static int SUCCESS  = 1;
    public static int FAIL     = 0;

    private static String CONNECTION    = "CONNECTION";
    private static String CREATE_ROOM   = "ROOM";
    private static String PICK          = "PICK";
    private static String COMPLETE      = "COMPLETE";
    private static String RELOAD_ROOM   = "ROOM_INFO";
    private static String ENTRANCE      = "ENTRANCE";
    private static String URL           = "http://here-dot.kro.kr/";

    private io.socket.client.Socket mSocket;




    // 이벤트 등록 리스너
    public interface WaySocketListener {
        void onCreateResultReceived(JSONObject result);
        void onPickEventReceived(JSONObject result);
        void onConnectionEventReceived();
        void onReloadEventReceived(JSONObject result);
        void onEntranceEventReceived(JSONObject result);
    }

    private WaySocketListener listener;

    public void setWaySocketListener(WaySocketListener listener) {
        this.listener = listener;
    }


    //생성자 및 싱글톤
    private static WaySocket ourInstance = new WaySocket();
    public static WaySocket getInstance() {
        return ourInstance;
    }

    private WaySocket() {
        this.listener = null;
        try{
            mSocket = IO.socket(URL);
            mSocket.connect();
            //Log.d("소켓 ID : ", id);

            //이벤트 처리
            mSocket.on(CONNECTION, onConnectionResultReceived);
            mSocket.on(CREATE_ROOM,onCreateResultReceived);
            mSocket.on(PICK,onPickResultReceived);
            mSocket.on(ENTRANCE, onEntranceResultRecieved);
            //mSocket.on(COMPLETE,onCompleteResultReceived);
            mSocket.on(RELOAD_ROOM,onReloadResultReceived);


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void requestCreateRoom(String name, String msg){
        JSONObject data = new JSONObject();
        try {
            data.put("room_name", name);
            mSocket.emit(CREATE_ROOM, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestEntrance(String currentRoomCode, String userName) {
        JSONObject data = new JSONObject();
        try {
            data.put("room_code", currentRoomCode);
            data.put("user_name",userName);
            mSocket.emit(ENTRANCE, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestPick(String room_code, Double lat, Double lng){
        Log.d("테스트 픽 실행", "OK");
        JSONObject data = new JSONObject();
        try {
            data.put("room_code", room_code);
            data.put("lat", lat);
            data.put("long",lng);
            mSocket.emit(PICK, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    public void requestComplete(String room_code){
        JSONObject data = new JSONObject();
        try {
            data.put("room_code", room_code);
            mSocket.emit(RELOAD_ROOM, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestReloadRoom(String room_code){
        JSONObject data = new JSONObject();
        try {
            data.put("room_code", room_code);
            mSocket.emit(RELOAD_ROOM, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestFindRoom(){
        JSONObject data = new JSONObject();
        try {
            data.put("key1", "value1");
            data.put("key2", "value2");
            mSocket.emit("FIND_ROOM", data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }


    private Emitter.Listener onConnectionResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
            JSONObject receivedData = (JSONObject) args[0];
            if(listener!=null)
                listener.onConnectionEventReceived();
        }
    };

    private Emitter.Listener onEntranceResultRecieved = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
            JSONObject receivedData = (JSONObject) args[0];
            if(listener!=null)
                listener.onEntranceEventReceived(receivedData);
        }
    };

    private Emitter.Listener onCreateResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            if(listener!=null)
                listener.onCreateResultReceived(receivedData);

        }
    };

    private Emitter.Listener onPickResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            if(listener!=null)
                listener.onPickEventReceived(receivedData);
        }
    };

    private Emitter.Listener onCompleteResultReceived= new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };

    private Emitter.Listener onReloadResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            if(listener!=null)
                listener.onReloadEventReceived(receivedData);
        }
    };


}
