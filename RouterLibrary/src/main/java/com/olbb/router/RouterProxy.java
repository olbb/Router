package com.olbb.router;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class RouterProxy {

    private Map<String, RouteMeta> routeMetaMap = new HashMap<>();

    private static RouterProxy INSTANCE = null;

    public static RouterProxy getInstance() {
        if (INSTANCE == null) {
            synchronized (RouterProxy.class) {
                INSTANCE = new RouterProxy();
            }
        }
        return INSTANCE;
    }


    RouterProxy() {
//        long temp = System.currentTimeMillis();
//        try {
//            initGroups();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("耗时:" + (System.currentTimeMillis() - temp));
    }

//    private void initGroups() throws Exception {
//        List<Class> classes = ClassUtils.getFileNameByPackageName(IRouteGroup.class);
//        for (Class c : classes) {
//            IRouteGroup iRouteGroup = (IRouteGroup) c.newInstance();
//            init(iRouteGroup);
//        }
//        System.out.println("有" + classes + "类");
//    }

    private RouteMeta getMeta(String str) {
        if (routeMetaMap.containsKey(str)) {
            return routeMetaMap.get(str);
        } else {
            return null;
        }
    }

    public Class getActivityClass(String str) {
        RouteMeta mate = getMeta(str);
        if (mate != null && mate.getType() == RouteType.ACTIVITY) {
            return mate.getDestination();
        } else {
            return null;
        }
    }

    public Intent getActivityIntent(Context context, String str) {
        Class t = getActivityClass(str);
        if (t != null) {
            return new Intent(context, t);
        }
        return null;
    }

    public Class getFragmentClass(String str) {
        RouteMeta mate = getMeta(str);
        if (mate != null && mate.getType() == RouteType.FRAGMENT) {
            return mate.getDestination();
        } else {
            return null;
        }
    }

    public Fragment getV4Fragment(String str) throws IllegalAccessException, InstantiationException {
        Class t = getFragmentClass(str);
        if (t != null) {
            return (Fragment) t.newInstance();
        } else {
            return null;
        }
    }

    public android.app.Fragment getFragment(String str) throws IllegalAccessException, InstantiationException {
        Class t = getFragmentClass(str);
        if (t != null) {
            return (android.app.Fragment) t.newInstance();
        } else {
            return null;
        }
    }


    public void init(IRouteGroup routeGroup) {
        routeGroup.loadInto(routeMetaMap);
        System.out.println("routeMetaMap:一共有" + routeMetaMap.size() + "条记录");
    }

}
