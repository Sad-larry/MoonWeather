package work.moonzs.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.moonzs.R;
import work.moonzs.bean.HourlyResponse;
import work.moonzs.utils.WeatherUtil;

public class WeatherHourlyAdapter extends BaseQuickAdapter<HourlyResponse.HourlyDTO, BaseViewHolder> {
    public WeatherHourlyAdapter(int layoutResId, @Nullable List<HourlyResponse.HourlyDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, HourlyResponse.HourlyDTO hourlyDTO) {
        //首先是对时间格式的处理,因为拿到的时间是 2020-04-28 22:00 要改成 晚上22:00
        //分两步，第一个是字符串的截取，第二个是时间段的判断返回文字描述
        String datetime = hourlyDTO.getFxTime();
        //截去前面的字符，保留后面所有的字符，就剩下 22:00
        String time = datetime.substring(11);
        baseViewHolder.setText(R.id.tv_time, WeatherUtil.showTimeInfo(time) + time.substring(0, 5))
                .setText(R.id.tv_temperature, hourlyDTO.getTemp() + "℃");
        // 天气状态图片
        ImageView weatherStateIcon = baseViewHolder.getView(R.id.iv_weather_state);
        int code = Integer.parseInt(hourlyDTO.getIcon());
        WeatherUtil.changeIcon(weatherStateIcon, code);

        bindViewClickListener(baseViewHolder, R.id.item_hourly);
    }
}
