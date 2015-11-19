package mobilesafe.itheima.com.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 手机黑名单拦截数据库帮助类
 */
public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库名子
     */
    private static final String NAMEDB = "blacknumber.db";

    /**
     * 数据库版本
     */
    private static final int VERSIONDB = 2;

    /**
     * 创建数据库
     *
     * @param context
     */
    public BlackNumberDBOpenHelper(Context context) {
        super(context, NAMEDB, null, VERSIONDB);
    }


    /**
     * 初始化数据库的表结构
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknumber (_id integer primary key autoincrement,number varchar(20),mode varchar(2))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
