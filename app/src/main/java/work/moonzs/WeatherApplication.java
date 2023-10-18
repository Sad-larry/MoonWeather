package work.moonzs;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import work.moonzs.mvplibrary.BaseApplication;
import work.moonzs.mvplibrary.utils.ActivityManager;

public class WeatherApplication extends BaseApplication {
    /**
     * 应用实例
     */
    public static WeatherApplication weatherApplication;
    private static Context context;
    private static ActivityManager activityManager;

    private static Activity sActivity;

    public static Context getMyContext() {
        return weatherApplication == null ? null : weatherApplication.getApplicationContext();
    }

    private Handler handler;

    public Handler getMyHandler() {
        return handler;
    }

    public void setMyHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        activityManager = new ActivityManager();
        context = getApplicationContext();
        weatherApplication = this;

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                sActivity = activity;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });


    }

    public static ActivityManager getActivityManager() {
        return activityManager;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // static代码段可以防止内存泄露
    static {
        // 设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                // 全局设置主题颜色
                layout.setPrimaryColorsId(android.R.color.darker_gray, android.R.color.black);
                //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
                // 指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context);
            }
        });
        // 设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context);
            }
        });
    }
}
