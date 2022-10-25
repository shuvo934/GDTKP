package com.shuvo.ttit.trkabikha.arraylist;

public class PICUserDetails {
    private String user_id;
    private String userName;
    private String user_fname;
    private String user_lname;
    private String email;
    private String contact;
    private String emp_id;
    private String dist_id;
    private String dd_id;
    private String userAccessType;
    private String div_id;
    private String div_name;
    private String dist_name;
    private String dd_name;

    public PICUserDetails(String user_id, String userName, String user_fname, String user_lname, String email, String contact, String emp_id, String dist_id, String dd_id, String userAccessType, String div_id, String div_name, String dist_name, String dd_name) {
        this.user_id = user_id;
        this.userName = userName;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.email = email;
        this.contact = contact;
        this.emp_id = emp_id;
        this.dist_id = dist_id;
        this.dd_id = dd_id;
        this.userAccessType = userAccessType;
        this.div_id = div_id;
        this.div_name = div_name;
        this.dist_name = dist_name;
        this.dd_name = dd_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getDist_id() {
        return dist_id;
    }

    public void setDist_id(String dist_id) {
        this.dist_id = dist_id;
    }

    public String getDd_id() {
        return dd_id;
    }

    public void setDd_id(String dd_id) {
        this.dd_id = dd_id;
    }

    public String getUserAccessType() {
        return userAccessType;
    }

    public void setUserAccessType(String userAccessType) {
        this.userAccessType = userAccessType;
    }

    public String getDiv_id() {
        return div_id;
    }

    public void setDiv_id(String div_id) {
        this.div_id = div_id;
    }

    public String getDiv_name() {
        return div_name;
    }

    public void setDiv_name(String div_name) {
        this.div_name = div_name;
    }

    public String getDist_name() {
        return dist_name;
    }

    public void setDist_name(String dist_name) {
        this.dist_name = dist_name;
    }

    public String getDd_name() {
        return dd_name;
    }

    public void setDd_name(String dd_name) {
        this.dd_name = dd_name;
    }
}
