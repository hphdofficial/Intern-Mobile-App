package fascon.vovinam.vn.Model;

import java.util.List;
import java.util.Map;

public class AttendanceTeacherModel {
    private int class_id;
    private String class_name;
    private Map<String, List<AttendanceTeacher>> attendance;

    public Map<String, List<AttendanceTeacher>> getAttendance() {
        return attendance;
    }

    public void setAttendance(Map<String, List<AttendanceTeacher>> attendance) {
        this.attendance = attendance;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}

