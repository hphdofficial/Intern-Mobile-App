package fascon.vovinam.vn.Model;

public class ArticleModel {
    private String title;
    private String content;

    public ArticleModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
