package mobilesafe.itheima.com.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 服务的工具类
 */
public class ServiceUtils {

    /**
     * 校验某个服务是否还活着
     * serviceName :传进来的服务的名称
     * <p/>
     * ActivityManager :activity 管理者，可以管理activity ,也可以管理服务
     */

    public static boolean isServiceRunning(Context context, String serviceName) {
        //校验服务是否还活着

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        //activity 管理者里 可以装多少个服务
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo info : infos) {
            //获取服务的名子
            String name = info.service.getClassName();

            //判断服务是否存在
            if (serviceName.equals(name)) {
                return true;
            }
        }
        return false;
    }


}
