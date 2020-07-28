package com.fun.tv.launcher.component.test;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.fun.tv.launcher.tangram.TangramBuilder;
import com.fun.tv.launcher.tangram.util.IInnerImageSetter;
import com.libra.Utils;
import com.squareup.picasso.Picasso;

public class MyApplication extends Application {

    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;

        //为了适配virtualview中的rp单位
        Utils.setUedScreenWidth(getResources().getDisplayMetrics().widthPixels);

        Picasso.setSingletonInstance(new Picasso.Builder(this).loggingEnabled(true).build());

        //整个app中使用tangram时的通用图片加载器，这边使用的是系统原生的imageview，
        TangramBuilder.init(this.getApplicationContext(), new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view,
                                                                 @Nullable String url) {
                Picasso.with(MyApplication.this.getApplicationContext()).load(url).into(view);
            }
        }, ImageView.class);
    }
}


