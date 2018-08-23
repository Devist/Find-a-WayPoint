package io.ssenbabies.findawaypoint.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class WaySocket {

    private static String CONNECTION    = "CONNECTION";
    private static String CREATE_ROOM   = "ROOM";
    private static String PICK          = "PICK";
    private static String COMPLETE      = "COMPLETE";
    private static String RELOAD_ROOM   = "RELOAD_ROOM";

    private io.socket.client.Socket mSocket;

    // 싱글톤
    private static WaySocket ourInstance = new WaySocket();
    public static WaySocket getInstance() {
        return ourInstance;
    }

    private WaySocket() {
        try{
            mSocket = IO.socket("http://here-dot.kro.kr/");
            mSocket.connect();
            //Log.d("소켓 ID : ", id);

            //이벤트 처리
            mSocket.on(CONNECTION, onConnectionResultReceived);
            mSocket.on(CREATE_ROOM,onCreateResultReceived);
            //mSocket.on(PICK,onPickResultReceived);
            //mSocket.on(COMPLETE,onCompleteResultReceived);
            //mSocket.on(RELOAD_ROOM,onReloadResultReceived);


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
    public void requestPick(){
        JSONObject data = new JSONObject();
        try {
            data.put("name", "value1");
            data.put("room", "value2");
            data.put("","");
            mSocket.emit(RELOAD_ROOM, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    public void requestComplete(){
        JSONObject data = new JSONObject();
        try {
            data.put("key1", "value1");
            data.put("key2", "value2");
            mSocket.emit(RELOAD_ROOM, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestReloadRoom(){
        JSONObject data = new JSONObject();
        try {
            data.put("key1", "value1");
            data.put("key2", "value2");
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


    // Socket서버에 connect 된 후, 서버로부터 전달받은 'Socket.EVENT_CONNECT' Event 처리.
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // your code...
            Log.d("소켓 ",args.toString());
        }
    };

    // 서버로부터 전달받은 'chat-message' Event 처리.
    private Emitter.Listener onConnectionResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
            JSONObject receivedData = (JSONObject) args[0];
            try{
                Log.d("소켓 커넥션 시도 : ",receivedData.getString("msg"));
            }catch(Exception e){

            }
        }
    };

    private Emitter.Listener onCreateResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("소켓 방 생성 결과 : ","결과받음");
            // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
            JSONObject receivedData = (JSONObject) args[0];
            try{
                Log.d("소켓 방 생성 결과 : ",receivedData.getString("room_name"));
                Log.d("소켓 방 생성 결과 : ",receivedData.getString("msg"));
            }catch(Exception e){
                Log.d("소켓 방 생성 실패 : ",receivedData.toString());
            }
        }
    };

    private Emitter.Listener onPickResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

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

        }
    };


}
