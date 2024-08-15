package fascon.vovinam.vn.Model;

public class ResetPasswordModel {

    private String email;
    private String otp;
    private String password;
    private String password_confirmation;

    public ResetPasswordModel(String email, String otp, String password, String password_confirmation) {
        this.email = email;
        this.otp = otp;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }
    public ResetPasswordModel() {
    }

    // getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }
}
