package com.olbb.router.app;

import android.app.Application;

import com.olbb.router.RouterProxy;


public class MApplication extends Application {

    RouterProxy proxy;

    @Override
    public void onCreate() {
        super.onCreate();
        proxy = new RouterProxy();
        proxy.init(new EBookRoute());
    }

    public RouterProxy getProxy() {
        return proxy;
    }
}
