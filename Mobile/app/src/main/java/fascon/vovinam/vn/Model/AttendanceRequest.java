package fascon.vovinam.vn.Model;

import java.util.List;

public class AttendanceRequest {
    private int id_class;
    private String date;
    private List<Attendee> attendees;

    // Getters and Setters

    public int getId_class() {
        return id_class;
    }

    public void setId_class(int id_class) {
        this.id_class = id_class;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public static class Attendee {
        private int id_atg_member;
        private String in;
        private String out;

        // Getters and Setters

        public int getId_atg_member() {
            return id_atg_member;
        }

        public void setId_atg_member(int id_atg_member) {
            this.id_atg_member = id_atg_member;
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
    }
}
