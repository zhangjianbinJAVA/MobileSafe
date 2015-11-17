package mobilesafe.itheima.com.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mobilesafe.itheima.com.mobilesafe.utils.NumberAddressQueryUtils;

public class NumberAddressActivity extends Activity {

    private EditText ed_phone;
    private TextView tv_showResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_address);

        ed_phone = (EditText) findViewById(R.id.ed_phone);
        tv_showResult = (TextView) findViewById(R.id.tv_showResult);


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
        } else {
            //去数据库中查询 电话号码查询归属地

            String address = NumberAddressQueryUtils.queryNumber(phoneNumber);
            tv_showResult.setText(address);
        }
    }
}
