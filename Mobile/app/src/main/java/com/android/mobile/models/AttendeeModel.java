package com.android.mobile.models;

import java.time.LocalTime;

public class AttendeeModel {
    private LocalTime in;
    private LocalTime out;
    private int id_atg_member;

    public AttendeeModel(int id_atg_member, LocalTime in, LocalTime out) {
        this.id_atg_member = id_atg_member;
        this.in = in;
        this.out = out;
    }

    public AttendeeModel() {
    }

    public int getId_atg_member() {
        return id_atg_member;
    }

    public void setId_atg_member(int id_atg_member) {
        this.id_atg_member = id_atg_member;
    }

    public LocalTime getIn() {
        return in;
    }

    public void setIn(LocalTime in) {
        this.in = in;
    }

    public LocalTime getOut() {
        return out;
    }

    public void setOut(LocalTime out) {
        this.out = out;
    }
}
