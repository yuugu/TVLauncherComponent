package com.fun.tv.launcher.waterfall.module;

import android.app.Application;

import com.squareup.picasso.Picasso;

/**
 * @author: zheng
 * @date: 2020/7/23
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Picasso.setSingletonInstance(new Picasso.Builder(this).loggingEnabled(true).build());
    }
}