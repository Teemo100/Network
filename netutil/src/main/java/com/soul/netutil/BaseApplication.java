package com.soul.netutil;

import android.app.Application;
import android.content.Context;

import com.qmtv.lib.util.ProcessUtils;

/**
 * description:
 *
 * @author soul
 * Create date: 2019/7/1/001 0:43
 */
public class BaseApplication extends Application {
    private static BaseApplication sBaseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sBaseApplication = this;
    }

    public static Context getContext() {
        return sBaseApplication.getApplicationContext();
    }
}
