package fascon.vovinam.vn.Model;

public class ReviewModel {
    private int ReviewID;
    private int ProductID;
    private String RatingValue; // Giá trị chuỗi cho API trả về và gửi lên
    private int RatingCount;
    private String ReviewDate;
    private String ReviewContent;
    private int id_atg_members;
    private String userName;
    private String avatarUrl;

    // Getters and Setters
    public int getReviewID() {
        return ReviewID;
    }

    public void setReviewID(int reviewID) {
        ReviewID = reviewID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getRatingValue() {
        return RatingValue;
    }

    public void setRatingValue(String ratingValue) {
        RatingValue = ratingValue;
    }

    public int getRatingCount() {
        return RatingCount;
    }

    public void setRatingCount(int ratingCount) {
        RatingCount = ratingCount;
    }

    public String getReviewDate() {
        return ReviewDate;
    }

    public void setReviewDate(String reviewDate) {
        ReviewDate = reviewDate;
    }

    public String getReviewContent() {
        return ReviewContent;
    }

    public void setReviewContent(String reviewContent) {
        ReviewContent = reviewContent;
    }

    public int getId_atg_members() {
        return id_atg_members;
    }

    public void setId_atg_members(int id_atg_members) {
        this.id_atg_members = id_atg_members;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
