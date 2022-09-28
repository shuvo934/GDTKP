package com.shuvo.ttit.trkabikha.arraylist;

import android.graphics.Bitmap;

public class PhotoListFromPE {

    private Bitmap photoName;
    private String uploadDate;
    private String stage;

    public PhotoListFromPE(Bitmap photoName, String uploadDate, String stage) {
        this.photoName = photoName;
        this.uploadDate = uploadDate;
        this.stage = stage;
    }

    public Bitmap getPhotoName() {
        return photoName;
    }

    public void setPhotoName(Bitmap photoName) {
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
