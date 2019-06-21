package com.example.demo;

import android.app.Application;

import com.tencent.smtt.sdk.QbSdk;

import cn.bmob.v3.Bmob;

public class NewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        QbSdk.initX5Environment(this, null);
        Bmob.initialize(this, "03a37baea6dd88e4a25c61c91922b808");
    }
}
