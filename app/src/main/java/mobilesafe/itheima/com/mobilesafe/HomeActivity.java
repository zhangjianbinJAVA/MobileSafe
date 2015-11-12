package mobilesafe.itheima.com.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 程序首页 界面
 */
public class HomeActivity extends Activity {

    private GridView list_home;
    private MyAdapter myAdapter;

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

        //采用九宫格方式显示 列表数据
        list_home = (GridView) findViewById(R.id.list_home);

        myAdapter = new MyAdapter();
        list_home.setAdapter(myAdapter);


        //列表数据点击事件
        list_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 8://进入设置中心
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);

                        startActivity(intent);
                        break;
                }


            }
        });
    }


    /**
     * 适配器
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
