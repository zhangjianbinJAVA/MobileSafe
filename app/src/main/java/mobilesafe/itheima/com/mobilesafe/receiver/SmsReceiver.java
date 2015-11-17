package mobilesafe.itheima.com.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import mobilesafe.itheima.com.mobilesafe.R;
import mobilesafe.itheima.com.mobilesafe.service.GPSService;

/**
 * 短信指令的广播接受者
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";
    private SharedPreferences sp;

    private Context context = null;

    /**
     * 设备策略服务
     */
    private DevicePolicyManager dpm;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        //实例化设备策略服务
        dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);

        // 写接收短信的代码
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        for (Object b : objs) {
            //具体的某一条短信
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) b);

            //发送者
            String sender = sms.getOriginatingAddress();//15555555556
            String safenumber = sp.getString("phone", "");//5556

            //短信的内容
            String body = sms.getMessageBody();

            if (sender.contains(safenumber)) {

                if ("#*location*#".equals(body)) {

                    //得到手机的GPS
                    //启动服务
                    Intent i = new Intent(context, GPSService.class);
                    context.startService(i);

                    //获取维度和经度
                    SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                    String lastlocation = sp.getString("lastlocation", null);

                    //发送短信
                    if (TextUtils.isEmpty(lastlocation)) {
                        //位置没有得到
                        SmsManager.getDefault().sendTextMessage(sender, null, "geting no loaction.....", null, null);
                    } else {
                        SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
                    }

                    //把这个广播终止掉
                    abortBroadcast();

                } else if ("#*alarm*#".equals(body)) {
                    //播放报警影音
                    Log.i(TAG, "播放报警影音");

                    MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                    player.setLooping(false);//不循环播放
                    player.setVolume(1.0f, 1.0f);//左声道和右声道
                    player.start();

                    //把这个广播终止掉
                    abortBroadcast();

                } else if ("#*wipedata*#".equals(body)) {
                    //远程清除数据
                    Log.i(TAG, "远程清除数据");

                    uninstall();
                    abortBroadcast();


                } else if ("#*lockscreen*#".equals(body)) {
                    //远程锁屏
                    Log.i(TAG, "远程锁屏");
                    openAdmin();
                    lockscreen();
                    abortBroadcast();
                }
            }
        }
    }

    /**
     * 用代码去开启管理员
     */
    public void openAdmin() {
        //创建一个Intent
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        //我要激活谁
        ComponentName mDeviceAdminSample = new ComponentName(context.getApplicationContext(), Admin.class);

        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        //劝说用户开启管理员权限
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "哥们开启我可以一键锁屏，你的按钮就不会经常失灵");

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        context.startActivity(intent);
    }

    /**
     * 一键锁屏
     */
    public void lockscreen() {
        ComponentName who = new ComponentName(context, Admin.class);
        if (dpm.isAdminActive(who)) {
            dpm.lockNow();//锁屏
            dpm.resetPassword("123", 0);//设置屏蔽密码

            //清除Sdcard上的数据
            //dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
            //恢复出厂设置
            //dpm.wipeData(0);
        } else {
            Toast.makeText(context, "还没有打开管理员权限", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    /**
     * 卸载当前软件
     */

    public void uninstall() {
        //1.先清除管理员权限
        ComponentName mDeviceAdminSample = new ComponentName(context.getApplicationContext(), Admin.class);
        dpm.removeActiveAdmin(mDeviceAdminSample);
        //2.普通应用的卸载
        Intent intent = new Intent();

        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + context.getPackageName()));

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        context.startActivity(intent);
    }

}
