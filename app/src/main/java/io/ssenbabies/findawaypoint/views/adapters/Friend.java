package io.ssenbabies.findawaypoint.views.adapters;

public class Friend {

    private int isDone;
    private String userName;

    public Friend(int isDone, String userName) {
        this.isDone = isDone;
        this.userName = userName;
    }

    public int getIsDone() {
        return isDone;
    }

    public String getUserName() {
        return userName;
    }
}
