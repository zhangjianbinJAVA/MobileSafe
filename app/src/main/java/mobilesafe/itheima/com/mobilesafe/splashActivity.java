package mobilesafe.itheima.com.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import mobilesafe.itheima.com.mobilesafe.utils.StreamTools;

/**
 * 程序初始化 界面 splash 1.0
 * <p/>
 * splash界面的作用
 * 1、用来展现产品的Logo；
 * 2、应用程序初始化的操作；
 * 3、检查应用程序的版本；
 * 4、检查当前应用程序是否合法注册；
 */
public class splashActivity extends Activity {

    protected static final String Tag = "splashActivity";

    private static final int ENTER_HOME = 0;
    private static final int SHOW_UPDATE_DIALOG = 1;
    private static final int URL_ERROR = 2;
    private static final int NETWORK_ERROR = 3;
    private static final int JSON_ERROR = 4;

    private TextView tv_splash_version;
    private String version;
    private String description;

    private TextView tv_update_info;

    /**
     * 新版本下载地址 apk
     */
    private String apkurl;

    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        copyDB();//拷贝数据库，只操作一次

        sp = getSharedPreferences("config", MODE_PRIVATE);

        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);

        //动态设置版本号
        tv_splash_version.setText("版本号" + getVersionName());


        //下载app进度
        tv_update_info = (TextView) findViewById(R.id.tv_update_info);

        boolean update = sp.getBoolean("update", false);

        if (update) {
            //检查版本升级
            checkUpdate();
        } else {
            //用户在设置中心已经关闭了自动更新
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterHome();//进入主页面
                }
            }, 2000);
        }

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(500);

        //设置动画效果只有 view 能启动 动画
        findViewById(R.id.rl_root_splash).startAnimation(alphaAnimation);

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ENTER_HOME: //进入主页面
                    enterHome();
                    break;
                case SHOW_UPDATE_DIALOG: //显示 升级对话框
                    showUpdateDialog();

                    break;
                case URL_ERROR://url 错误
                    enterHome();
                    Toast.makeText(splashActivity.this, "url错误", Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ERROR: //网络错误
                    enterHome();
                    Toast.makeText(splashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();

                    break;
                case JSON_ERROR://json 解析出错
                    enterHome();
                    Toast.makeText(splashActivity.this, "数据解析出错", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    /**
     * 弹出 升级对话框
     */
    private void showUpdateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("提醒升级");

        /**
         * 用户点击返回 或   触摸 屏幕其它地方，对话框不 消失，通常用于强制升级(不友好)
         */
        // builder.setCancelable(false);

        /**
         * （友好的体验） 监听用户 点击 取消的事件
         */
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //进入主页面
                enterHome();
                dialog.dismiss();
            }
        });

        builder.setMessage(description);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //下载apk，并且替换安装
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    //sdcard存在  FinalHttp :自动下载app框架
                    FinalHttp finalHttp = new FinalHttp();
                    finalHttp.download(
                            apkurl,
                            Environment.getExternalStorageDirectory().getAbsolutePath()
                                    + "/mobilesafe2.0.apk",
                            new AjaxCallBack<File>() {

                                @Override
                                public void onFailure(Throwable t, int errorNo, String strMsg) {
                                    super.onFailure(t, errorNo, strMsg);
                                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(File file) {
                                    super.onSuccess(file);
                                    installAPK(file);
                                }

                                @Override
                                public void onLoading(long count, long current) {
                                    super.onLoading(count, current);

                                    //显示进度条
                                    tv_update_info.setVisibility(View.VISIBLE);

                                    int progress = (int) (current * 100 / count);
                                    tv_update_info.setText("下载进度：" + progress + "%");
                                }

                                /**
                                 * 安装apk
                                 * @param file
                                 */
                                private void installAPK(File file) {

                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                    startActivity(intent);
                                }


                            });


                } else {
                    Toast.makeText(getApplicationContext(), "没有sdcard，请安装再试升级", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();//进入主界面
            }
        });

        builder.create().show();
    }

    /**
     * 进入主页
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        //关闭当前页面
        finish();
    }

    /**
     * 得到应用程序的版本名称
     *
     * @return
     */
    private String getVersionName() {
        //用来管理手机的apk
        try {
            PackageManager pm = getPackageManager();

            //得到 apk的功能清单文件
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 检查版本升级
     */
    private void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //消息池中获取 消息对象
                Message mes = Message.obtain();


                //开始时间
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL(getString(R.string.serverurl));

                    //联网操作
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(4000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        //联网成功，并获取服务器上的apk的版本信息 json数据
                        final String result = StreamTools.readFromStream(conn.getInputStream());
                        Log.i(Tag, "联网成功");


                        //json 解析
                        JSONObject obj = new JSONObject(result);

                        version = (String) obj.get("version");
                        description = (String) obj.get("description");
                        apkurl = (String) obj.get("apkurl");

                        //校验是否有新版本
                        if (getVersionName().equals(version)) {
                            //版本一致，进入主界面
                            mes.what = ENTER_HOME;


                        } else {
                            //有新版本，弹出 升级对话框
                            mes.what = SHOW_UPDATE_DIALOG;
                        }


                    }

                } catch (MalformedURLException e) {
                    mes.what = URL_ERROR;
                    e.printStackTrace();

                } catch (IOException e) {
                    mes.what = NETWORK_ERROR;
                    e.printStackTrace();

                } catch (JSONException e) {
                    mes.what = JSON_ERROR;
                    e.printStackTrace();
                } finally {

                    long endTime = System.currentTimeMillis();

                    long dTime = endTime - startTime;
                    if (dTime < 2000) {
                        try {
                            Thread.sleep(2000 - dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    handler.sendMessage(mes);
                }
            }
        }).start();
    }


    // path 把address.db这个数据库拷贝到data/data/《包名》/files/address.db
    private void copyDB() {
        try {

            File file = new File(getFilesDir(), "address.db");

            if (file.exists() && file.length() > 0) {
                //正常了，就不需要拷贝了
                Toast.makeText(this, "正常了，就不需要拷贝了", Toast.LENGTH_SHORT).show();
            } else {
                InputStream is = getAssets().open("address.db");

                file = new File(getFilesDir(), "address.db");
                FileOutputStream fos = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
