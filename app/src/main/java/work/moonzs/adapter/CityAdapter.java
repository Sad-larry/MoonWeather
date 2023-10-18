package work.moonzs.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.moonzs.R;
import work.moonzs.bean.CityResponse;

/**
 * 市列表适配器
 */
public class CityAdapter extends BaseQuickAdapter<CityResponse.CityBean, BaseViewHolder> {

    public CityAdapter(int layoutResId, @Nullable List<CityResponse.CityBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CityResponse.CityBean cityBean) {
        // 市名称
        baseViewHolder.setText(R.id.tv_city, cityBean.getName());
        // 点击事件，点击进入区/县列表
        bindViewClickListener(baseViewHolder, R.id.item_city);
    }
}
