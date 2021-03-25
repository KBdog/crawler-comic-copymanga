package utils;

import java.io.*;

public class IOTool {
    //关闭所有字符输入流
    public static void closeAllReader(Reader...readers){
        try {
            for(Reader object:readers){
                if(object!=null){
                    object.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //关闭所有字符输出流
    public static void closeAllWriter(Writer...writers){
        try {
            for(Writer object:writers){
                if(object!=null){
                    object.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //关闭所有字节输入流
    public static void closeAllInputStream(InputStream...inputStreams){
        try {
            for(InputStream object:inputStreams){
                if(object!=null){
                    object.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //关闭所有字节输出流
    public static void closeAllOutputStream(OutputStream...outputStreams){
        try {
            for(OutputStream object:outputStreams){
                if(object!=null){
                    object.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
