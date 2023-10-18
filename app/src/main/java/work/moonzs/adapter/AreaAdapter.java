package work.moonzs.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.moonzs.R;
import work.moonzs.bean.CityResponse;

/**
 * 区/县适配器
 */
public class AreaAdapter extends BaseQuickAdapter<CityResponse.CityBean.AreaBean, BaseViewHolder> {
    public AreaAdapter(int layoutResId, @Nullable List<CityResponse.CityBean.AreaBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CityResponse.CityBean.AreaBean areaBean) {
        // 区/县的名称
        baseViewHolder.setText(R.id.tv_city, areaBean.getName());
        // 点击事件，点击之后得到区/县，然后查找天气数据
        bindViewClickListener(baseViewHolder, R.id.item_city);
    }
}
