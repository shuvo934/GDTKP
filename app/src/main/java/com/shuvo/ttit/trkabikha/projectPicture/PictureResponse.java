package com.shuvo.ttit.trkabikha.projectPicture;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PictureResponse {

    @SerializedName("count")
    private String count;

    @SerializedName("items")
    private List<PicInfo> items;

    public static class PicInfo {
        @SerializedName("ud_db_generated_file_name")
        private String ud_db_generated_file_name;

        @SerializedName("image_name")
        private String image_name;

        @SerializedName("ud_date")
        private String ud_date;

        @SerializedName("ud_doc_upload_stage")
        private String ud_doc_upload_stage;

        public String getUd_db_generated_file_name() {
            return ud_db_generated_file_name;
        }

        public void setUd_db_generated_file_name(String ud_db_generated_file_name) {
            this.ud_db_generated_file_name = ud_db_generated_file_name;
        }

        public String getImage_name() {
            return image_name;
        }

        public void setImage_name(String image_name) {
            this.image_name = image_name;
        }

        public String getUd_date() {
            return ud_date;
        }

        public void setUd_date(String ud_date) {
            this.ud_date = ud_date;
        }

        public String getUd_doc_upload_stage() {
            return ud_doc_upload_stage;
        }

        public void setUd_doc_upload_stage(String ud_doc_upload_stage) {
            this.ud_doc_upload_stage = ud_doc_upload_stage;
        }
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<PicInfo> getItems() {
        return items;
    }

    public void setItems(List<PicInfo> items) {
        this.items = items;
    }
}
