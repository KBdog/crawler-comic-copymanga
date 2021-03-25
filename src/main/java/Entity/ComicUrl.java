package Entity;

public class ComicUrl {
    //页数
    private int urlId;
    //当前页url
    private String urlAddress;

    public ComicUrl(int urlId, String urlAddress) {
        this.urlId = urlId;
        this.urlAddress = urlAddress;
    }

    public ComicUrl() {
    }

    public int getUrlId() {
        return urlId;
    }

    public void setUrlId(int urlId) {
        this.urlId = urlId;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }
}
