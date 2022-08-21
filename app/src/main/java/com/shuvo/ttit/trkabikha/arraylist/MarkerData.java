package com.shuvo.ttit.trkabikha.arraylist;

import com.google.android.gms.maps.model.Marker;

public class MarkerData {
    private Marker marker;
    private String id;
    private boolean poly;

    public MarkerData(Marker marker, String id, boolean poly) {
        this.marker = marker;
        this.id = id;
        this.poly = poly;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPoly() {
        return poly;
    }

    public void setPoly(boolean poly) {
        this.poly = poly;
    }
}
