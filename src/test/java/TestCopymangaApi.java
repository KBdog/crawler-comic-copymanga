import Entity.Comic;
import Entity.ComicChapter;
import Entity.ComicUrl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import utils.IOTool;
import utils.JSONTool;
import utils.TransferTool;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/*
测试拷贝漫画api
关键字搜索:https://api.copymanga.com/api/v3/search/comic?format=json&limit=18&offset=0&platform=3&q=${keyword}
漫画总章节:https://api.copymanga.com/api/v3/comic/${path_word}/group/default/chapters?limit=500&offset=0&platform=3
章节图片信息:https://api.copymanga.com/api/v3/comic/${path_word}/chapter2/${uuid}?platform=3
 */
public class TestCopymangaApi {
    public static void main(String[] args) throws IOException {
        Proxy proxy=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",1080));
//        queryComicList("他与她的选择",proxy);
//        getChapterList("tayutadexuanze",proxy);
        getUrlList("tayutadexuanze","5847ba3c-88ae-11ea-90aa-00163e0ca5bd",proxy);
    }

    public static void queryComicList(String keyword,Proxy proxy) {
        URL url= null;
        HttpURLConnection connection= null;
        InputStreamReader isr=null;
        BufferedReader br=null;
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
//        System.out.println(JSONTool.formatJSON(jsonObject));
                //获得results
                JSONObject results= JSONTool.getJSONObject("results",jsonObject);
                //获得list数组
                JSONArray list = JSONTool.getJSONArray("list", results);
                List<Comic>comicList=new ArrayList<>();
                for(int i=0;i<list.size();i++){
                    JSONObject comicObject = list.getJSONObject(i);
                    Comic comic=new Comic();
                    //漫画名去空格
                    comic.setComicName(comicObject.getString("name").replaceAll(" ",""));
                    comic.setComicPathWord(comicObject.getString("path_word"));
                    comicList.add(comic);
                }
                for(Comic tmp:comicList){
                    System.out.println(tmp.getComicName()+"-"+tmp.getComicPathWord());
                }

            }else {
                System.out.println("状态码错误"+connection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭流
            IOTool.closeAllReader(br,isr);
            if(connection!=null){
                connection.disconnect();
            }
        }
    }

    public static void getChapterList(String pathWord,Proxy proxy) {
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
                for(ComicChapter chapter: chapterList){
                    System.out.println(chapter.getChapterName()+"-"+chapter.getChapterId());
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
        }

    }

    public static void getUrlList(String pathWord,String uuid,Proxy proxy){
        URL url= null;
        HttpURLConnection connection=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
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
                List<ComicUrl>comicUrlList=new ArrayList<>();
                for(int i=0;i<contents.size();i++){
                    JSONObject urlObject = contents.getJSONObject(i);
                    ComicUrl comicUrl=new ComicUrl();
                    //把页数对应上的url并入实体类
                    comicUrl.setUrlId(wordList[i]);
                    comicUrl.setUrlAddress(urlObject.getString("url"));
                    comicUrlList.add(comicUrl);
                }
                for(ComicUrl comicUrl:comicUrlList){
                    System.out.println(comicUrl.getUrlId()+"-"+comicUrl.getUrlAddress());
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
        }
    }
}
