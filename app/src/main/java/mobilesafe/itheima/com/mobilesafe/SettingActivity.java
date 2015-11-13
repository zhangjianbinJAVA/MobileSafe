package mobilesafe.itheima.com.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import mobilesafe.itheima.com.mobilesafe.ui.SetingItemView;

/**
 * 设置中心
 */
public class SettingActivity extends Activity {

    //自定义控件对象
    private SetingItemView siv_update;

    //保存用户的设置信息
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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


    }


}
