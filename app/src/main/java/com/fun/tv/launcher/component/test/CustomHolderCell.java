package com.fun.tv.launcher.component.test;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fun.tv.launcher.component.R;
import com.fun.tv.launcher.tangram.MVHelper;
import com.fun.tv.launcher.tangram.eventbus.BusSupport;
import com.fun.tv.launcher.tangram.eventbus.Event;
import com.fun.tv.launcher.tangram.eventbus.EventHandlerWrapper;
import com.fun.tv.launcher.tangram.structure.BaseCell;

import org.json.JSONObject;

public class CustomHolderCell extends BaseCell<LinearLayout> {

    private static final String TAG = "CustomHolderCell";

    private String headerText;
    private String inputHint;

    private TextView textView;
    private EditText editText;

    private final EventHandlerWrapper eventHandlerWrapper = BusSupport.wrapEventHandler("clicked", "", this, "eventTest");

    @Override
    public void parseWith(@NonNull JSONObject data, @NonNull MVHelper resolver) {
        try {
            if (data.has("headerText")) {
                headerText = data.getString("headerText");
            }
            if (data.has("inputHint")) {
                inputHint = data.getString("inputHint");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindView(@NonNull LinearLayout view) {
        if (serviceManager != null) {
            BusSupport busSupport = serviceManager.getService(BusSupport.class);
            busSupport.register(eventHandlerWrapper);
        }
        TextView header = view.findViewById(R.id.headerText);
        EditText textInput = view.findViewById(R.id.textInput);
        this.textView = header;
        this.editText = textInput;
        if (!TextUtils.isEmpty(headerText)) {
            header.setText(headerText);
        }
        if (!TextUtils.isEmpty(inputHint)) {
            textInput.setHint(inputHint);
        }
    }

    @Override
    public void unbindView(@NonNull LinearLayout view) {
        super.unbindView(view);
        if (serviceManager != null) {
            BusSupport busSupport = serviceManager.getService(BusSupport.class);
            busSupport.unregister(eventHandlerWrapper);
        }
    }

    public void eventTest(Event event) {
        Log.d(TAG, "收到事件:" + event);
        editText.setText("收到点击事件了，现在显示文字了！");

    }
}
