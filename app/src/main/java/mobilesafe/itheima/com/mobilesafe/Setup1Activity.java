package mobilesafe.itheima.com.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

/**
 * 程序设置向导页面 1
 */
public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }

    @Override
    protected void showNext() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();

        /**
         * 要求该方法 在 finish() 或者 startActivity(intent) ;
         *
         *  后面执行
         *
         *  参数：进入的动画 和出去的动画
         */
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }


    @Override
    protected void showPre() {


    }
}
