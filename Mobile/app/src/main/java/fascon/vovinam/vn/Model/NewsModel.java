package fascon.vovinam.vn.Model;

public class NewsModel {
    private int id;
    private String tenvi;
    private String noidungvi;
    private String photo;
    private String type; // Thêm trường type
    private long ngaytao;

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

    public String getType() { // Thêm getter cho type
        return type;
    }

    public void setType(String type) { // Thêm setter cho type
        this.type = type;
    }

    public long getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(long ngaytao) {
        this.ngaytao = ngaytao;
    }
}
