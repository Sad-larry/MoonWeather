package work.moonzs.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    // 长消息
    public static void showLongToast(Context context, CharSequence sequence) {
        Toast.makeText(context.getApplicationContext(), sequence, Toast.LENGTH_LONG).show();
    }

    // 短消息
    public static void showShortToast(Context context, CharSequence sequence) {
        Toast.makeText(context.getApplicationContext(), sequence, Toast.LENGTH_SHORT).show();
    }
}
