package com.shuvo.ttit.trkabikha.arraylist;

public class SelectedVillageList {

    private String ddv_id;
    private String village_name;
    private String ddv_ddw_id;

    public SelectedVillageList(String ddv_id, String village_name, String ddv_ddw_id) {
        this.ddv_id = ddv_id;
        this.village_name = village_name;
        this.ddv_ddw_id = ddv_ddw_id;
    }

    public String getDdv_id() {
        return ddv_id;
    }

    public void setDdv_id(String ddv_id) {
        this.ddv_id = ddv_id;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getDdv_ddw_id() {
        return ddv_ddw_id;
    }

    public void setDdv_ddw_id(String ddv_ddw_id) {
        this.ddv_ddw_id = ddv_ddw_id;
    }
}
