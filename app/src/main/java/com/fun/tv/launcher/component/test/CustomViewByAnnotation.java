package com.fun.tv.launcher.component.test;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fun.tv.launcher.component.R;
import com.fun.tv.launcher.tangram.structure.BaseCell;
import com.fun.tv.launcher.tangram.structure.CellRender;
import com.fun.tv.launcher.tangram.util.ImageUtils;


public class CustomViewByAnnotation extends LinearLayout {

    private static final String TAG = "CustomViewByAnnotation";

    private TextView textView;
    private ImageView imageView;

    public CustomViewByAnnotation(Context context) {
        this(context, null);
    }

    public CustomViewByAnnotation(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewByAnnotation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_by_annotation_layout, this, false);
        textView = view.findViewById(R.id.contentText);
        imageView = view.findViewById(R.id.headImg);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 还可以为组件每个属性实现单独的设置方法，而不是在postBindView方法里一次性绑定数据，
     * 这些方法必须以@CellRender注解，框架会在public void postBindView(BaseCell cell)
     * 调用之前调用这些数据绑定方法；
     *
     * @param text 这里的key=text表示让框架取原始json数据里text字段的值传给该方法，原始数据里没有该字段，参数值会是定义类型的默认值
     */
    @CellRender(key = "text")
    public void toastMethod(String text) {
        Log.d("wcedlaLog", "在postBindView执行前显示log:" + text);
    }

    @CellRender
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
    }

    @CellRender
    public void postBindView(BaseCell cell) {
        textView.setTextColor(Color.parseColor(cell.optStringParam("textColor")));
        textView.setText(cell.optParam("text").toString());
        ImageUtils.doLoadImageUrl(imageView, cell.optStringParam("headImg"));
        String bgColor = cell.optStringParam("bgColor");
        if (!TextUtils.isEmpty(bgColor)) {
            setBackgroundColor(Color.parseColor(bgColor));
        }
    }

    @CellRender
    public void postUnBindView(BaseCell cell) {

    }

}
