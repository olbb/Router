package com.olbb.router.app;

import android.app.Application;

import com.olbb.router.RouterProxy;
import com.olbb.router.app.router.AppRouterTestProvider;

import router.ebook.media.meizu.com.sublibmodule.SubRouterProvider;


public class MApplication extends Application {

    RouterProxy proxy;

    @Override
    public void onCreate() {
        super.onCreate();
        proxy = RouterProxy.getInstance();
        proxy.init(new AppRouterTestProvider());
        proxy.init(new SubRouterProvider());
    }

    public RouterProxy getProxy() {
        return proxy;
    }
}
