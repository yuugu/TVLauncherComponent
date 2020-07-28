package com.fun.tv.launcher.component.test;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import com.fun.tv.launcher.tangram.MVHelper;
import com.fun.tv.launcher.tangram.structure.BaseCell;
import com.fun.tv.launcher.tangram.support.SimpleClickSupport;

import org.json.JSONObject;

public class CustomCell extends BaseCell<CustomViewByCustomCell> {
    private static final String TAG = "CustomCell";
    private String headerImgUrl;
    private String footerImgUrl;
    private String text;
    private String textColor;

    @Override
    public void parseWith(@NonNull JSONObject data, @NonNull MVHelper resolver) {
        //这边其实还要做子节点下面的属性判断，这边暂时只做了父节点的属性值判断，就是要兼容style下的属性也能够被识别到
        try {
            if (data.has("headImg")) {
                headerImgUrl = data.getString("headImg");
            }
            if (data.has("footImg")) {
                footerImgUrl = data.getString("footImg");
            }
            if (data.has("text")) {
                text = data.getString("text");
            }
            if (data.has("textColor")) {
                textColor = data.getString("textColor");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindView(@NonNull CustomViewByCustomCell view) {
        view.setOnClickListener(this);
        if (!TextUtils.isEmpty(headerImgUrl)) {
            view.setHeaderImg(headerImgUrl);
        }
        if (!TextUtils.isEmpty(footerImgUrl)) {
            view.setFooterImg(footerImgUrl);
        }
        if (!TextUtils.isEmpty(text)) {
            view.setContentText(text);
        }
        if (!TextUtils.isEmpty(textColor)) {
            view.setContentTextColor(textColor);
        }
    }

    @Override
    public void unbindView(@NonNull CustomViewByCustomCell view) {
        super.unbindView(view);
    }

    /**
     * 首先自定义的点击支持类，首先不开启优化
     * 然后使用自定义model的方式实现的有天然的优势
     * 就是可以重写onClick事件,直接调用自定义的点击支持类中对应的点击方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (serviceManager != null) {
            CustomClickSupportDisableOptimized service = (CustomClickSupportDisableOptimized) serviceManager.getService(SimpleClickSupport.class);
            if (service != null) {
                service.onClickCustomCellComponent(v, this, this.pos);
            }
        }
    }
}
