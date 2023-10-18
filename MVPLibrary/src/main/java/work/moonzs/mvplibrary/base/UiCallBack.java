package work.moonzs.mvplibrary.base;

import android.os.Bundle;

/**
 * UI回调接口
 */
public interface UiCallBack {
    // 初始化savedInstanceState
    void initBeforeView(Bundle saveInstanceState);

    // 初始化
    void initData(Bundle saveInstanceState);

    // 布局
    int getLayoutId();
}
