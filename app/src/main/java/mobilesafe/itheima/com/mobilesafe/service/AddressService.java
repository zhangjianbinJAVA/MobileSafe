package mobilesafe.itheima.com.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import mobilesafe.itheima.com.mobilesafe.R;
import mobilesafe.itheima.com.mobilesafe.db.NumberAddressQueryUtils;

/**
 * 来电号码归属地的显示 后台监听服务
 */
public class AddressService extends Service {

    /**
     * 窗体管理者
     */
    private WindowManager wm;

    /**
     * 用于实例化自定义 土司 的布局文件 view 对象
     */
    private View view;

    /**
     * 电话服务
     */
    private TelephonyManager tm;

    /**
     * 来电 电话状态监听 处理事件
     */
    private MyListenerPhone listenerPhone;

    /**
     * 外拨电话号码的 广播接收处理者
     */
    private OutCallReceiver receiver;

    public AddressService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        // 监听来电
        listenerPhone = new MyListenerPhone();
        tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);

        //用代码去注册广播接收者
        receiver = new OutCallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(receiver, filter);

        //实例化窗体
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    // 服务里面的内部类
    //广播接收者的生命周期和服务一样
    class OutCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 这就是我们拿到的播出去的电话号码
            String phone = getResultData();

            // 查询数据库
            String address = NumberAddressQueryUtils.queryNumber(phone);

            Toast.makeText(context, address, Toast.LENGTH_SHORT).show();
            //myToast(address);
        }

    }

    /**
     * 来电 监听的电话 处理事件
     */
    private class MyListenerPhone extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // state：状态，incomingNumber：来电号码
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://有电话时， 来电铃声响起

                    // 查询数据库的操作：查询来电的归属地
                    String address = NumberAddressQueryUtils
                            .queryNumber(incomingNumber);

                    Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
                    // myToast(address);
                    break;

                case TelephonyManager.CALL_STATE_IDLE://电话的空闲状态：挂电话、来电拒绝
                    //把这个View移除
                    if (view != null) {
                        wm.removeView(view);
                    }
                    break;

                default:
                    break;
            }
        }

    }


    /**
     * 自定义土司
     */
    public void myToast(String address) {

        //实例化布局
        view = View.inflate(this, R.layout.address_show, null);

        TextView textview = (TextView) view.findViewById(R.id.tv_address);

        //土司的 背景色资源
        int[] ids = {R.drawable.button_pressed};

        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);

        //根据用户的选择，设置土司的背景图片
        view.setBackgroundResource(ids[sp.getInt("which", 0)]);
        textview.setText(address);

        //窗体的参数就设置好了
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
       // wm.addView(view, params);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消监听来电
        tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
        listenerPhone = null;

        //用代码取消注册广播接收者
        unregisterReceiver(receiver);
        receiver = null;
    }
}
