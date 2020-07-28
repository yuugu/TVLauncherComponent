package com.fun.tv.launcher.component.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fun.tv.launcher.component.R;
import com.fun.tv.launcher.tangram.structure.BaseCell;
import com.fun.tv.launcher.tangram.structure.view.ITangramViewLifeCycle;
import com.fun.tv.launcher.tangram.support.TimerSupport;

public class CustomTimerView extends LinearLayout implements ITangramViewLifeCycle, TimerSupport.OnTickListener {

    private static final String TAG = "CustomTimerView";

    private TextView timerText;

    private int nowSecond;

    public CustomTimerView(Context context) {
        this(context, null);
    }

    public CustomTimerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_timer_view_layout, this, false);
        timerText = view.findViewById(R.id.timerText);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void cellInited(BaseCell cell) {

    }

    @Override
    public void postBindView(BaseCell cell) {
        nowSecond = 0;
        if (cell.serviceManager != null) {
            TimerSupport timerSupport = cell.serviceManager.getService(TimerSupport.class);
            if (timerSupport != null && !timerSupport.isRegistered(this)) {
                //第一个参数4是单位秒，第二个参数是接口回调，第三个参数是立即执行
                timerSupport.register(1, this, true);
            }
        }
    }

    @Override
    public void postUnBindView(BaseCell cell) {
        if (cell.serviceManager != null) {
            TimerSupport timerSupport = cell.serviceManager.getService(TimerSupport.class);
            if (timerSupport != null) {
                timerSupport.unregister(this);
            }
        }
    }


    @Override
    public void onTick() {
        Log.d(TAG, "定时器回调线程:" + Thread.currentThread().getName());
        nowSecond += 1;
        timerText.setText(String.format("当前已运行:%ss", nowSecond));
    }
}
