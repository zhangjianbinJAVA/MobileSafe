package mobilesafe.itheima.com.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Setup4Activity extends BaseSetupActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        sp = getSharedPreferences("config", MODE_PRIVATE);
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
        editor.putBoolean("configed", true);
        editor.commit();

        //设置向导页面完成，进入手机防盗页面
        Intent intent = new Intent(this, LostFindActivity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }


}
