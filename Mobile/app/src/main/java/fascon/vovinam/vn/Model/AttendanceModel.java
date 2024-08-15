package fascon.vovinam.vn.Model;

import java.util.List;

public class AttendanceModel {

    private String begin_date;
    private String class_name;
    private List<Attendance> attendance;

    public List<Attendance> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<Attendance> attendance) {
        this.attendance = attendance;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public static class Attendance {
        private String date;
        private String day_of_week;
        private String in;
        private String out;
        private String hienDien;

        public Attendance(String date, String day_of_week, String hienDien, String in, String out) {
            this.date = date;
            this.day_of_week = day_of_week;
            this.hienDien = hienDien;
            this.in = in;
            this.out = out;
        }

        public Attendance() {
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDay_of_week() {
            return day_of_week;
        }

        public void setDay_of_week(String day_of_week) {
            this.day_of_week = day_of_week;
        }

        public String getIn() {
            return in;
        }

        public void setIn(String in) {
            this.in = in;
        }

        public String getOut() {
            return out;
        }

        public void setOut(String out) {
            this.out = out;
        }

        public String getHienDien() {
            return hienDien;
        }

        public void setHienDien(String hienDien) {
            this.hienDien = hienDien;
        }
    }
}
