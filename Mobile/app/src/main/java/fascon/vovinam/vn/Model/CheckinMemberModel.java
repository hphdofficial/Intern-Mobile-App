package fascon.vovinam.vn.Model;

public class CheckinMemberModel {
    private int id;
    private String ten;
    private String begin_date;
    private boolean isChecked;
    private boolean ableCheck;

    public CheckinMemberModel(boolean ableCheck, String begin_date, int id, boolean isChecked, String ten) {
        this.ableCheck = false;
        this.begin_date = begin_date;
        this.id = id;
        this.isChecked = false;
        this.ten = ten;
    }

    public CheckinMemberModel() {
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public boolean isAbleCheck() {
        return ableCheck;
    }

    public void setAbleCheck(boolean ableCheck) {
        this.ableCheck = ableCheck;
    }
}
