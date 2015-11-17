package mobilesafe.itheima.com.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_proteing;
    private TextView tv_proteing_state;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        sp = getSharedPreferences("config", MODE_PRIVATE);

        cb_proteing = (CheckBox) findViewById(R.id.cb_proteing);

        tv_proteing_state = (TextView) findViewById(R.id.tv_proteing_state);

        cb_proteing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_proteing_state.setText("你已开启防盗保护");
                } else {
                    tv_proteing_state.setText("你没有开启防盗保护");
                }

                //保存选择的状态
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("proteing", isChecked);
                editor.commit();
            }
        });

        //记录用户上一次的选择
        boolean proteing = sp.getBoolean("proteing", false);
        if (proteing) {
            cb_proteing.setChecked(true);
            tv_proteing_state.setText("你已开启防盗保护");
        } else {
            cb_proteing.setChecked(false);
            tv_proteing_state.setText("你没有开启防盗保护");
        }
    }

    @Override
    protected void showPre() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

    @Override
    protected void showNext() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("configed", true); //用户已做过向导了，不需要在出现向导页面了
        editor.commit();

        //设置向导页面完成，进入手机防盗页面
        Intent intent = new Intent(this, LostFindActivity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }


}
