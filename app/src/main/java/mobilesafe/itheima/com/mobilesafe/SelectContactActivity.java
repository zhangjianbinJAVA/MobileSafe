package mobilesafe.itheima.com.mobilesafe;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择联系人界面
 */
public class SelectContactActivity extends AppCompatActivity {

    private ListView list_select_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        list_select_contact = (ListView) findViewById(R.id.list_select_contact);

        //获取联系人的数据
        final List<Map<String, String>> data = getContactInfo();

        //数据显示在布局文件中
        list_select_contact.setAdapter(
                new SimpleAdapter(this,
                        data,
                        R.layout.contact_list_item,
                        new String[]{"name", "phone"},
                        new int[]{R.id.name, R.id.phone})
        );

        //数据列表的每一项的点击事件
        list_select_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = data.get(position).get("phone");

                //选择的结果返回
                Intent data = new Intent();
                data.putExtra("phone", phone);
                setResult(0, data);
                finish();//关闭当前页面
            }
        });
    }


    /**
     * 读取手里面的联系人
     *
     * @return
     */
    private List<Map<String, String>> getContactInfo() {

        //保存所有的联系人
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        // 得到一个内容解析器
        ContentResolver resolver = getContentResolver();
        // raw_contacts uri
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uriData = Uri.parse("content://com.android.contacts/data");

        Cursor cursor = resolver.query(
                uri,
                new String[]{"contact_id"},
                null, null, null);

        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(0);

            if (contact_id != null) {
                //保存具体的某一个联系人
                Map<String, String> map = new HashMap<String, String>();

                Cursor dataCursor = resolver.query(
                        uriData,
                        new String[]{"data1", "mimetype"},
                        "contact_id=?",
                        new String[]{contact_id},
                        null);

                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);

                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        //联系人的姓名
                        map.put("name", data1);

                    } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {

                        //联系人的电话号码
                        map.put("phone", data1);
                    }
                }

                list.add(map);
                dataCursor.close();
            }
        }
        cursor.close();
        return list;
    }
}
