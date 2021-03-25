package utils;

public class TransferTool {
    //string数组转int数组(string数组内字符要求为整型数字)
    public static int[] stringToInt(String stringArray){
        String s1=stringArray.replaceAll("\\[","");
        String s2=s1.replaceAll("\\]","");
        String[] tmp = s2.split(",");
        int []result=new int[tmp.length];
        for(int i=0;i<result.length;i++){
            result[i]=Integer.parseInt(tmp[i]);
        }
        return result;
    }
}
