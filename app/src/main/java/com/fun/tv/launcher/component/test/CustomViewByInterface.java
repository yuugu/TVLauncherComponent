package com.fun.tv.launcher.component.test;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
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
import com.fun.tv.launcher.tangram.structure.view.ITangramViewLifeCycle;
import com.fun.tv.launcher.tangram.support.ExposureSupport;
import com.fun.tv.launcher.tangram.util.ImageUtils;

public class CustomViewByInterface extends LinearLayout implements ITangramViewLifeCycle {
    private static final String TAG = "CustomViewByInterface";
    private TextView textView;
    private ImageView imageView;

    public CustomViewByInterface(Context context) {
        this(context, null);
    }

    public CustomViewByInterface(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewByInterface(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_by_interface_layout, this, false);
        textView = view.findViewById(R.id.contentText);
        imageView = view.findViewById(R.id.headImg);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
        if (cell.serviceManager != null) {
            ExposureSupport exposureSupport = cell.serviceManager.getService(ExposureSupport.class);
            if (exposureSupport != null) {
                exposureSupport.onTrace(this, cell, cell.pos);
            }
        }
    }

    @Override
    public void postBindView(BaseCell cell) {
        textView.setTextColor(Color.parseColor(cell.optStringParam("textColor")));
        textView.setText(cell.optParam("text").toString());
        Log.d(TAG, "图片地址:" + cell.optStringParam("headImg"));
        ImageUtils.doLoadImageUrl(imageView, cell.optStringParam("headImg"));
    }

    @Override
    public void postUnBindView(BaseCell cell) {
    }
}
