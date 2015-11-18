package mobilesafe.itheima.com.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import mobilesafe.itheima.com.mobilesafe.service.AddressService;
import mobilesafe.itheima.com.mobilesafe.service.ServiceUtils;
import mobilesafe.itheima.com.mobilesafe.ui.SetingItemView;

/**
 * 设置中心
 */
public class SettingActivity extends Activity {

    //自定义控件对象 ：设置是否开启自动更新
    private SetingItemView siv_update;

    //设置是否开启显示 来电归属地
    private SetingItemView siv_show_address;


    //保存用户的设置信息
    private SharedPreferences sp;

    private Intent showAddress;


    /**
     * 当服务停止后，再回来界面时，显示在选中在状态，所在要以这里处理一下
     */
    @Override
    protected void onResume() {
        super.onResume();

        //实例化后台 显示来电归属地的服务
        showAddress = new Intent(this, AddressService.class);

        //检查 来电归属地的服务是否存在
        boolean isServiceRunning = ServiceUtils.isServiceRunning(
                this,
                "mobilesafe.itheima.com.mobilesafe.service.AddressService");

        if (isServiceRunning) {
            //临听来电的服务是开启的
            siv_show_address.setChecked(true);
        } else {
            siv_show_address.setChecked(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //自动更新
        siv_update = (SetingItemView) findViewById(R.id.siv_update);

        sp = getSharedPreferences("config", MODE_PRIVATE);

        boolean update = sp.getBoolean("update", false);
        if (update) {
            //自动更新已经开启
            siv_update.setChecked(true);
        } else {
            //自动更新已经关闭
            siv_update.setChecked(false);
        }


        //复选框 点击事件
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();

                //判断是否选中
                if (siv_update.isCheck()) {
                    //已经打开自动升级
                    siv_update.setChecked(false);
                    editor.putBoolean("update", false);

                } else {
                    //没有打开自动升级
                    siv_update.setChecked(true);
                    editor.putBoolean("update", true);
                }

                editor.commit();
            }
        });


        //来电归属地显示
        siv_show_address = (SetingItemView) findViewById(R.id.siv_show_address);

        //实例化后台 显示来电归属地的服务
        showAddress = new Intent(this, AddressService.class);

        //检查 来电归属地的服务是否存在
        boolean isServiceRunning = ServiceUtils.isServiceRunning(
                this,
                "mobilesafe.itheima.com.mobilesafe.service.AddressService");


        if (isServiceRunning) {
            //监听来电的服务是开启的
            siv_show_address.setChecked(true);
        } else {
            siv_show_address.setChecked(false);
        }


        siv_show_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_show_address.isCheck()) {
                    // 变为非选中状态
                    siv_show_address.setChecked(false);
                    stopService(showAddress);

                } else {
                    // 选择状态
                    siv_show_address.setChecked(true);
                    startService(showAddress);

                }

            }
        });
    }


}
