package io.ssenbabies.findawaypoint.views.adapters;

public class Room {

    private String roomID;
    private String roomTitle;
    private String appointmentPlaceContents;
    private String arroundStationContents;
    private int isOnGoing;

    public Room(String roomID, String roomTitle, String appointmentPlaceContents, String arroundStationContents, int onGoing) {
        this.roomID = roomID;
        this.roomTitle = roomTitle;
        this.appointmentPlaceContents = appointmentPlaceContents;
        this.arroundStationContents = arroundStationContents;
        this.isOnGoing = onGoing;
    }

    public String getRoomID(){ return roomID;};

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getAppointmentPlaceContents() {
        return appointmentPlaceContents;
    }

    public String getArroundStationContents() {
        return arroundStationContents;
    }

    public int getIsOnGoing(){
        return isOnGoing;
    }
}