package mobilesafe.itheima.com.mobilesafe;

import android.test.AndroidTestCase;

import java.util.List;
import java.util.Random;

import mobilesafe.itheima.com.mobilesafe.db.BlackNumberDBOpenHelper;
import mobilesafe.itheima.com.mobilesafe.db.dao.BlackNumberDao;
import mobilesafe.itheima.com.mobilesafe.domain.BlackNumberInfo;

/**
 * 数据库测试
 */
public class TestBlackNumberDB extends AndroidTestCase {

    /**
     * 数据库创建测试
     */
    public void testCreateDB() {
        BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
        helper.getWritableDatabase();
    }

    //    添加黑名单号码
    public void testAdd() {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.add("110", "1");
    }

    //    删除黑名单号码
    public void testDelete() {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.delete("110");
    }

    //    修改黑名单号码的拦截模式
    public void testUpdate() {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.update("110", "2");
    }

    //    查询黑名单号码是是否存在
    public void testFind() {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        boolean result = dao.find("110");
        assertEquals(true, result);
    }

    //    添加100条记录
    public void testAdd100() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        long basenumber = 13500000000l;
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            dao.add(String.valueOf(basenumber + i), String.valueOf(random.nextInt(3) + 1));
        }
    }

    //    查询所有数据
    public void testFindAll() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        List<BlackNumberInfo> infos = dao.findAll();
        for (BlackNumberInfo info : infos) {
            System.out.println(info.toString());
        }
    }

}
