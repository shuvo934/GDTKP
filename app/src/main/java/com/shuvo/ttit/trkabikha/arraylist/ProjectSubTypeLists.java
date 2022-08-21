package com.shuvo.ttit.trkabikha.arraylist;

public class ProjectSubTypeLists {

    private String ptdId;
    private String projectSubTypeName;

    public ProjectSubTypeLists(String ptdId, String projectSubTypeName) {
        this.ptdId = ptdId;
        this.projectSubTypeName = projectSubTypeName;
    }

    public String getPtdId() {
        return ptdId;
    }

    public void setPtdId(String ptdId) {
        this.ptdId = ptdId;
    }

    public String getProjectSubTypeName() {
        return projectSubTypeName;
    }

    public void setProjectSubTypeName(String projectSubTypeName) {
        this.projectSubTypeName = projectSubTypeName;
    }
}
