package mobilesafe.itheima.com.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import mobilesafe.itheima.com.mobilesafe.ui.SetingItemView;

/**
 * 程序设置向导页面 2
 */
public class Setup2Activity extends BaseSetupActivity {

    //自定义组件对象
    private SetingItemView siv_setup2_sim;

    //读取 sim卡的信息
    private TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup2);

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        siv_setup2_sim = (SetingItemView) findViewById(R.id.siv_setup2_sim);

        String sim = sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)) {
            siv_setup2_sim.setChecked(false);
        } else {
            siv_setup2_sim.setChecked(true);
        }

        //点击事件
        siv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();

                if (siv_setup2_sim.isCheck()) {
                    siv_setup2_sim.setChecked(false);
                    editor.putString("sim", null);

                } else {
                    siv_setup2_sim.setChecked(true);

                    //保存sim卡的序列号
                    String sim = tm.getSimSerialNumber();
                   // Toast.makeText(Setup2Activity.this, sim, Toast.LENGTH_LONG).show();
                    editor.putString("sim", sim);
                }

                editor.commit();

            }
        });
    }


    @Override
    protected void showNext() {
        //取出是否绑定sim 信息
        String sim = sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)) {
            //没有绑定
            Toast.makeText(this, "sim卡没有绑定", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }

    @Override
    protected void showPre() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }
}
