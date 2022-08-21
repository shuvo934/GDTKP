package com.shuvo.ttit.trkabikha.arraylist;

public class FinancialYearLists {

    private String fyId;
    private String financialYearName;
    private String fyFromYear;
    private String fyToYear;
    private String fyDetails;
    private String activeFlag;

    public FinancialYearLists(String fyId, String financialYearName, String fyFromYear, String fyToYear, String fyDetails, String activeFlag) {
        this.fyId = fyId;
        this.financialYearName = financialYearName;
        this.fyFromYear = fyFromYear;
        this.fyToYear = fyToYear;
        this.fyDetails = fyDetails;
        this.activeFlag = activeFlag;
    }

    public String getFyId() {
        return fyId;
    }

    public void setFyId(String fyId) {
        this.fyId = fyId;
    }

    public String getFinancialYearName() {
        return financialYearName;
    }

    public void setFinancialYearName(String financialYearName) {
        this.financialYearName = financialYearName;
    }

    public String getFyFromYear() {
        return fyFromYear;
    }

    public void setFyFromYear(String fyFromYear) {
        this.fyFromYear = fyFromYear;
    }

    public String getFyToYear() {
        return fyToYear;
    }

    public void setFyToYear(String fyToYear) {
        this.fyToYear = fyToYear;
    }

    public String getFyDetails() {
        return fyDetails;
    }

    public void setFyDetails(String fyDetails) {
        this.fyDetails = fyDetails;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }
}
