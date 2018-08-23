package io.ssenbabies.findawaypoint.views.adapters;

public class Room {

    private String roomID;
    private String roomTitle;
    private String roomDate;
    private int isOnGoing;

    public Room(String roomID, String roomTitle, String roomDate, int onGoing) {
        this.roomID = roomID;
        this.roomTitle = roomTitle;
        this.roomDate = roomDate;
        this.isOnGoing = onGoing;
    }

    public String getRoomID(){ return roomID;};

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getRoomDate() {
        return roomDate;
    }

    public int getIsOnGoing(){
        return isOnGoing;
    }
}