package mobilesafe.itheima.com.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

/**
 * 程序设置向导页面 3
 */
public class Setup3Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    @Override
    protected void showPre() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

    @Override
    protected void showNext() {
        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }

}
