package work.moonzs.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.moonzs.R;
import work.moonzs.bean.TodayDetailBean;

public class TodayDetailAdapter extends BaseQuickAdapter<TodayDetailBean, BaseViewHolder> {
    public TodayDetailAdapter(int layoutResId, @Nullable List<TodayDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, TodayDetailBean todayDetailBean) {
        ImageView imageView = baseViewHolder.getView(R.id.iv_icon);
        imageView.setImageResource(todayDetailBean.getIcon());//图标
        baseViewHolder.setText(R.id.tv_value, todayDetailBean.getValue())//值
                .setText(R.id.tv_name, todayDetailBean.getName());//名称
    }
}
