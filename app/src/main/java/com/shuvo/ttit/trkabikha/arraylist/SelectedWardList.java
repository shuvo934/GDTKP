package com.shuvo.ttit.trkabikha.arraylist;

public class SelectedWardList {

    private String ddw_id;
    private String ward_name;

    public SelectedWardList(String ddw_id, String ward_name) {
        this.ddw_id = ddw_id;
        this.ward_name = ward_name;
    }

    public String getDdw_id() {
        return ddw_id;
    }

    public void setDdw_id(String ddw_id) {
        this.ddw_id = ddw_id;
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }
}
