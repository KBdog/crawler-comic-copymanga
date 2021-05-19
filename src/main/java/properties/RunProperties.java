package properties;

import Entity.Comic;
import Entity.ComicChapter;
import parser.CopyMangaParser;
import us.codecraft.webmagic.Spider;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
/*
1、获得comic
2、获得全局的chapterList
3、获得chapterlist的size
4、当标志数小于chapterlist_size时在process中根据标志数给chapter赋值
5、根据chapter的id获取当前章节url集合
6、把url集合放入field传递给pipeline处理
 */
public class RunProperties {
    public static Spider spider=null;
    public static String crawlerDirectory="D:/Comic/copymanga/";
    public static Comic comic=null;
    public static List<ComicChapter>chapterList=null;
    public static int flag=0;
    public static int chapterList_size=0;
    public static CopyMangaParser parser=null;
    public static Proxy proxy=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",1080));
    public final static String HTTPS_PROTOCOLS="TLSv1,TLSv1.1,TLSv1.2";
    //漫画简介信息
    public static String comicCover=null;
    public static String comicBrief=null;
    public static String comicBriefString=null;
}
