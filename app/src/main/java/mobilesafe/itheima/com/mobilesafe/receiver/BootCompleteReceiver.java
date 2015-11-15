package mobilesafe.itheima.com.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * 接收系统发出的广播
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    private SharedPreferences sp;
    private TelephonyManager tm;

    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //读取之前保存的sim信息
        sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        String sim = sp.getString("sim", "");

        //读取当前的 sim 卡信息，比较是否一样
        tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String realSim = tm.getSimSerialNumber();

        if (!sim.equals(realSim)) {
            //sim 已经变更，发送一个短信给安全号码
            Toast.makeText(context, "已变更", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "没有变更", Toast.LENGTH_SHORT).show();
        }

    }
}
