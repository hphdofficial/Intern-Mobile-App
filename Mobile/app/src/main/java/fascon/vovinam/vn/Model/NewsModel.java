package fascon.vovinam.vn.Model;

public class NewsModel {
    private int id;
    private String tenvi;
    private String noidungvi;
    private String photo;
    private String type;
    private long ngaytao;
    private int id_club; // Thêm trường id_club
    private String link_video;
    private String tenen;

    public String getTenen() {
        return tenen;
    }

    public void setTenen(String tenen) {
        this.tenen = tenen;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenvi() {
        return tenvi;
    }

    public void setTenvi(String tenvi) {
        this.tenvi = tenvi;
    }

    public String getNoidungvi() {
        return noidungvi;
    }

    public void setNoidungvi(String noidungvi) {
        this.noidungvi = noidungvi;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(long ngaytao) {
        this.ngaytao = ngaytao;
    }

    public int getId_club() { // Thêm getter cho id_club
        return id_club;
    }

    public void setId_club(int id_club) { // Thêm setter cho id_club
        this.id_club = id_club;
    }

    public String getLink_video() {
        return link_video;
    }

    public void setLink_video(String link_video) {
        this.link_video = link_video;
    }
}