package com.android.mobile.models;

import java.util.Date;

public class CheckinMemberModel {
    private Date start_date;
    private Date end_date;
    private String access_token;

    public CheckinMemberModel(String access_token, Date end_date, Date start_date) {
        this.access_token = access_token;
        this.end_date = end_date;
        this.start_date = start_date;
    }

    public CheckinMemberModel() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }
}
