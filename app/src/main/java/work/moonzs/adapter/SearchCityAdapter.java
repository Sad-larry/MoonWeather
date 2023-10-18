package work.moonzs.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.moonzs.R;
import work.moonzs.bean.NewSearchCityResponse;

/**
 * 搜索城市结果列表适配器
 */
public class SearchCityAdapter extends BaseQuickAdapter<NewSearchCityResponse.LocationDTO, BaseViewHolder> {

    public SearchCityAdapter(int layoutResId, @Nullable List<NewSearchCityResponse.LocationDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, NewSearchCityResponse.LocationDTO locationDTO) {
        baseViewHolder.setText(R.id.tv_city_name, locationDTO.getName());
        // 绑定点击事件
        bindViewClickListener(baseViewHolder, R.id.tv_city_name);
    }
}
