package mobilesafe.itheima.com.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义一个TextView ，并自动获取了焦点
 */
public class FocusedTextView extends TextView {


    public FocusedTextView(Context context) {
        super(context);
    }

    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 当前并没有焦点 ,欺骗了android 系统
     *
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
