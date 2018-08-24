package io.ssenbabies.findawaypoint.views.adapters;

public class Room {

    private String roomID;
    private String roomTitle;
    private String roomDate;
    private int isOnGoing;
    private int alreadyPick;

    public Room(String roomID, String roomTitle, String roomDate, int onGoing, int alreadyPick) {
        this.roomID = roomID;
        this.roomTitle = roomTitle;
        this.roomDate = roomDate;
        this.isOnGoing = onGoing;
        this.alreadyPick = alreadyPick;
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

    public int getAlreadyPick(){
        return alreadyPick;
    }
}