package mobilesafe.itheima.com.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mobilesafe.itheima.com.mobilesafe.db.NumberAddressQueryUtils;

/**
 * 号码归属地查询
 */
public class NumberAddressActivity extends Activity {

    private EditText ed_phone;
    private TextView tv_showResult;

    /**
     * 系统自带的震动效果
     */
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_address);

        ed_phone = (EditText) findViewById(R.id.ed_phone);
        tv_showResult = (TextView) findViewById(R.id.tv_showResult);

        //文本输入后自动查询
        ed_phone.addTextChangedListener(new TextWatcher() {

            /**
             * 当文本发生变化之前回调
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             * 当文本发生变化的时候回调
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() >= 3) {
                    //查询数据库，并且显示结果
                    String address = NumberAddressQueryUtils.queryNumber(s.toString());
                    tv_showResult.setText("结果："+address);
                }

            }

            /**
             * 当文本发生变化之后回调
             */
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    /**
     * 查询号码归属地
     *
     * @param view
     */
    public void numberAddressQuery(View view) {

        String phoneNumber = ed_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "号码为空", Toast.LENGTH_SHORT).show();

            //实例化震动服务
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            vibrator.vibrate(2000);//震动的时间

            long[] pattern = {200,200,200,200};//停，震动
            vibrator.vibrate(pattern,-1);// 0：循环震动， -1：不重复  1：从pattern的索引1开始震动

        } else {
            //去数据库中查询 电话号码查询归属地
            //第一种：联网查询；
            //第二种：把数据库 放在本地；
            String address = NumberAddressQueryUtils.queryNumber(phoneNumber);
            tv_showResult.setText("结果："+address);
        }
    }
}
