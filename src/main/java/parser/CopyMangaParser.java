package parser;

import Entity.Comic;
import Entity.ComicChapter;
import Entity.ComicUrl;

import java.net.Proxy;
import java.util.List;

public interface CopyMangaParser {
    //根据关键字查询所有漫画列表
    public List<Comic>getComicList(String keyword, Proxy proxy);
    //根据漫画pathword获得当前漫画所有章节
    public List<ComicChapter>getChapterList(String pathWord,Proxy proxy);
    //根据漫画pathword,章节uuid获取当前章节的所有url
    public List<ComicUrl>getUrlList(String pathWord,String uuid,Proxy proxy);
    //获取漫画简介和封面
    public void getComicBrief(String pathWord,Proxy proxy);
}
