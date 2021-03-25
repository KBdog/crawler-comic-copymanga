package crawler;

import Entity.ComicChapter;
import Entity.ComicUrl;
import properties.RunProperties;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class CrawlerComicProcessor implements PageProcessor {
    private Site site = Site.me().setCycleRetryTimes(3000).setCharset("UTF-8");
    @Override
    public void process(Page page) {
        if(RunProperties.flag<RunProperties.chapterList_size){
            //设置无关请求只为让processor继续运行
            Request request=new Request("https://www.baidu.com/s?wd="+RunProperties.flag);
            request.addHeader("Cookie", "display_mode=1");
            page.addTargetRequest(request);
            //开始对数据进行处理
            ComicChapter chapter =RunProperties.chapterList.get(RunProperties.flag);
            List<ComicUrl> urlList = RunProperties.parser.getUrlList(RunProperties.comic.getComicPathWord(), chapter.getChapterId(), RunProperties.proxy);
            //需要传递过pipeline的图片集合
            List<String>mangaList=null;
            if(urlList!=null){
                mangaList=new ArrayList<>();
                for(ComicUrl comicUrl:urlList){
                    //漫画名___章节名___章节图片url___章节图片页码
                    //防止出现通配符?
                    String url=RunProperties.comic.getComicName().replaceAll("\\?","？")+"___"+
                            chapter.getChapterName().replaceAll("\\?","？")+"___"+
                            comicUrl.getUrlAddress()+"___"+comicUrl.getUrlId();
                    mangaList.add(url);
                }
                page.putField("mangaList",mangaList);
            }else{
                System.out.println("该章列表为空:"+chapter.getChapterName());
            }
        }
        RunProperties.flag++;
    }

    @Override
    public Site getSite() {
        return site;
    }
}
