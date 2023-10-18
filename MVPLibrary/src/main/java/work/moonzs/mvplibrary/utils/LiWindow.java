package work.moonzs.mvplibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.HashMap;
import java.util.Map;

import work.moonzs.mvplibrary.R;

/**
 * 自定义弹窗
 */
public class LiWindow {
    private LiWindow mLiWindow;
    private PopupWindow mPopupWindow;
    private LayoutInflater inflater;
    private View mView;
    private Context mContext;
    private WindowManager show;
    WindowManager.LayoutParams context;
    private Map<String, Object> mMap = new HashMap<>();

    public Map<String, Object> getmMap() {
        return mMap;
    }

    public LiWindow(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        mLiWindow = this;
    }

    public LiWindow(Context mContext, Map<String, Object> mMap) {
        this.mContext = mContext;
        this.mMap = mMap;
        inflater = LayoutInflater.from(mContext);
    }

    /**
     * 右侧显示 自适应大小
     *
     * @param mView
     */
    public void showRightPopupWindow(View mView) {
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(mView);
        // 设置点击空白处关闭弹窗
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        // 设置动画
        mPopupWindow.setAnimationStyle(R.style.AnimationRightFade);
        mPopupWindow.showAtLocation(mView, Gravity.RIGHT, 0, 0);
        setBackgroundAlpha(0.5f, mContext);
        WindowManager.LayoutParams nomal = ((Activity) mContext).getWindow().getAttributes();
        nomal.alpha = 0.5f;
        ((Activity) mContext).getWindow().setAttributes(nomal);
        mPopupWindow.setOnDismissListener(closeDismiss);
    }

    /**
     * 右侧显示，高度占满父布局
     *
     * @param mView
     */
    public void showRightPopupWindowMatchParent(View mView) {
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setContentView(mView);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimationRightFade);
        mPopupWindow.showAtLocation(mView, Gravity.RIGHT, 0, 0);
        setBackgroundAlpha(0.5f, mContext);
        WindowManager.LayoutParams nomal = ((Activity) mContext).getWindow().getAttributes();
        nomal.alpha = 0.5f;
        ((Activity) mContext).getWindow().setAttributes(nomal);
        mPopupWindow.setOnDismissListener(closeDismiss);
    }

    /**
     * 底部显示
     *
     * @param mView
     */
    public void showBottomPopupWindow(View mView, PopupWindow.OnDismissListener onDismissListener) {
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(mView);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        mPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);

        setBackgroundAlpha(0.5f, mContext);
        WindowManager.LayoutParams nomal = ((Activity) mContext).getWindow().getAttributes();
        nomal.alpha = 0.5f;
        ((Activity) mContext).getWindow().setAttributes(nomal);

        mPopupWindow.setOnDismissListener(onDismissListener);
    }

    /**
     * 中间显示
     *
     * @param mView
     * @param width
     * @param height
     */
    public void showCenterPopupWindow(View mView, int width, int height) {
        mPopupWindow = new PopupWindow(mView, width, height, true);
        mPopupWindow.setContentView(mView);
        mPopupWindow.setAnimationStyle(R.style.AnimationCenterFade);
        mPopupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
        mPopupWindow.update();
        mPopupWindow.setOnDismissListener(closeDismiss);
    }

    public static void setBackgroundAlpha(float bgAlpha, Context mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    // 弹窗消失时关闭阴影
    public PopupWindow.OnDismissListener closeDismiss = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            WindowManager.LayoutParams nomal = ((Activity) mContext).getWindow().getAttributes();
            nomal.alpha = 1f;
            ((Activity) mContext).getWindow().setAttributes(nomal);
        }
    };

    public void closePopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 设置弹窗动画
     *
     * @param animId
     * @return
     */
    public LiWindow setAnim(int animId) {
        if (mPopupWindow != null) {
            mPopupWindow.setAnimationStyle(animId);
        }
        return mLiWindow;
    }
}
