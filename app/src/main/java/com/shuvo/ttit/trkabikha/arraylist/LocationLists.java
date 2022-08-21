package com.shuvo.ttit.trkabikha.arraylist;

public class LocationLists {

    private String latitude;
    private String longitude;
    private int segment;

    public LocationLists(String latitude, String longitude, int segment) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.segment = segment;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getSegment() {
        return segment;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }
}
