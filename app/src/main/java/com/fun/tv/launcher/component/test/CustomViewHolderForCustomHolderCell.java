package com.fun.tv.launcher.component.test;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.fun.tv.launcher.tangram.structure.viewcreator.ViewHolderCreator;


public class CustomViewHolderForCustomHolderCell extends ViewHolderCreator.ViewHolder {

    public LinearLayout linearLayout;

    public CustomViewHolderForCustomHolderCell(Context context) {
        super(context);
    }

    @Override
    protected void onRootViewCreated(View view) {
        if (view instanceof LinearLayout) {
            linearLayout = (LinearLayout) view;
        }
    }
}
