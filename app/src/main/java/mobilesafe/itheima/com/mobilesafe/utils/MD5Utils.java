package mobilesafe.itheima.com.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangjb on 2015/11/13.
 */
public class MD5Utils {


    public static String getMD5Password(String password) {
        StringBuffer buffer = new StringBuffer();
        MessageDigest digest = null;
        try {

            digest = MessageDigest.getInstance("md5");

            byte[] result = digest.digest(password.getBytes());

            for (byte b : result) {
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {

                    buffer.append("0");
                }

                buffer.append(number);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
