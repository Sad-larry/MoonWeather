package work.moonzs.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.moonzs.R;
import work.moonzs.bean.WeatherForecastResponse;
import work.moonzs.utils.WeatherUtil;

/**
 * 天气预报列表展示适配器
 */
public class WeatherForecastAdapter extends BaseQuickAdapter<WeatherForecastResponse.DailyDTO, BaseViewHolder> {


    public WeatherForecastAdapter(int layoutResId, @Nullable List<WeatherForecastResponse.DailyDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, WeatherForecastResponse.DailyDTO dailyDTO) {
        // 日期
        baseViewHolder.setText(R.id.tv_date, dailyDTO.getFxDate())
                // 天气
//                .setText(R.id.tv_info, dailyDTO.getTextDay())
                // 最低温、最高温
                .setText(R.id.tv_low_and_height, dailyDTO.getTempMin() + "/" + dailyDTO.getTempMax() + "℃");
        // 天气状态图片
        ImageView weatherStateIcon = baseViewHolder.getView(R.id.iv_weather_state);
        // 获取天气状态码
        int code = Integer.parseInt(dailyDTO.getIconDay());
        // 调用工具类修改对应状态码修改图片
        WeatherUtil.changeIcon(weatherStateIcon, code);

        bindViewClickListener(baseViewHolder, R.id.item_forecast);
    }
}
