package work.moonzs.mvplibrary;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import work.moonzs.mvplibrary.utils.ActivityManager;

/**
 * 工程管理
 */
public class BaseApplication extends Application {
    private static ActivityManager activityManager;
    private static BaseApplication application;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // 声明activity管理
        activityManager = new ActivityManager();
        context = getApplicationContext();
        application = this;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static ActivityManager getActivityManager() {
        return activityManager;
    }

    // 内容提供器
    public static Context getContext() {
        return context;
    }

    public static BaseApplication getApplication() {
        return application;
    }
}
