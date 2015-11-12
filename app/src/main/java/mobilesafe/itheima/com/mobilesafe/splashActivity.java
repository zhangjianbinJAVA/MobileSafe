package mobilesafe.itheima.com.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import mobilesafe.itheima.com.mobilesafe.utils.StreamTools;

/**
 * 程序初始化 界面 splash 1.0
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

    /**
     * 新版本下载地址 apk
     */
    private String apkurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);

        //动态设置版本号
        tv_splash_version.setText("版本号" + getVersionName());


        //检查版本升级
        checkUpdate();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(500);

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
                    break;
                case NETWORK_ERROR: //网络错误
                    enterHome();
                    break;
                case JSON_ERROR://json 解析出错
                    enterHome();
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
                                }

                                @Override
                                public void onSuccess(File file) {
                                    super.onSuccess(file);
                                }

                                @Override
                                public void onLoading(long count, long current) {
                                    super.onLoading(count, current);
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
        PackageManager pm = getPackageManager();
        try {

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


                    } else {
                        Log.i(Tag, "联网失败");
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

}
