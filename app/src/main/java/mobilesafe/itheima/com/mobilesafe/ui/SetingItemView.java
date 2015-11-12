package mobilesafe.itheima.com.mobilesafe.ui;

import android.content.Context;
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


    public SetingItemView(Context context) {
        super(context);
        iniView(context);
    }

    public SetingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniView(context);

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
