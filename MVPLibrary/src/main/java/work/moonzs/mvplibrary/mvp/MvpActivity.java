package work.moonzs.mvplibrary.mvp;

import android.os.Bundle;

import work.moonzs.mvplibrary.base.BaseActivity;
import work.moonzs.mvplibrary.base.BasePresenter;
import work.moonzs.mvplibrary.base.BaseView;

/**
 * 适用于需要访问网络接口的Activity
 */
public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
    protected P mPresent;

    @Override
    public void initBeforeView(Bundle saveInstanceState) {
        mPresent = createPresent();
        mPresent.attach((BaseView) this);
    }

    protected abstract P createPresent();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresent.detach((BaseView) this);
    }
}
