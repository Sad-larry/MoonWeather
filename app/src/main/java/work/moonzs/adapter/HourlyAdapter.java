package work.moonzs.adapter;

import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.moonzs.R;
import work.moonzs.bean.HourlyResponse;
import work.moonzs.utils.DateUtils;
import work.moonzs.utils.WeatherUtil;

/**
 * 逐小时预报数据列表适配器
 */
public class HourlyAdapter extends BaseQuickAdapter<HourlyResponse.HourlyDTO, BaseViewHolder> {
    public HourlyAdapter(int layoutResId, @Nullable List<HourlyResponse.HourlyDTO> data) {
        super(layoutResId, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, HourlyResponse.HourlyDTO hourlyDTO) {
        //首先是对时间格式的处理,因为拿到的时间是 2022-06-25T22:00+08:00 要改成 晚上22:00
        //分两步，第一个是字符串的截取，第二个是时间段的判断返回文字描述
        //截去前面的字符，保留后面所有的字符，就剩下 22:00
        String time = DateUtils.updateTime(hourlyDTO.getFxTime());
        baseViewHolder.setText(R.id.tv_time, WeatherUtil.showTimeInfo(time) + time)
                .setText(R.id.tv_temperature, hourlyDTO.getTemp() + "℃");

        // 天气状态图片
        ImageView weatherStateIcon = baseViewHolder.getView(R.id.iv_weather_state);
        int code = Integer.parseInt(hourlyDTO.getIcon());
        WeatherUtil.changeIcon(weatherStateIcon, code);

        bindViewClickListener(baseViewHolder, R.id.item_hourly);
    }
}
