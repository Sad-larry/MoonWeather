package work.moonzs.mvplibrary.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.Unbinder;
import work.moonzs.mvplibrary.R;
import work.moonzs.mvplibrary.kit.KnifeKit;

/**
 * 用于不需要请求网络接口的BaseFragment
 */
public abstract class BaseFragment extends Fragment implements UiCallBack {
    protected View rootView;
    protected LayoutInflater layoutInflater;
    protected Activity context;
    private Unbinder unbinder;
    private Dialog mDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeView(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutInflater = inflater;
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), null);
            unbinder = KnifeKit.bind(this, rootView);
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(rootView);
            }
        }
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public void initBeforeView(Bundle saveInstanceState) {

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
}
