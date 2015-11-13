package mobilesafe.itheima.com.mobilesafe.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobilesafe.itheima.com.mobilesafe.R;

/**
 * 自定义组合 控件
 * <p/>
 * 1.两个TexView
 * 2.一个CheckBox
 * 3.一个View，是一个下划线
 */
public class SetingItemView extends RelativeLayout {

    private CheckBox cb_status;

    private TextView tv_title, tv_desc;

    //自定义属性
    private String desc_title;
    private String desc_on;
    private String desc_off;


    public SetingItemView(Context context) {
        super(context);
        iniView(context);
    }


    /**
     * 带有两个参数的构造方法，布局文件使用的时候调用
     *
     * @param context
     * @param attrs   ：自动传入的属性集合
     */
    public SetingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniView(context);

        //获取属性设置的值
        if (attrs != null) {

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.myView);

            desc_title = a.getString(R.styleable.myView_desc_title);
            desc_on = a.getString(R.styleable.myView_desc_on);
            desc_off = a.getString(R.styleable.myView_desc_off);

            a.recycle();//为了保持以后使用的一致性，需要回收
        }

        tv_title.setText(desc_title);
    }

    public SetingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        View.inflate(context, R.layout.setting_item_view, SetingItemView.this);


        //获取组件
        cb_status = (CheckBox) this.findViewById(R.id.cb_status);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_desc = (TextView) this.findViewById(R.id.tv_desc);


    }


    /**
     * 校验组合组件是否 选中
     *
     * @return
     */
    public boolean isCheck() {
        return cb_status.isChecked();
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
        cb_status.setChecked(checked);
    }


    /**
     * 根据选中的状态 更新状态信息
     *
     * @param text
     */
    public void setDesc(String text) {
        tv_desc.setText(text);
    }


}
