package mobilesafe.itheima.com.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

/**
 * 程序设置向导页面 2
 */
public class Setup2Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }


    @Override
    protected void showNext() {
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
