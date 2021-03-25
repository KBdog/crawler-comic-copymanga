package parser.impl;

import Entity.Comic;
import Entity.ComicChapter;
import Entity.ComicUrl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import parser.CopyMangaParser;
import properties.RunProperties;
import utils.IOTool;
import utils.JSONTool;
import utils.TransferTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CopyMangaParserImpl implements CopyMangaParser {


    @Override
    public List<Comic> getComicList(String keyword, Proxy proxy) {
//        System.setProperty("https.protocols", RunProperties.HTTPS_PROTOCOLS);
        URL url= null;
        HttpURLConnection connection= null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        List<Comic>comicList=null;
        try {
            url = new URL("https://api.copymanga.com/api/v3/search/comic" +
                    "?format=json&limit=18&offset=0&platform=3&q="+keyword);
            connection = (HttpURLConnection) url.openConnection(proxy);
            connection.connect();
            System.out.println("返回状态码:"+connection.getResponseCode());
            if(connection.getResponseCode()==200){
                System.out.println("状态码正确");
                isr=new InputStreamReader(connection.getInputStream());
                br=new BufferedReader(isr);
                StringBuffer sb=new StringBuffer();
                int length=0;
                char[]cache=new char[1024*20];
                while((length=br.read(cache,0,cache.length))!=-1){
                    sb.append(cache,0,length);
                }
                JSONObject jsonObject=JSONObject.parseObject(sb.toString());
                //获得results
                JSONObject results= JSONTool.getJSONObject("results",jsonObject);
                //获得list数组
                JSONArray list = JSONTool.getJSONArray("list", results);
                if(list!=null){
                    comicList=new ArrayList<>();
                    for(int i=0;i<list.size();i++){
                        JSONObject comicObject = list.getJSONObject(i);
                        Comic comic=new Comic();
                        //漫画名去空格
                        comic.setComicName(comicObject.getString("name").replaceAll(" ",""));
                        comic.setComicPathWord(comicObject.getString("path_word"));
                        comicList.add(comic);
                    }
                }else {
                    System.out.println("查询列表为空");
                }
            }else {
                System.out.println("状态码错误"+connection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            //关闭流
            IOTool.closeAllReader(br,isr);
            if(connection!=null){
                connection.disconnect();
            }
            return comicList;
        }
    }

    @Override
    public List<ComicChapter> getChapterList(String pathWord, Proxy proxy) {
//        System.setProperty("https.protocols", RunProperties.HTTPS_PROTOCOLS);
        URL url= null;
        HttpURLConnection connection=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        List<ComicChapter>chapterList=null;
        try {
            url = new URL("https://api.copymanga.com/api/v3/comic/"+pathWord+
                    "/group/default/chapters?limit=500&offset=0&platform=3");
            connection=(HttpURLConnection)url.openConnection(proxy);
            connection.connect();
            if(connection.getResponseCode()==200){
                isr=new InputStreamReader(connection.getInputStream());
                br=new BufferedReader(isr);
                StringBuffer sb=new StringBuffer();
                int length=0;
                char[]cache=new char[1024];
                while((length=br.read(cache,0,cache.length))!=-1){
                    sb.append(cache,0,length);
                }
                JSONObject jsonObject = JSONObject.parseObject(sb.toString());
                //获得results
                JSONObject results= JSONTool.getJSONObject("results",jsonObject);
                //获得list数组
                JSONArray list = JSONTool.getJSONArray("list", results);
                chapterList=new ArrayList<>();
                for(int i=0;i<list.size();i++){
                    JSONObject chapterObject = list.getJSONObject(i);
                    ComicChapter chapter=new ComicChapter();
                    chapter.setChapterId(chapterObject.getString("uuid"));
                    chapter.setChapterName(chapterObject.getString("name"));
                    chapterList.add(chapter);
                }
            }else {
                System.out.println("状态码错误:"+connection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOTool.closeAllReader(br,isr);
            if(connection!=null){
                connection.disconnect();
            }
            return chapterList;
        }
    }

    @Override
    public List<ComicUrl> getUrlList(String pathWord, String uuid, Proxy proxy) {
//        System.setProperty("https.protocols", RunProperties.HTTPS_PROTOCOLS);
        URL url= null;
        HttpURLConnection connection=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        List<ComicUrl>comicUrlList=null;
        try {
            url = new URL("https://api.copymanga.com/api/v3/comic/"+pathWord+"/chapter2/"+uuid+"?platform=3");
            connection=(HttpURLConnection)url.openConnection(proxy);
            connection.connect();
            if(connection.getResponseCode()==200){
                isr=new InputStreamReader(connection.getInputStream());
                br=new BufferedReader(isr);
                StringBuffer sb=new StringBuffer();
                int length=0;
                char[]cache=new char[1024];
                while((length=br.read(cache,0,cache.length))!=-1){
                    sb.append(cache,0,length);
                }
                JSONObject jsonObject = JSONObject.parseObject(sb.toString());
                //获得results
                JSONObject results= JSONTool.getJSONObject("results",jsonObject);
                //获得chapter
                JSONObject chapter= JSONTool.getJSONObject("chapter",results);
                //从chapter中获得words
                JSONArray words = chapter.getJSONArray("words");
                //把words转成int数组
                int[] wordList = TransferTool.stringToInt(words.toString());
                //从chapter中获得contents
                JSONArray contents = chapter.getJSONArray("contents");
                comicUrlList=new ArrayList<>();
                for(int i=0;i<contents.size();i++){
                    JSONObject urlObject = contents.getJSONObject(i);
                    ComicUrl comicUrl=new ComicUrl();
                    //把页数对应上的url并入实体类
                    comicUrl.setUrlId(wordList[i]);
                    comicUrl.setUrlAddress(urlObject.getString("url"));
                    comicUrlList.add(comicUrl);
                }
                //根据id升序排序
                comicUrlList.sort(Comparator.comparing(ComicUrl::getUrlId));
            }else {
                System.out.println("状态码错误:"+connection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOTool.closeAllReader(br,isr);
            if(connection!=null){
                connection.disconnect();
            }
            return comicUrlList;
        }
    }

    //漫画简介和封面
    @Override
    public void getComicBrief(String pathWord, Proxy proxy) {
        URL url=null;
        HttpURLConnection connection=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        try {
            url=new URL("https://api.copymanga.com/api/v3/comic2/"+pathWord+"?platform=3");
            connection=(HttpURLConnection) url.openConnection(proxy);
            if(connection.getResponseCode()==200){
                isr=new InputStreamReader(connection.getInputStream());
                br=new BufferedReader(isr);
                StringBuffer sb=new StringBuffer();
                int length=0;
                char[]cache=new char[1024];
                while((length=br.read(cache,0,cache.length))!=-1){
                    sb.append(cache,0,length);
                }
                JSONObject jsonObject = JSONObject.parseObject(sb.toString());
                JSONObject results=jsonObject.getJSONObject("results");
                JSONObject comic=results.getJSONObject("comic");
                RunProperties.comicCover=comic.getString("cover");
                RunProperties.comicBriefString=comic.getString("brief");
                JSONObject comicBrief=new JSONObject();
                comicBrief.put("comic_cover",RunProperties.comicCover);
                comicBrief.put("comic_description",RunProperties.comicBriefString);
                RunProperties.comicBrief=JSONTool.formatJSON(comicBrief);
//                System.out.println(RunProperties.comicBrief);
            }
        } catch (IOException e) {
            //出错循环执行
            getComicBrief(pathWord,proxy);
        }finally {
            try {
                if(br!=null){
                    br.close();
                }
                if(isr!=null){
                    isr.close();
                }
                if(connection!=null){
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
