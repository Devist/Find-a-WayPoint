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

    private static String CONNECTION    = "CONNECTION";     // 소켓 커넥션 이벤트
    private static String ENTRANCE      = "ENTRANCE";       // 방 입장 이벤트
    private static String RELOAD_ROOM   = "ROOM_INFO";      // 코드 존재 여부 찾을 때 발생하는 이벤트
    private static String CREATE_ROOM   = "ROOM";           // 방 생성 이벤트
    private static String PICK          = "PICK";           // 위치 입력 이벤트
    private static String COMPLETE      = "COMPLETE";       // 완료 이벤트
    private static String ROOM_LIST     = "ROOM_LIST";      // 음..
    private static String URL           = "http://here-dot.kro.kr/";

    private io.socket.client.Socket mSocket;


    // 이벤트 등록 리스너
    public interface WaySocketListener {
        void onConnectionEventReceived();                   //소켓 커넥션 완료를 알려줌
        void onEntranceEventReceived(JSONObject result);    //방 입장시 발생하는 정보를 알려줌
        void onReloadEventReceived(JSONObject result);      //뱅 정보를 알려줌. 방 코드 입력 후 발생
        void onCreateResultReceived(JSONObject result);     //방 생성 정보를 알려줌
        void onPickResultReceived(JSONObject result);        //위치 정보 입력 발생 정보를 알려줌
        void onRoomListReceived(JSONObject result);
        void onCompleteResultReceived(JSONObject result);

    }
    private WaySocketListener listener;
    public void setWaySocketListener(WaySocketListener listener) {
        this.listener = listener;
    }


    //싱글톤
    private static WaySocket ourInstance = new WaySocket();
    public static WaySocket getInstance() {
        return ourInstance;
    }
    private WaySocket() {
        this.listener = null;
        try{
            mSocket = IO.socket(URL);
            mSocket.connect();

            //이벤트 처리
            mSocket.on(CONNECTION, onConnectionResultReceived);
            mSocket.on(CREATE_ROOM,onCreateResultReceived);         //ROOM
            mSocket.on(PICK,onPickResultReceived);
            mSocket.on(ENTRANCE, onEntranceResultReceived);
            mSocket.on(COMPLETE,onCompleteResultReceived);
            mSocket.on(RELOAD_ROOM,onReloadResultReceived);


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void requestCreateRoom(String name){ //ROOM 이벤트 발생기
        JSONObject data = new JSONObject();
        try {
            data.put("room_name", name);
            mSocket.emit(CREATE_ROOM, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestEntrance(String currentRoomCode, String userName) {  //ENTRANCE 이벤트 발생기
        Log.d("테스트","방 입장 요청");
        JSONObject data = new JSONObject();
        try {
            data.put("room_code", currentRoomCode);
            data.put("user_name",userName);
            mSocket.emit(ENTRANCE, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestPick(String roomCode, Double lat, Double lng, String userName){  //PICK 이벤트 발생기
       Log.d("테스트 픽 실행", "OK");
        JSONObject data = new JSONObject();
        try {
            data.put("room_code", roomCode);
            data.put("lat", lat);
            data.put("long",lng);
            data.put("user_name",userName);
            mSocket.emit(PICK, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    public void requestComplete(String roomCode){  //COMPLETE 이벤트 발생기
        JSONObject data = new JSONObject();
        try {
            data.put("room_code", roomCode);
            mSocket.emit(RELOAD_ROOM, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestReloadRoom(String roomCode){  //ROOM_INFO 이벤트 발생기
        JSONObject data = new JSONObject();
        try {
            data.put("room_code", roomCode);
            mSocket.emit(RELOAD_ROOM, data);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestRoomList(String[] roomCodes){
        JSONObject data = new JSONObject();
        try{
            data.put("room_codes",roomCodes);
            mSocket.emit(ROOM_LIST,data);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    //CONNECTION 이벤트 처리기
    private Emitter.Listener onConnectionResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            if(listener!=null)
                listener.onConnectionEventReceived();
        }
    };

    //ENTRANCE 이벤트 처리기
    private Emitter.Listener onEntranceResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("테스트","입장 이벤트 들어왔어요");
            JSONObject receivedData = (JSONObject) args[0];
            Log.d("테스트 룸인포",receivedData.toString());
            if(listener!=null)
                listener.onEntranceEventReceived(receivedData);
        }
    };

    //ROOM 이벤트 처리기
    private Emitter.Listener onCreateResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            if(listener!=null)
                listener.onCreateResultReceived(receivedData);

        }
    };

    //PICK 이벤트 처리기
    private Emitter.Listener onPickResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            if(listener!=null)
                listener.onPickResultReceived(receivedData);
        }
    };

    //COMPLETE 이벤트 처리기
    private Emitter.Listener onCompleteResultReceived= new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            if(listener!=null)
                listener.onCompleteResultReceived(receivedData);
        }
    };

    //ROOM_INFO 이벤트 처리기
    private Emitter.Listener onReloadResultReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            Log.d("테스트 룸인포",receivedData.toString());
            if(listener!=null)
                listener.onReloadEventReceived(receivedData);
        }
    };

    private Emitter.Listener onRoomListReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject receivedData = (JSONObject) args[0];
            if(listener!=null)
                listener.onRoomListReceived(receivedData);
        }
    };
}
