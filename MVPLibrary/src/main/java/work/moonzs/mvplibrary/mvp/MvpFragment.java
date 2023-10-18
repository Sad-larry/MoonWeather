package work.moonzs.mvplibrary.mvp;

import android.os.Bundle;

import work.moonzs.mvplibrary.base.BaseFragment;
import work.moonzs.mvplibrary.base.BasePresenter;
import work.moonzs.mvplibrary.base.BaseView;

/**
 * 适用于需要访问网络接口的Fragment
 */
public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {
    protected P mPresent;

    @Override
    public void initBeforeView(Bundle saveInstanceState) {
        mPresent = createPresent();
        mPresent.attach((BaseView) this);
    }

    protected abstract P createPresent();

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresent != null) {
            mPresent.detach((BaseView) this);
        }
    }
}
