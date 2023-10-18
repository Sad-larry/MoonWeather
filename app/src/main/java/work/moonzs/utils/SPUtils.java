package work.moonzs.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences 工具类
 */
public class SPUtils {
    private static final String NAME = "config";

    private static SharedPreferences getSP(Context ctx) {
        return ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static void putBoolean(String key, boolean value, Context ctx) {
        getSP(ctx).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue, Context ctx) {
        return getSP(ctx).getBoolean(key, defValue);
    }

    public static void putString(String key, String value, Context ctx) {
        getSP(ctx).edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue, Context ctx) {
        if (ctx != null) {
            return getSP(ctx).getString(key, defValue);
        }
        return "";
    }

    public static void putInt(String key, int value, Context ctx) {
        getSP(ctx).edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue, Context ctx) {
        return getSP(ctx).getInt(key, defValue);
    }

    public static void remove(String key, Context ctx) {
        getSP(ctx).edit().remove(key).apply();
    }
}
