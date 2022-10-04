package Utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class md5CalcUtil {

    public static String md5Calc(String str) {
        try {
            // 创建具有MD5算法的信息摘要
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
            byte[] bytes = md5.digest(str.getBytes());
            // 将得到的字节数组变成十六进制字符串返回
            BigInteger bI = new BigInteger(1, bytes);    // 转换为正大整数
            return bI.toString(16);     // 转换为十六进制返回
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
