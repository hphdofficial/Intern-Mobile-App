package com.android.mobile.models;

import java.util.ArrayList;
import java.util.Date;

public class CheckinTeacherModel {
    private int id_class;
    private Date date;
    private ArrayList<AttendeeModel> AttendeeList;

    public CheckinTeacherModel(ArrayList<AttendeeModel> attendeeList, Date date, int id_class) {
        AttendeeList = attendeeList;
        this.date = date;
        this.id_class = id_class;
    }

    public CheckinTeacherModel() {
    }

    public ArrayList<AttendeeModel> getAttendeeList() {
        return AttendeeList;
    }

    public void setAttendeeList(ArrayList<AttendeeModel> attendeeList) {
        AttendeeList = attendeeList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId_class() {
        return id_class;
    }

    public void setId_class(int id_class) {
        this.id_class = id_class;
    }
}
