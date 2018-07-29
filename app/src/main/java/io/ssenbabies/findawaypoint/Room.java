package io.ssenbabies.findawaypoint;

public class Room {

    private String roomTitle;
    private String appointmentPlaceContents;
    private String arroundStationContents;

    public Room(String roomTitle, String appointmentPlaceContents, String arroundStationContents) {
        this.roomTitle = roomTitle;
        this.appointmentPlaceContents = appointmentPlaceContents;
        this.arroundStationContents = arroundStationContents;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getAppointmentPlaceContents() {
        return appointmentPlaceContents;
    }

    public String getArroundStationContents() {
        return arroundStationContents;
    }


}