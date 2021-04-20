import Entity.ComicUrl;
import parser.CopyMangaParser;
import parser.impl.CopyMangaParserImpl;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
对url进行排序
 */
public class TestSortUrl {
    public static void main(String[] args) {
        Proxy proxy=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",1080));
        CopyMangaParser parser=new CopyMangaParserImpl();
        List<ComicUrl> list = parser.getUrlList("tayutadexuanze", "5847ba3c-88ae-11ea-90aa-00163e0ca5bd", proxy);
        //正序
//        list.sort(Comparator.comparing(ComicUrl::getUrlId));
        //倒序
        list.sort(Comparator.comparing(ComicUrl::getUrlId).reversed());
        for(ComicUrl url:list){
            System.out.println(url.getUrlId()+"--"+url.getUrlAddress());
        }
    }
}
