package mobilesafe.itheima.com.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import mobilesafe.itheima.com.mobilesafe.service.AddressService;
import mobilesafe.itheima.com.mobilesafe.utils.ServiceUtils;
import mobilesafe.itheima.com.mobilesafe.ui.SetingClickView;
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

    //后台的服务
    private Intent showAddress;

    //设置归属地显示框背景
    private SetingClickView scv_changebg;


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
            //监听来电的服务是开启的
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


        //设置号码归属地的背景
        scv_changebg = (SetingClickView) findViewById(R.id.scv_changebg);
        scv_changebg.setTitle("归属地提示框风格");

        //土司背景样式的名子
        final String[] names = {"半透明", "活力橙", "卫士蓝", "苹果绿"};

        //获取用户的选择
        int which = sp.getInt("which", 0);
        scv_changebg.setDesc(names[which]);


        scv_changebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出一个对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);

                builder.setTitle("归属地提示框风格");

                builder.setSingleChoiceItems(names, sp.getInt("which", 0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //保存选择参数
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putInt("which", which);
                        editor.commit();

                        scv_changebg.setDesc(names[which]);//更改描述信息
                        dialog.dismiss();//关闭对话框
                    }
                });

                builder.setNegativeButton("关闭", null);
                builder.show();
            }
        });


    }


}
