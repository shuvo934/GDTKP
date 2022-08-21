package com.shuvo.ttit.trkabikha.arraylist;

public class ProjectTypeLists {

    private String ptmId;
    private String projectTypeName;

    public ProjectTypeLists(String ptmId, String projectTypeName) {
        this.ptmId = ptmId;
        this.projectTypeName = projectTypeName;
    }

    public String getPtmId() {
        return ptmId;
    }

    public void setPtmId(String ptmId) {
        this.ptmId = ptmId;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }
}
