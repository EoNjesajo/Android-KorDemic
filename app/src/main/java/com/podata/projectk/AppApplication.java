package com.podata.projectk;

import android.app.Application;
import android.content.Context;

public class AppApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        AppApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AppApplication.context;
    }
}
