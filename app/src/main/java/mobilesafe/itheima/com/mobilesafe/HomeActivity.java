package mobilesafe.itheima.com.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mobilesafe.itheima.com.mobilesafe.utils.MD5Utils;

/**
 * 程序首页 界面
 */
public class HomeActivity extends Activity {
    private static final String TAG = "HomeActivity";

    private GridView list_home;
    private MyAdapter myAdapter;

    private SharedPreferences sp;


    private static String[] names = {
            "手机防盗",
            "通讯卫士",
            "软件管理",
            "进程管理",
            "流量统计",
            "手机杀毒",
            "缓存清理",
            "高级工具",
            "设置中心"
    };

    private static int ids[] = {

            R.drawable.safe,
            R.drawable.callmsgsafe,
            R.drawable.app,
            R.drawable.taskmanager,
            R.drawable.netmanager,
            R.drawable.trojan,
            R.drawable.sysoptimize,
            R.drawable.atools,
            R.drawable.settings


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = getSharedPreferences("config", MODE_PRIVATE);


        //采用九宫格方式显示 列表数据
        list_home = (GridView) findViewById(R.id.list_home);

        myAdapter = new MyAdapter();
        list_home.setAdapter(myAdapter);


        //列表数据点击事件
        list_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 8://设置中心
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);

                        startActivity(intent);
                        break;
                    case 0: //手机防盗
                        showLostFindDialog();
                        break;
                    case 7://高级工具
                        Intent intent1 = new Intent(HomeActivity.this, AtoolsActivity.class);
                        startActivity(intent1);
                        break;
                }


            }
        });
    }


    //手机防盗
    protected void showLostFindDialog() {
        //判断是否设置过密码
        if (isSetupPwd()) {
            //已经设置过密码了，弹出 输入密码对话框
            showEnterDialog();


        } else {
            //没有设置密码,弹出 设置密码对话框
            showSetuPwdDialog();

        }

    }

    private EditText et_setup_pwd, et_setup_confrim;
    private Button ok, cancel;

    private AlertDialog dialog;

    /**
     * 设置密码对话框
     */
    private void showSetuPwdDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        //自定义对话框布局
        View view = View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);

        et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
        et_setup_confrim = (EditText) view.findViewById(R.id.et_setup_confrim);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把对话框关闭掉
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取去密码
                String password = et_setup_pwd.getText().toString().trim();
                String password_confirm = et_setup_confrim.getText().toString().trim();

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password_confirm)) {
                    Toast.makeText(HomeActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //判断是否一致
                if (password.equals(password_confirm)) {
                    //保存密码
                    SharedPreferences.Editor edit = sp.edit();

                    //对密码加密,并保存
                    edit.putString("password", MD5Utils.getMD5Password(password));

                    edit.commit();
                    dialog.dismiss();

                    //进入防盗页面
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(HomeActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    et_setup_pwd.setText("");
                    et_setup_confrim.setText("");

                    return;
                }
            }
        });


        builder.setView(view);
        dialog = builder.show();
    }

    /**
     * 输入密码对话框
     */
    private void showEnterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        //自定义对话框布局
        View view = View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);

        et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把对话框关闭掉
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取去密码
                String password = et_setup_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(HomeActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp.getString("password", "").equals(MD5Utils.getMD5Password(password))) {
                    //密码正确，则对话框消掉，进入手机防盗页页
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                    startActivity(intent);

                    dialog.dismiss();
                } else {
                    Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    et_setup_pwd.setText("");
                    return;
                }
            }
        });


        /**
         * 在低版本中会显示 黑边，
         * 解决：
         * 1.背景设为白色
         * 2.使用 dialog 来显示布局的view
         *  builder.setView(view);
         *  dialog = builder.show();
         *
         */
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }


    /**
     * 判断是否设置过密码
     *
     * @return
     */
    private boolean isSetupPwd() {
        String password = sp.getString("password", null);
        return !TextUtils.isEmpty(password);
    }


    /**
     * 适配器，功能列表
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(HomeActivity.this, R.layout.list_home, null);

            ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
            TextView tv_item = (TextView) view.findViewById(R.id.tv_item);

            iv_item.setImageResource(ids[position]);
            tv_item.setText(names[position]);
            return view;
        }
    }
}
