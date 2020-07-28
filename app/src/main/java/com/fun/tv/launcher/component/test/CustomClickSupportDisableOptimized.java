package com.fun.tv.launcher.component.test;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fun.tv.launcher.tangram.eventbus.BusSupport;
import com.fun.tv.launcher.tangram.eventbus.EventContext;
import com.fun.tv.launcher.tangram.structure.BaseCell;
import com.fun.tv.launcher.tangram.support.SimpleClickSupport;

public class CustomClickSupportDisableOptimized extends SimpleClickSupport {

    @Override
    public void defaultClick(View targetView, BaseCell cell, int eventType) {
        if (cell.serviceManager != null) {
            BusSupport busSupport = cell.serviceManager.getService(BusSupport.class);
            if (busSupport != null) {
                EventContext eventContext = new EventContext();
                eventContext.producer = cell;
                busSupport.post(BusSupport.obtainEvent());
            }
        }
        Log.d("wcedlaLog", "默认实现的点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos);
        Toast.makeText(MyApplication.applicationContext, "默认实现的点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos, Toast.LENGTH_SHORT).show();

    }

    public void onClickAnnotationComponent(CustomViewByAnnotation targetView, BaseCell cell, int type) {
        Log.d("wcedlaLog", "未优化时点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos);
        Toast.makeText(MyApplication.applicationContext, "未优化时点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos, Toast.LENGTH_SHORT).show();
    }

    public void onClickCustomCellComponent(View targetView, BaseCell cell, int type) {
        Log.d("wcedlaLog", "自定义model点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos);
        Toast.makeText(MyApplication.applicationContext, "自定义model点击:" + targetView.getClass().getName() + ",cell的类型:" + cell.stringType + ",cell位置:" + cell.pos, Toast.LENGTH_SHORT).show();
    }

}
