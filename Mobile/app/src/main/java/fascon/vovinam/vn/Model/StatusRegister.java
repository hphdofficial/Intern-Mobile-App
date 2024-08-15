package fascon.vovinam.vn.Model;

import com.google.gson.annotations.SerializedName;

public class StatusRegister {
    @SerializedName("status")
    private String status;
    @SerializedName("member_id")
    private String member_id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }
}
