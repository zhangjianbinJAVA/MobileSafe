package mobilesafe.itheima.com.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhangjb on 2015/11/11.
 */
public class StreamTools {

    /**
     * 将 输入流中的 数据 转为字符串形式
     *
     * @param in 输入流
     * @return 返回的字件串
     * @throws IOException
     */
    public static String readFromStream(InputStream in) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;

        while ((len = in.read(buffer)) != -1) {
            baos.write(buffer, 0, buffer.length);
        }

        in.close();
        String result = baos.toString();
        baos.close();

        return result;
    }


}
