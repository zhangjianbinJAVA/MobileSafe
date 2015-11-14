package mobilesafe.itheima.com.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 手机防盗页面
 */
public class LostFindActivity extends AppCompatActivity {

    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 判断一下，是否做过设置向导，如果没有做过，就跳转到设置向导页里去设置
         * ，否则就留在当前的页面
         *
         */
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = sp.getBoolean("configed", false);

        if (configed) {
            setContentView(R.layout.activity_lost_find);
        } else {
            //还没有做过设置向导
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);

            //关闭当前页面
            finish();
        }

    }

    /**
     * 重新进入设置向导页面
     *
     * @param view
     */
    public void reEnterSetup(View view) {
        //还没有做过设置向导
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);

        //关闭当前页面
        finish();
    }

}
