package mobilesafe.itheima.com.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

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

            //Toast.makeText(context, address, Toast.LENGTH_SHORT).show();
            myToast(address);
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

                    //Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
                    myToast(address);
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


    private WindowManager.LayoutParams params;
    private SharedPreferences sp;

    //定义需要点击的次数
    long[] mHits = new long[2];

    /**
     * 自定义土司
     */
    public void myToast(String address) {

        //实例化布局
        view = View.inflate(this, R.layout.address_show, null);
        TextView textview = (TextView) view.findViewById(R.id.tv_address);

        //土司的 背景色资源
        int[] ids = {
                R.drawable.button_pressed,
                R.drawable.button_pressed,
                R.drawable.button_pressed,
                R.drawable.button_pressed
        };

        //根据用户的选择，设置土司的背景图片
        sp = getSharedPreferences("config", MODE_PRIVATE);
        view.setBackgroundResource(ids[sp.getInt("which", 0)]);
        textview.setText(address);


        //单击事件 ：双击这个控件，自动在窗体局中显示
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拷贝数组
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);

                //记录cup开机的时间
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();

                //判断是否在规定的时间内 连续点击了多少次数
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    // 双击居中了。。。
                    params.x = wm.getDefaultDisplay().getWidth() / 2 - view.getWidth() / 2;

                    wm.updateViewLayout(view, params);
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putInt("lastX", params.x);
                    editor.commit();
                }
            }});

            //给view 对象设置一个触摸的监听器
            view.setOnTouchListener(new View.OnTouchListener(){
                int startX;
                int startY;

                @Override
                public boolean onTouch (View v, MotionEvent event){

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        //新的位置
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();

                        //偏移量
                        int dx = newX - startX;
                        int dy = newY - startY;

                        //更新 imageview 在窗体上的位置
                        //指定窗体距离左边 上边 的位置
                        params.x += dx;
                        params.y += dy;

                        //考虑边界问题
                        if (params.x < 0) {
                            params.x = 0;
                        }

                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > wm.getDefaultDisplay().getWidth() - view.getWidth()) {
                            params.x = wm.getDefaultDisplay().getWidth() - view.getWidth();
                        }
                        if (params.y > wm.getDefaultDisplay().getHeight() - view.getHeight()) {
                            params.y = wm.getDefaultDisplay().getWidth() - view.getWidth();
                        }

                        //更新布局的位置
                        wm.updateViewLayout(view, params);

                        //重新初始化手指的开始位置
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:

                        //记录土司 距离屏幕上边和上边的位置
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("lastX", params.x);
                        editor.putInt("lastY", params.y);
                        editor.commit();
                        break;
                }

                return false;//return true :事件处理完毕了，不要让父控件 父布局响应触摸事件了：也就是说，事件到这里就结束了
            }});


            //窗体的参数就设置好了
            params=new WindowManager.LayoutParams();
            params.height=WindowManager.LayoutParams.WRAP_CONTENT;
            params.width=WindowManager.LayoutParams.WRAP_CONTENT;

            //窗体的对齐方式：显示在左上角
            params.gravity=Gravity.TOP+Gravity.LEFT;

            //指定窗体距离左边100  上边100个像素
            params.x=sp.getInt("lastX",100);
            params.y=sp.getInt("lastY",100);

            params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            params.format=PixelFormat.TRANSLUCENT;
            params.type=WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;

            //视图加到窗体上
            wm.addView(view,params);
        }


        @Override
        public void onDestroy () {
            super.onDestroy();
            // 取消监听来电
            tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
            listenerPhone = null;

            //用代码取消注册广播接收者
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
