package work.moonzs.mvplibrary.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.Unbinder;
import work.moonzs.mvplibrary.BaseApplication;
import work.moonzs.mvplibrary.R;
import work.moonzs.mvplibrary.kit.KnifeKit;

/**
 * 用于不需要请求网络接口的Activity
 */
public abstract class BaseActivity extends AppCompatActivity implements UiCallBack {
    private static final int FAST_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;
    protected Activity context;
    private Unbinder unbinder;
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeView(savedInstanceState);
        this.context = this;
        // 添加继承这个BaseActivity的Activity
        BaseApplication.getActivityManager().addActivity(this);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            unbinder = KnifeKit.bind(this);
        }
        initData(savedInstanceState);
    }

    @Override
    public void initBeforeView(Bundle saveInstanceState) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // 弹窗出现
    public void showLoadingDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(context, R.style.loading_dialog);
        }
        mDialog.setContentView(R.layout.dialog_loading);
        mDialog.setCancelable(false);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.show();
    }

    // 弹窗消失
    public void dismissLoadingDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = null;
    }

    /**
     * 返回
     */
    public void Back(Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
                if (!isFastClick()) {
                    context.finish();
                }
            }
        });
    }

    // 两次点击间隔不能少于500ms
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
}
