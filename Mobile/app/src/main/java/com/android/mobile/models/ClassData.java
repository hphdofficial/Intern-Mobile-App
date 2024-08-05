package com.android.mobile.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ClassData {
    @SerializedName("class_id")
    private int classId;

    @SerializedName("class_name")
    private String className;

    @SerializedName("attendance")
    private Map<String, List<AttendanceTeacher>> attendance;

    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public Map<String, List<AttendanceTeacher>> getAttendance() {
        return attendance;
    }
}
