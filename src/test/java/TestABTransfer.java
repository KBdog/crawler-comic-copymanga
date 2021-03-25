import java.util.HashMap;
import java.util.Scanner;

/**
 * b站av与bv号互相转换算法
 * 作者:知乎@mcfx
 */
public class TestABTransfer {
    //解码表,一个字符对应一个数字，从0~57
    private static String table = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF";
    private static HashMap<String, Integer> bvMap = new HashMap<>();
    private static HashMap<Integer, String> avMap = new HashMap<>();
//    static int ss[] = {11, 10, 3, 8, 4, 6, 2, 9, 5, 7};
    static int ss[] = {11, 10, 3, 8, 4, 6};
    static long xor = 177451812;
    static long add = 8728348608l;

    public static long power(int a, int range) {
        long power = 1;
        for (int i = 0; i < range; i++)
            power *= a;
        return power;
    }

    //bv号转av号
    public static String bvToav(String bvNum) {
        long r = 0;
        for (int i = 0; i < 58; i++) {
            String tmp = table.substring(i, i + 1);
            bvMap.put(tmp, i);
        }
        for (int i = 0; i < 6; i++) {
            r = r + bvMap.get(bvNum.substring(ss[i], ss[i] + 1)) * power(58, i);
        }
        return "av" + ((r - add) ^ xor);
    }

    //av号转bv号
    public static String avTobv(String avNum) {
        long s = Long.valueOf(avNum.split("av")[1]);
        StringBuffer sb = new StringBuffer("BV1  4 1 7  ");
        s = (s ^ xor) + add;
        for (int i = 0; i < 58; i++) {
            String tmp = table.substring(i, i + 1);
            avMap.put(i, tmp);
        }
        for (int i = 0; i < 6; i++) {
            String r = avMap.get((int) (s / power(58, i) % 58));
            sb.replace(ss[i], ss[i] + 1, r);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        System.out.print("请输入bv号:");
        String bvNum=in.nextLine();
        System.out.println("av号:"+bvToav(bvNum));
    }
}
