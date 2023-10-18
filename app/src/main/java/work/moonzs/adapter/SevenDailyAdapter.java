package work.moonzs.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.moonzs.R;
import work.moonzs.bean.DailyResponse;
import work.moonzs.utils.DateUtils;
import work.moonzs.utils.WeatherUtil;

public class SevenDailyAdapter extends BaseQuickAdapter<DailyResponse.DailyDTO, BaseViewHolder> {
    public SevenDailyAdapter(int layoutResId, @Nullable List<DailyResponse.DailyDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, DailyResponse.DailyDTO dailyDTO) {
        baseViewHolder.setText(R.id.tv_date, DateUtils.dateSplitPlus(dailyDTO.getFxDate()) +
                // 日期
                DateUtils.Week(dailyDTO.getFxDate()))
                // 最高温
                .setText(R.id.tv_temp_height, dailyDTO.getTempMax() + "℃")
                // 最低温
                .setText(R.id.tv_temp_low, " / " + dailyDTO.getTempMin() + "℃");
        // 天气状态图片
        ImageView weatherStateIcon = baseViewHolder.getView(R.id.iv_weather_state);
        // 获取天气状态码，根据状态码来显示图标
        int code = Integer.parseInt(dailyDTO.getIconDay());
        // 调用工具类中写好的方法
        WeatherUtil.changeIcon(weatherStateIcon, code);
    }
}
