package work.moonzs.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.moonzs.R;
import work.moonzs.bean.CityResponse;

public class ProvinceAdapter extends BaseQuickAdapter<CityResponse, BaseViewHolder> {
    public ProvinceAdapter(int layoutResId, @Nullable List<CityResponse> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CityResponse cityResponse) {
        // 省名称
        baseViewHolder.setText(R.id.tv_city, cityResponse.getName());
        // 点击后进入市级列表
        bindViewClickListener(baseViewHolder, R.id.item_city);
    }
}
