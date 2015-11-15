package mobilesafe.itheima.com.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 程序设置向导页面 3
 */
public class Setup3Activity extends BaseSetupActivity {

    private EditText et_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        et_phone = (EditText) findViewById(R.id.et_phone);

        String phone = sp.getString("phone", "");
        et_phone.setText(phone);
    }

    /**
     * 上一步
     */
    @Override
    protected void showPre() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();

        //activity切换动画
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

    /**
     * 下一步
     */
    @Override
    protected void showNext() {
        //保存安全号码
        String phone = et_phone.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("phone", phone);
            editor.commit();
        } else {
            Toast.makeText(Setup3Activity.this, "全安号码没有设置", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }


    /**
     * 选择联系人点击事件
     *
     * @param view
     */
    public void selectContact(View view) {
        Intent intent = new Intent(this, SelectContactActivity.class);
        startActivityForResult(intent, 0);
    }

    /**
     * 选择联系人页面关闭后的回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        String phone = data.getStringExtra("phone").replace("-", "");

        //返回的结果填充到控件中
        et_phone.setText(phone);
    }
}
