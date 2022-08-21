package com.shuvo.ttit.trkabikha.arraylist;

public class CommentList {

    private String pcof_id;
    private String pcof_pcm_id;
    private String commentator;
    private String comm_email;
    private String comment;
    private String comment_time;

    public CommentList(String pcof_id, String pcof_pcm_id, String commentator, String comm_email, String comment, String comment_time) {
        this.pcof_id = pcof_id;
        this.pcof_pcm_id = pcof_pcm_id;
        this.commentator = commentator;
        this.comm_email = comm_email;
        this.comment = comment;
        this.comment_time = comment_time;
    }

    public String getPcof_id() {
        return pcof_id;
    }

    public void setPcof_id(String pcof_id) {
        this.pcof_id = pcof_id;
    }

    public String getPcof_pcm_id() {
        return pcof_pcm_id;
    }

    public void setPcof_pcm_id(String pcof_pcm_id) {
        this.pcof_pcm_id = pcof_pcm_id;
    }

    public String getCommentator() {
        return commentator;
    }

    public void setCommentator(String commentator) {
        this.commentator = commentator;
    }

    public String getComm_email() {
        return comm_email;
    }

    public void setComm_email(String comm_email) {
        this.comm_email = comm_email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }
}
