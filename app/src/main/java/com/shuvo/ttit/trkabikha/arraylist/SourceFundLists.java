package com.shuvo.ttit.trkabikha.arraylist;

public class SourceFundLists {

    private String fsmId;
    private String fundName;

    public SourceFundLists(String fsmId, String fundName) {
        this.fsmId = fsmId;
        this.fundName = fundName;
    }

    public String getFsmId() {
        return fsmId;
    }

    public void setFsmId(String fsmId) {
        this.fsmId = fsmId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }
}
