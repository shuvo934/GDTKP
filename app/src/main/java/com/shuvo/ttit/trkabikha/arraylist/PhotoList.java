package com.shuvo.ttit.trkabikha.arraylist;

import android.graphics.Bitmap;

public class PhotoList {
    private String photoName;
    private String uploadDate;
    private String stage;
    private Bitmap image;

    public PhotoList(String photoName, String uploadDate, String stage, Bitmap image) {
        this.photoName = photoName;
        this.uploadDate = uploadDate;
        this.stage = stage;
        this.image = image;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
