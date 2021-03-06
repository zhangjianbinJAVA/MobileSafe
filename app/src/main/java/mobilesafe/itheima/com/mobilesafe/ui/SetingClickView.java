package mobilesafe.itheima.com.mobilesafe.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobilesafe.itheima.com.mobilesafe.R;

/**
 * 自定义组合 控件
 * <p/>
 * 1.两个TexView
 * 2. 一个图片
 * 3.一个View，是一个下划线
 */
public class SetingClickView extends RelativeLayout {

    private TextView tv_title, tv_desc;

    //自定义属性
    private String desc_title;
    private String desc_on;
    private String desc_off;


    public SetingClickView(Context context) {
        super(context);
        iniView(context);
    }


    /**
     * 带有两个参数的构造方法，布局文件使用的时候调用
     *
     * @param context
     * @param attrs   ：自动传入的属性集合
     */
    public SetingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniView(context);

        //获取自定义属性设置的值
        if (attrs != null) {

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.myView);

            desc_title = a.getString(R.styleable.myView_desc_title);
            desc_on = a.getString(R.styleable.myView_desc_on);
            desc_off = a.getString(R.styleable.myView_desc_off);

            a.recycle();//为了保持以后使用的一致性，需要回收
        }


        tv_title.setText(desc_title);//使用这个组合组件是的默认显示
        setDesc(desc_off);//使用这个组合组件是的默认显示
    }

    public SetingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        iniView(context);
    }

    /**
     * 初始化布局文件
     *
     * @param context
     */
    private void iniView(Context context) {
        /**
         * 把一个布局文件转 为View ,并且加载到 SetingItemView 对象中
         */
        View.inflate(context, R.layout.setting_click_view, SetingClickView.this);

        //获取组件
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_desc = (TextView) this.findViewById(R.id.tv_desc);
    }


    /**
     * 设置组合控件的状态
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        if (checked) {
            setDesc(desc_on);//使用自定义属性值
        } else {
            setDesc(desc_off);
        }
    }


    /**
     * 根据选中的状态 更新状态信息
     *
     * @param text
     */
    public void setDesc(String text) {
        tv_desc.setText(text);
    }


    /**
     * 设置组合控件的标题
     */

    public void setTitle(String title) {
        tv_title.setText(title);
    }

}
