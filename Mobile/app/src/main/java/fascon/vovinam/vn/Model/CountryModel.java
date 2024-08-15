package fascon.vovinam.vn.Model;

public class CountryModel {
    private int id;
    private String ten;
    private String map_lat;
    private String map_long;
    private int zoom;
    private String tenkhongdau;
    private String matp;
    private int stt;
    private int hienthi;
    private long ngaytao;
    private long ngaysua;
    private int gia;

    public CountryModel() {
    }

    public CountryModel(int id, String ten, String map_lat, String map_long) {
        this.id = id;
        this.ten = ten;
        this.map_lat = map_lat;
        this.map_long = map_long;
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

    public String getMap_lat() {
        return map_lat;
    }

    public void setMap_lat(String map_lat) {
        this.map_lat = map_lat;
    }

    public String getMap_long() {
        return map_long;
    }

    public void setMap_long(String map_long) {
        this.map_long = map_long;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public String getTenkhongdau() {
        return tenkhongdau;
    }

    public void setTenkhongdau(String tenkhongdau) {
        this.tenkhongdau = tenkhongdau;
    }

    public String getMatp() {
        return matp;
    }

    public void setMatp(String matp) {
        this.matp = matp;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public int getHienthi() {
        return hienthi;
    }

    public void setHienthi(int hienthi) {
        this.hienthi = hienthi;
    }

    public long getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(long ngaytao) {
        this.ngaytao = ngaytao;
    }

    public long getNgaysua() {
        return ngaysua;
    }

    public void setNgaysua(long ngaysua) {
        this.ngaysua = ngaysua;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    @Override
    public String toString() {
        return ten;
    }
}