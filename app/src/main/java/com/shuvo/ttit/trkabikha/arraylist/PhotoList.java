package com.shuvo.ttit.trkabikha.arraylist;

public class PhotoList {
    private String photoName;
    private String uploadDate;
    private String stage;

    public PhotoList(String photoName, String uploadDate, String stage) {
        this.photoName = photoName;
        this.uploadDate = uploadDate;
        this.stage = stage;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
