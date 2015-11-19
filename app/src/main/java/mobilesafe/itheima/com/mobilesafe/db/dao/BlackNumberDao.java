package mobilesafe.itheima.com.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mobilesafe.itheima.com.mobilesafe.db.BlackNumberDBOpenHelper;
import mobilesafe.itheima.com.mobilesafe.domain.BlackNumberInfo;

/**
 * 黑名拦截的 dao
 */
public class BlackNumberDao {

    private BlackNumberDBOpenHelper helper;

    /**
     * 创建 数据库工具实例
     *
     * @param context
     */
    public BlackNumberDao(Context context) {
        this.helper = new BlackNumberDBOpenHelper(context);
    }

    /**
     * 查询黑名单号码是是否存在
     *
     * @param number
     * @return
     */
    public boolean find(String number) {
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blacknumber where number=?", new String[]{number});
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询黑名单号码的拦截模式
     *
     * @param number
     * @return 返回号码的拦截模式，不是黑名单号码返回null
     */
    public String findMode(String number) {
        String result = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select mode from blacknumber where number=?", new String[]{number});
        if (cursor.moveToNext()) {
            result = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询全部黑名单号码
     *
     * @return
     */
    public List<BlackNumberInfo> findAll() {
        List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc", null);//倒序的查询
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            info.setMode(mode);
            info.setNumber(number);
            result.add(info);
        }
        cursor.close();
        db.close();
        return result;
    }


    /**
     * 添加黑名单号码
     *
     * @param number 黑名单号码
     * @param mode   拦截模式 1.电话拦截 2.短信拦截 3.全部拦截
     */
    public void add(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        db.insert("blacknumber", null, values);
        db.close();
    }

    /**
     * 修改黑名单号码的拦截模式
     *
     * @param number  要修改的黑名单号码
     * @param newmode 新的拦截模式
     */
    public void update(String number, String newmode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", newmode);
        db.update("blacknumber", values, "number=?", new String[]{number});
        db.close();
    }

    /**
     * 删除黑名单号码
     *
     * @param number 要删除的黑名单号码
     */
    public void delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("blacknumber", "number=?", new String[]{number});
        db.close();
    }

}
