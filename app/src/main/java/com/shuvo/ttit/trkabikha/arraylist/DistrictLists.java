package com.shuvo.ttit.trkabikha.arraylist;

public class DistrictLists {

    private String distId;
    private String distName;

    public DistrictLists(String distId, String distName) {
        this.distId = distId;
        this.distName = distName;
    }

    public String getDistId() {
        return distId;
    }

    public void setDistId(String distId) {
        this.distId = distId;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }
}
