package Entity;

public class Comic {
    private String comicPathWord;
    private String comicName;

    public Comic() {
    }

    public Comic(String comicPathWord, String comicName) {
        this.comicPathWord = comicPathWord;
        this.comicName = comicName;
    }

    public String getComicPathWord() {
        return comicPathWord;
    }

    public void setComicPathWord(String comicPathWord) {
        this.comicPathWord = comicPathWord;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }
}
