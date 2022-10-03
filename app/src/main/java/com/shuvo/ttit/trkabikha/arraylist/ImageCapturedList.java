package com.shuvo.ttit.trkabikha.arraylist;

import android.graphics.Bitmap;

public class ImageCapturedList {

    private Bitmap bitmap;
    private String fileName;
    private boolean uploaded;
    private String stage;

    public ImageCapturedList(Bitmap bitmap, String fileName, boolean uploaded,String stage) {
        this.bitmap = bitmap;
        this.fileName = fileName;
        this.uploaded = uploaded;
        this.stage = stage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
