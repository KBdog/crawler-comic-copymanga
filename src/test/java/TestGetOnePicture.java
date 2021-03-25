import Entity.ComicUrl;
import parser.CopyMangaParser;
import parser.impl.CopyMangaParserImpl;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

public class TestGetOnePicture {
    public static void main(String[] args) {
        Proxy proxy=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",1080));
        CopyMangaParser parser=new CopyMangaParserImpl();
        List<ComicUrl> list = parser.getUrlList("tayutadexuanze", "5847ba3c-88ae-11ea-90aa-00163e0ca5bd", proxy);
        ComicUrl url=list.get(0);
        System.out.println(url.getUrlAddress());
    }
}
