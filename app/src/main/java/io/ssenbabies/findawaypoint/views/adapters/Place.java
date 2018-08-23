package io.ssenbabies.findawaypoint.views.adapters;

public class Place {

    private String placeName;
    private double placeLatitude;
    private double placeLongtitude;
    private double placeRating;

    public Place(String placeName, double placeLatitude, double placeLongtitude, double placeRating) {
        this.placeName = placeName;
        this.placeLatitude = placeLatitude;
        this.placeLongtitude = placeLongtitude;
        this.placeRating = placeRating;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public double getPlaceLongtitude() {
        return placeLongtitude;
    }

    public void setPlaceLongtitude(double placeLongtitude) {
        this.placeLongtitude = placeLongtitude;
    }

    public double getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(double placeRating) {
        this.placeRating = placeRating;
    }
}
