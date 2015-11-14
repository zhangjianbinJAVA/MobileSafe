package mobilesafe.itheima.com.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {

    //定义手势识别器
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_setup);

        /**
         * 实例化一个手势 识别器
         */
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            /**
             * 当手指在屏幕上 滑动的时候 回调的方法
             *
             * @param e1 :手指按下的那个点
             * @param e2 ：手指离开的那个点
             * @param velocityX : x轴移动的像素
             * @param velocityY ：y轴移动的像素
             * @return :如果返回true :则不能做任何事情，如果返回 false ,则可以做其他事情
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if ((e2.getRawX() - e1.getRawX()) > 100) {
                    //显示上一个页面，从左往右滑动
                    showPre();
                    return true;
                }

                if ((e1.getRawX() - e2.getRawX()) > 100) {
                    //显示下一个页面，从右往左滑动
                    showNext();

                    return true;
                }


                //屏蔽 斜着划的情况
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
                    Toast.makeText(getBaseContext(), "不能这样滑动", Toast.LENGTH_SHORT).show();
                    return true;
                }

                //屏蔽在 x轴滑动很慢的情形
                if (Math.abs(velocityX) < 100) {
                    Toast.makeText(getBaseContext(), "滑动的太慢了", Toast.LENGTH_SHORT).show();
                    return true;
                }


                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * 使用手势识别器
     * <p/>
     * 当手指滑动时，触发该方法，将 滑动的事件 交给手势识别器去处理
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }


    /**
     * 显示下一步抽象方法
     */
    protected abstract void showNext();

    /**
     * 显示上一步抽象方法
     */
    protected abstract void showPre();


    public void next(View view) {
        showNext();
    }

    public void pre(View view) {
        showPre();
    }

}
