package Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class aesUtil {

    public static byte[] encrypt(byte[] srcBytes, String key) {
        try{
            key = expandTo16(key);
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            return cipher.doFinal(srcBytes);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt(byte[] srcBytes, String key) {
        try {
            key = expandTo16(key);
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            return cipher.doFinal(srcBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 扩充密码至16位
    public static String expandTo16(String key) {
        if (key.length() != 16) {
            int quotient = 16 / key.length();
            int remainder = 16 % key.length();

            for (int i = 0; i < quotient - 1; i++) {
                key += key;
            }

            key += key.substring(0, remainder);
        }
        return key;
    }
}
