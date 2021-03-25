import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.HmacUtils;
import utils.JSONTool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class TestBikaApi {
    public static void main(String[] args) throws IOException {
        //向哔咔发送post请求查询
        //https://picaapi.picacomic.com/comics/advanced-search?page=1
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("keyword","夜王");
        jsonObject.put("sort","dd");
        //把传输数据转成字节数组
        byte[] cache = (jsonObject.toString()).getBytes();
        //开始连接
        URL url=new URL("https://picaapi.picacomic.com/comics/advanced-search?page=1");
        HttpURLConnection connection=(HttpURLConnection) url.openConnection(
                new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",1080)));
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type","application/json;charset=UTF-8");
        connection.setRequestProperty("user-agent","okhttp/3.8.1");
        connection.setRequestProperty("content-length",String.valueOf(cache.length));
        connection.setRequestProperty("api-key","C69BAF41DA5ABD1FFEDC6D2FEA56B");
        connection.setRequestProperty("accept","application/vnd.picacomic.com.v1+json");
        connection.setRequestProperty("accept-encoding","gzip");
        connection.setRequestProperty("app-channel","2");
        connection.setRequestProperty("app-version","2.2.1.3.3.4");
        connection.setRequestProperty("app-uuid","defaultUuid");
        //动态生成
        //jwt:json web token
        connection.setRequestProperty("authorization",
                //header部分
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                        //payload部分
                        "eyJfaWQiOiI1YjU0YmU0MjllZTk2MjY1NTVhYTQ5YTMiLCJlbWFpbCI6IjEyNDY0NTAzMzlxcS5jb20iLCJyb2xlIjoibWVtYmVyIiwibmFtZSI6IuelnuiIrOeahOWFlOWtkCIsInZlcnNpb24iOiIyLjIuMS4zLjMuNCIsImJ1aWxkVmVyc2lvbiI6IjQ1IiwicGxhdGZvcm0iOiJhbmRyb2lkIiwiaWF0IjoxNjEyODg1NjU4LCJleHAiOjE2MTM0OTA0NTh9." +
                        //signature部分:对前两部分的签名,使用HS256算法
                        "M1N8gFZdhBNlQlQsY0o7tpe0G1DiEgqOrf6meIEiSQI");
        //客户端生成的签名:与时间戳、随机数有关
        connection.setRequestProperty("signature","b887501d9f874ff5098593b3fb0baccebe6774f8e9f3ff1351c1d88e7ed901a3");
        //客户端生成的时间戳
        connection.setRequestProperty("time","1612889184");
        //客户端生成的随机数
        connection.setRequestProperty("nonce","fc98091ec9874427a02e567f4d166e15");
        //允许输入输出
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();
        OutputStream out = connection.getOutputStream();
        // 写入请求的字符串
        out.write((jsonObject.toString()).getBytes());
        out.flush();
        out.close();
        System.out.println(connection.getResponseCode());
        // 请求返回的状态
        if (connection.getResponseCode() == 200) {
            System.out.println("连接成功");
            // 请求返回的数据
            InputStream in = connection.getInputStream();
            String a = null;
            try {
                byte[] data = new byte[in.available()];
                in.read(data);
                // 转成字符串
                a = new String(data);
                System.out.println(a);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                in.close();
            }
        } else {
            System.out.println("连接失败");
        }
    }
}
