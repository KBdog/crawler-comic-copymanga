package crawler;

import properties.RunProperties;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.*;

public class CrawlerComicPipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> mangaList = resultItems.get("mangaList");
        if(mangaList!=null){
            if(!mangaList.isEmpty()){
                DownLoadImg(mangaList,10,500);
            }else {
                System.out.println("漫画列表为空！");
            }
        }
    }

    private void DownLoadImg(final List<String> imgList, final int threadsize, final long sleeptime) {
        int count = 0;
        int size = imgList.size();
        String[] s = imgList.get(0).split("___");
        System.out.println("-当前章节名:"+s[1]);
        System.out.println("--当前漫画页数:"+size);
        //开放多个线程进行并发下载
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadsize);
        CompletionService<String> cs = new ExecutorCompletionService<String>(fixedThreadPool);
        for (String url : imgList) {
            final String url1 = url;
            cs.submit(new Callable<String>() {
                public String call() throws Exception {
                    try {
                        Thread.sleep(sleeptime);
                        //下载图片
                        return down(url1);
                    } catch (InterruptedException e) {
                        System.out.println("线程异常");
                        return "error_" + "url1";
                    }
                }
            });
        }
        //监控下载数
        for (String url : imgList) {
            try {
                String a = cs.take().get();
                if (a != null) {
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (count == size) {
                    System.out.println("---"+count + "/" + size);
                    System.out.println("over");
                    fixedThreadPool.shutdown();
                } else {
                    System.out.println("---"+count + "/" + size);
                }
            }
        }
//        fixedThreadPool.shutdown();
    }

    protected String down(String url) {
//        System.setProperty("https.protocols", RunProperties.HTTPS_PROTOCOLS);
        HttpURLConnection uc =null;
        InputStream is=null;
        BufferedInputStream bis =null;
        BufferedOutputStream bos =null;
        FileOutputStream fos =null;
        try {
            url = url.replace(" ", "");
            //存放爬虫的目录
            File mangas_directory = new File(RunProperties.crawlerDirectory);
            if (!mangas_directory.exists() && !mangas_directory.isDirectory()) {
                mangas_directory.mkdir();
            }
            //存放漫画类别目录
            File manga_directory = new File(RunProperties.crawlerDirectory + url.split("___")[0]);
            if (!manga_directory.exists() && !manga_directory.isDirectory()) {
                manga_directory.mkdir();
            }
            //漫画简介json
            File manga_message_json=new File(RunProperties.crawlerDirectory+url.split("___")[0]
                                    +"/"+"message.json");
            if(!manga_message_json.exists()){
                //创建json文件
                manga_message_json.createNewFile();
                FileOutputStream fileOutputStream=new FileOutputStream(manga_message_json);
                //把字符串写入json文件
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(fileOutputStream,"gbk"));
                bufferedWriter.write(RunProperties.comicBrief);
                bufferedWriter.close();
                fileOutputStream.close();
            }
            //存放漫画单话目录
            File chapters_directory=new File(RunProperties.crawlerDirectory+
                    url.split("___")[0]+"/"+
                    url.split("___")[1]);
            if(!chapters_directory.exists()&&!chapters_directory.isDirectory()){
                chapters_directory.mkdir();
            }
            //存放漫画单话每页图片
            File img=new File(RunProperties.crawlerDirectory+
                    //漫画名
                    url.split("___")[0]+"/"+
                    //标题
                    url.split("___")[1]+"/"+
                    //页码
                    (Integer.parseInt(url.split("___")[3])+1)+".jpg");
            if(!img.exists()){
                img.createNewFile();
            }

            //字节输出流
            fos = new FileOutputStream(img);
            URL temp;
            String imgurl = url.split("___")[2];
            temp = new URL(imgurl.trim());
            uc = (HttpURLConnection) temp.openConnection(RunProperties.proxy);
            uc.setConnectTimeout(2000);
            uc.setReadTimeout(2000);
            //添加header
            uc.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/88.0.4324.104 Safari/537.36");
            //referer
            uc.addRequestProperty("Referer", "https://www.copymanga.com/"+RunProperties.comic.getComicPathWord());
            is = uc.getInputStream();
            //为字节输入流加缓冲
            bis = new BufferedInputStream(is);
            //为字节输出流加缓冲
            bos = new BufferedOutputStream(fos);
            int length;
            byte[] bytes = new byte[1024 * 20];
            while ((length = bis.read(bytes, 0, bytes.length)) != -1) {
                bos.write(bytes, 0, length);
            }
//            System.out.println("---chapter:"+url.split("___")[1]);
//            System.out.println("---url:"+url.split("___")[2]);
//            System.out.println("---page:"+url.split("___")[3]);
            return "success_" + "url1";
        }catch(FileNotFoundException e){
            System.out.println("文件失效_缺页_"+url.split("___")[0]+"_"+url.split("___")[1]
                    +"_"+url.split("___")[2]+"_"+"第"+(Integer.parseInt(url.split("___")[3])+1)+"页");
            return "error_" + "url1";
        }catch (SSLHandshakeException e){
            //出现异常通常是网络问题,此时递归下载把未成功下载的页数重新下载直到成功为止
            System.out.println("TCP连接握手失败_缺页_"+url.split("___")[0]+"_"+url.split("___")[1]
                    +"_"+url.split("___")[2]+"_"+"第"+(Integer.parseInt(url.split("___")[3])+1)+"页");
            return down(url);
        } catch (SocketTimeoutException e){
            //出现异常通常是网络问题,此时递归下载把未成功下载的页数重新下载直到成功为止
            System.out.println("连接超时_缺页_"+url.split("___")[0]+"_"+url.split("___")[1]
                    +"_"+url.split("___")[2]+"_"+"第"+(Integer.parseInt(url.split("___")[3])+1)+"页");
            return down(url);
        } catch (Exception e) {
            System.out.println("未知错误_缺页_"+url.split("___")[0]+"_"+url.split("___")[1]
                    +"_"+url.split("___")[2]+"_"+"第"+(Integer.parseInt(url.split("___")[3])+1)+"页");
            return down(url);
        } finally {
            try {
                if(bos!=null){
                    bos.close();
                }
                if(fos!=null){
                    fos.close();
                }
                if(bis!=null){
                    bis.close();
                }
                if(is!=null){
                    is.close();
                }
                if(uc!=null){
                    uc.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
