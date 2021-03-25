import parser.CopyMangaParser;
import parser.impl.CopyMangaParserImpl;
import properties.RunProperties;


public class TestGetMangaBrief {
    public static void main(String[] args) {
        CopyMangaParser parser=new CopyMangaParserImpl();
        parser.getComicBrief("wuzhizhuansheng", RunProperties.proxy);
    }
}
