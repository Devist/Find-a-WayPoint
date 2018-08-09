package io.ssenbabies.findawaypoint.DTO;

/**
 * Created by xowns on 2018-08-09.
 */

public class GooglePlace {

    private double lat; // 위도
    private double lng; // 경도
    private String name; // 이름
    private String addr; // 주소
    private String type;// 종류

    public GooglePlace(double lat, double lng, String name, String addr, String type) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.addr = addr;
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
