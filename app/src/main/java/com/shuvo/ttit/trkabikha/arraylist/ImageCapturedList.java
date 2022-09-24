package com.shuvo.ttit.trkabikha.arraylist;

import android.graphics.Bitmap;

public class ImageCapturedList {

    private Bitmap bitmap;
    private String fileName;
    private boolean uploaded;

    public ImageCapturedList(Bitmap bitmap, String fileName, boolean uploaded) {
        this.bitmap = bitmap;
        this.fileName = fileName;
        this.uploaded = uploaded;
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
}
