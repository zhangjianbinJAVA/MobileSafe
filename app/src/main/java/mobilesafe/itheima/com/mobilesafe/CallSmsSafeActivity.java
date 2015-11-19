package mobilesafe.itheima.com.mobilesafe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mobilesafe.itheima.com.mobilesafe.db.dao.BlackNumberDao;
import mobilesafe.itheima.com.mobilesafe.domain.BlackNumberInfo;

/**
 * 黑名单拦截 界面
 */
public class CallSmsSafeActivity extends AppCompatActivity {
    private ListView lv_callsms_safe;
    private List<BlackNumberInfo> infos;
    private BlackNumberDao dao;
    CallSmsSafeAdapter callSmsSafeAdapter;

    //对话框的组件
    private EditText et_blacknumber;
    private CheckBox cb_phone, cb_sms;
    private Button bt_ok, bt_canc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);

        lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);

        dao = new BlackNumberDao(this);
        infos = dao.findAll();
        callSmsSafeAdapter = new CallSmsSafeAdapter();
        lv_callsms_safe.setAdapter(callSmsSafeAdapter);
    }


    /**
     * 添加黑名单电话号码
     *
     * @param view
     */
    public void addBlackNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View contentView = View.inflate(this, R.layout.dialog_add_blacknumber, null);
        dialog.setView(contentView, 0, 0, 0, 0);
        dialog.show();

        et_blacknumber = (EditText) contentView.findViewById(R.id.et_blacknumber);
        cb_phone = (CheckBox) contentView.findViewById(R.id.cb_phone);
        cb_sms = (CheckBox) contentView.findViewById(R.id.cb_sms);
        bt_ok = (Button) contentView.findViewById(R.id.bt_ok);
        bt_canc = (Button) contentView.findViewById(R.id.bt_canc);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blacknumber = et_blacknumber.getText().toString().trim();
                if (TextUtils.isEmpty(blacknumber)) {
                    Toast.makeText(CallSmsSafeActivity.this, "黑名单不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String mode;
                if (cb_phone.isChecked() && cb_sms.isChecked()) {
                    //全部拦截
                    mode = "3";

                } else if (cb_phone.isChecked()) {
                    //电话拦截
                    mode = "1";

                } else if (cb_sms.isChecked()) {
                    //短信拦截
                    mode = "2";
                } else {
                    Toast.makeText(getApplicationContext(), "请选择拦截模式", Toast.LENGTH_SHORT).show();
                    return;
                }

                //黑名单号码 添加到数据库中
                dao.add(blacknumber, mode);

                //更新listView里面的内容，并添加到数组中的第一个位置
                infos.add(0, new BlackNumberInfo(blacknumber, mode));

                //通知listView 数据列表改变了
                callSmsSafeAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        bt_canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    /**
     * 数据适配器
     */
    private class CallSmsSafeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
            return infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;

            //减少内存中 view 对象的创建的个数
            if (convertView == null) {
                view = View.inflate(CallSmsSafeActivity.this, R.layout.list_item_callsms, null);

                //减少子孩子查询的次数
                holder = new ViewHolder();
                holder.tv_black_number = (TextView) view.findViewById(R.id.tv_black_number);
                holder.tv_block_mode = (TextView) view.findViewById(R.id.tv_block_mode);

                //当孩子生出来的时候找到他们的引用，存放在记事本，放在父亲的口袋
                view.setTag(holder);

            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }


            holder.tv_black_number.setText(infos.get(position).getNumber());
            String mode = infos.get(position).getMode();

            if ("1".equals(mode)) {
                holder.tv_block_mode.setText("电话拦截");
            } else if ("2".equals(mode)) {
                holder.tv_block_mode.setText("短信拦截");
            } else {
                holder.tv_block_mode.setText("全部拦截");
            }

            return view;
        }
    }

    /**
     * view 对象的容器
     * 记录孩子的内存地址
     */
    static class ViewHolder {
        TextView tv_black_number;
        TextView tv_block_mode;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_call_sms_safe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
