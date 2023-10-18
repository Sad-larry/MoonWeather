package work.moonzs;

import static work.moonzs.mvplibrary.utils.RecyclerViewAnimation.runLayoutAnimationRight;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;
import work.moonzs.adapter.AreaAdapter;
import work.moonzs.adapter.CityAdapter;
import work.moonzs.adapter.DailyAdapter;
import work.moonzs.adapter.HourlyAdapter;
import work.moonzs.adapter.ProvinceAdapter;
import work.moonzs.bean.AirNowResponse;
import work.moonzs.bean.BiYingImgResponse;
import work.moonzs.bean.CityResponse;
import work.moonzs.bean.DailyResponse;
import work.moonzs.bean.HourlyResponse;
import work.moonzs.bean.LifestyleResponse;
import work.moonzs.bean.NewSearchCityResponse;
import work.moonzs.bean.NowResponse;
import work.moonzs.contract.WeatherContract;
import work.moonzs.eventbus.SearchCityEvent;
import work.moonzs.mvplibrary.mvp.MvpActivity;
import work.moonzs.mvplibrary.utils.AnimationUtil;
import work.moonzs.mvplibrary.utils.LiWindow;
import work.moonzs.mvplibrary.utils.SizeUtils;
import work.moonzs.mvplibrary.view.RoundProgressBar;
import work.moonzs.mvplibrary.view.WhiteWindmills;
import work.moonzs.ui.BackGroundManagerActivity;
import work.moonzs.ui.MapWeatherActivity;
import work.moonzs.ui.SearchCityActivity;
import work.moonzs.utils.CodeToStringUtils;
import work.moonzs.utils.Constant;
import work.moonzs.utils.DateUtils;
import work.moonzs.utils.SPUtils;
import work.moonzs.utils.StatusBarUtil;
import work.moonzs.utils.ToastUtils;
import work.moonzs.utils.WeatherUtil;

public class MainActivity extends MvpActivity<WeatherContract.WeatherPresenter> implements WeatherContract.IWeatherView {
    // 权限请求框架
    private RxPermissions rxPermissions;
    // 天气状况
    @BindView(R.id.tv_info)
    TextView tvInfo;
    // 温度
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    // 最高温和最低温
    @BindView(R.id.tv_low_height)
    TextView tvLowHeight;
    // 城市
    @BindView(R.id.tv_city)
    TextView tvCity;
    // 最近更新时间
    @BindView(R.id.tv_old_time)
    TextView tvOldTime;
    // 天气预报显示列表
    @BindView(R.id.rv)
    RecyclerView rv;
    // 紫外线
    @BindView(R.id.tv_uv)
    TextView tvUv;
    // 舒适度
    @BindView(R.id.tv_comf)
    TextView tvComf;
    // 旅游指数
    @BindView(R.id.tv_trav)
    TextView tvTrav;
    // 运动指数
    @BindView(R.id.tv_sport)
    TextView tvSport;
    // 洗车指数
    @BindView(R.id.tv_cw)
    TextView tvCw;
    // 空气指数
    @BindView(R.id.tv_air)
    TextView tvAir;
    // 穿衣指数
    @BindView(R.id.tv_drsg)
    TextView tvDrsg;
    // 感冒指数
    @BindView(R.id.tv_flu)
    TextView tvFlu;
    // 大风车
    @BindView(R.id.ww_big)
    WhiteWindmills wwBig;
    // 小风车
    @BindView(R.id.ww_small)
    WhiteWindmills wwSmall;
    // 风向
    @BindView(R.id.tv_wind_direction)
    TextView tvWindDirection;
    // 小风车
    @BindView(R.id.tv_wind_power)
    TextView tvWindPower;
    // 地图天气
    @BindView(R.id.iv_map)
    ImageView ivMap;
    // 更多功能
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    // 背景图
    @BindView(R.id.bg)
    LinearLayout bg;
    // 刷新布局
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    // 定位图标
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    // 逐小时天气显示列表
    @BindView(R.id.rv_hourly)
    RecyclerView rvHourly;
    // 污染指数圆环
    @BindView(R.id.rpb_aqi)
    RoundProgressBar rpbAqi;
    // PM10
    @BindView(R.id.tv_pm10)
    TextView tvPm10;
    // PM2.5
    @BindView(R.id.tv_pm25)
    TextView tvPm25;
    // 二氧化氮
    @BindView(R.id.tv_no2)
    TextView tvNo2;
    // 二氧化硫
    @BindView(R.id.tv_so2)
    TextView tvSo2;
    // 臭氧
    @BindView(R.id.tv_o3)
    TextView tvO3;
    // 一氧化碳
    @BindView(R.id.tv_co)
    TextView tvCo;


    // 图标显示标识，true显示，false不显示，只有定位的时候才为true，切换城市和常用城市为false
    private boolean flag = true;
    // 右上角的弹窗
    private PopupWindow mPopupWindow;
    private AnimationUtil animUtil;
    private float bgAlpha = 1f;
    private boolean bright = false;
    // 0.5s
    private static final long DURATION = 500;
    // 开始透明度
    private static final float START_ALPHA = 0.7f;
    // 结束透明度
    private static final float END_ALPHA = 1f;


    // ----------------------数据初始化开始---------------------------
    // 数据初始化 主线程 删除onCreate方法
    @Override
    public void initData(Bundle saveInstanceState) {
        // 因为这个框架里面已经放入了绑定，所以这行代码可以注释掉了。
        // ButterKnife.bind(this);
        // 透明状态栏
        StatusBarUtil.transparencyBar(context);
        // 天气预报列表初始化
        initList();
        // 实例化这个权限请求框架，否则会报错
        rxPermissions = new RxPermissions(this);
        // 调用 - 权限判断
        permissionVersion();
        // 刷新框架默认有下拉和上拉，所以禁用上拉，因为用不到
        refresh.setEnableLoadMore(false);
        // 初始化弹窗
        mPopupWindow = new PopupWindow(this);
        animUtil = new AnimationUtil();

        // 注册
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SearchCityEvent event) {
        flag = false;
        district = SPUtils.getString(Constant.DISTRICT, "", context);
        // 先获取城市ID再获取其他数据
        mPresent.newSearchCity(district);
    }

    // 绑定布局文件
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    // 绑定Presenter（必须）
    @Override
    protected WeatherContract.WeatherPresenter createPresent() {
        return new WeatherContract.WeatherPresenter();
    }

    // 天气预报数据列表
    List<DailyResponse.DailyDTO> dailyListV7 = new ArrayList<>();
    // 天气预报适配器
    DailyAdapter mAdapterDailyV7;
    // 逐小时天气预报数据列表
    List<HourlyResponse.HourlyDTO> hourlyListV7 = new ArrayList<>();
    // 逐小时预报适配器
    HourlyAdapter mAdapterHourlyV7;

    /**
     * 初始化天气预报 和 逐小时 数据列表
     */
    private void initList() {
        // 七天天气预报
        // 为适配器设置布局和数据源
        mAdapterDailyV7 = new DailyAdapter(R.layout.item_weather_forcast_list, dailyListV7);
        // 布局管理,默认是纵向
        // 为列表配置管理器
        rv.setLayoutManager(new LinearLayoutManager(this));
        // 为列表配置适配器
        rv.setAdapter(mAdapterDailyV7);
        // 未来七天七天预报列表item点击事件
        mAdapterDailyV7.addChildClickViewIds(R.id.item_forecast);
        mAdapterDailyV7.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                DailyResponse.DailyDTO data = dailyListV7.get(position);
                // 天气预报详情弹窗
                showForecastWindow(data);
            }
        });

        // 逐小时天气预报
        mAdapterHourlyV7 = new HourlyAdapter(R.layout.item_weather_hourly_list, hourlyListV7);
        LinearLayoutManager managerHourly = new LinearLayoutManager(context);
        // 设置列表为横向
        managerHourly.setOrientation(RecyclerView.HORIZONTAL);
        rvHourly.setLayoutManager(managerHourly);
        rvHourly.setAdapter(mAdapterHourlyV7);
        // 小时天气预报列表item点击事件
        mAdapterHourlyV7.addChildClickViewIds(R.id.item_hourly);
        mAdapterHourlyV7.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                // 赋值
                HourlyResponse.HourlyDTO data = hourlyListV7.get(position);
                // 小时天气详情弹窗
                showHourlyWindow(data);
            }
        });
    }

    /**
     * 显示逐三小时详情天气信息弹窗
     *
     * @param data
     */
    @SuppressLint("SetTextI18n")
    private void showHourlyWindow(HourlyResponse.HourlyDTO data) {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_hourly_detail, null);
        // 时间
        TextView tv_time = view.findViewById(R.id.tv_time);
        // 温度
        TextView tv_tem = view.findViewById(R.id.tv_tem);
        // 天气状态描述
        TextView tv_cond_txt = view.findViewById(R.id.tv_cond_txt);
        // 风向360角度
        TextView tv_wind_deg = view.findViewById(R.id.tv_wind_deg);
        // 风力
        TextView tv_wind_dir = view.findViewById(R.id.tv_wind_dir);
        // 风向
        TextView tv_wind_sc = view.findViewById(R.id.tv_wind_sc);
        // 风速
        TextView tv_wind_spd = view.findViewById(R.id.tv_wind_spd);
        // 相对湿度
        TextView tv_hum = view.findViewById(R.id.tv_hum);
        // 大气压强
        TextView tv_pres = view.findViewById(R.id.tv_pres);
        // 降水概率
        TextView tv_pop = view.findViewById(R.id.tv_pop);
        // 露点温度
        TextView tv_dew = view.findViewById(R.id.tv_dew);
        // 云量
        TextView tv_cloud = view.findViewById(R.id.tv_cloud);

        // 截去前面的字符，截取后面所有的字符，就剩下 22:00
        String time = DateUtils.updateTime(data.getFxTime());
        tv_time.setText(WeatherUtil.showTimeInfo(time) + time);
        tv_tem.setText(data.getTemp() + "℃");
        tv_cond_txt.setText(data.getText());
        tv_wind_deg.setText(data.getWind360() + "°");
        tv_wind_dir.setText(data.getWindDir());
        tv_wind_sc.setText(data.getWindScale() + "级");
        tv_wind_spd.setText(data.getWind360() + "公里/小时");
        tv_hum.setText(data.getHumidity() + "%");
        tv_pres.setText(data.getPressure() + "hPa");
        tv_pop.setText(data.getPop() + "%");
        tv_dew.setText(data.getDew() + "℃");
        tv_cloud.setText(data.getCloud() + "%");
        liWindow.showCenterPopupWindow(view, SizeUtils.dp2px(context, 300), SizeUtils.dp2px(context, 400));
    }

    /**
     * 显示天气预报详情弹窗
     *
     * @param data
     */
    @SuppressLint("SetTextI18n")
    private void showForecastWindow(DailyResponse.DailyDTO data) {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_forecast_detail, null);
        TextView tv_datetime = view.findViewById(R.id.tv_datetime);
        // 最高温
        TextView tv_tmp_max = view.findViewById(R.id.tv_tmp_max);
        // 最低温
        TextView tv_tmp_min = view.findViewById(R.id.tv_tmp_min);
        // 紫外线强度
        TextView tv_uv_index = view.findViewById(R.id.tv_uv_index);
        // 白天天气状态
        TextView tv_cond_txt_d = view.findViewById(R.id.tv_cond_txt_d);
        // 晚上天气状态
        TextView tv_cond_txt_n = view.findViewById(R.id.tv_cond_txt_n);
        // 风向360角度
        TextView tv_wind_deg = view.findViewById(R.id.tv_wind_deg);
        // 风向
        TextView tv_wind_dir = view.findViewById(R.id.tv_wind_dir);
        // 风力
        TextView tv_wind_sc = view.findViewById(R.id.tv_wind_sc);
        // 风速
        TextView tv_wind_spd = view.findViewById(R.id.tv_wind_spd);
        // 相对湿度
        TextView tv_hum = view.findViewById(R.id.tv_hum);
        // 大气压强
        TextView tv_pres = view.findViewById(R.id.tv_pres);
        // 降水量
        TextView tv_pcpn = view.findViewById(R.id.tv_pcpn);
        // 云量
        TextView tv_cloud = view.findViewById(R.id.tv_cloud);
        // 能见度
        TextView tv_vis = view.findViewById(R.id.tv_vis);

        // 时间日期
        tv_datetime.setText(data.getFxDate() + "   " + DateUtils.Week(data.getFxDate()));
        tv_tmp_max.setText(data.getTempMax() + "℃");
        tv_tmp_min.setText(data.getTempMin() + "℃");
        tv_uv_index.setText(data.getUvIndex());
        tv_cond_txt_d.setText(data.getTextDay());
        tv_cond_txt_n.setText(data.getTextNight());
        tv_wind_deg.setText(data.getWind360Day() + "°");
        tv_wind_dir.setText(data.getWindDirDay());
        tv_wind_sc.setText(data.getWindScaleDay() + "级");
        tv_wind_spd.setText(data.getWindSpeedDay() + "公里/小时");
        tv_hum.setText(data.getHumidity());
        tv_pres.setText(data.getPressure());
        tv_pcpn.setText(data.getPrecip());
        tv_cloud.setText(data.getCloud() + "%");
        tv_vis.setText(data.getVis());
        liWindow.showCenterPopupWindow(view, SizeUtils.dp2px(context, 300), SizeUtils.dp2px(context, 500));
    }


    // 权限判断
    @SuppressLint("ObsoleteSdkInt")
    private void permissionVersion() {
        // Android6.0或以上
        if (Build.VERSION.SDK_INT >= 23) {
            // 调用 - 动态权限申请
            permissionsRequest();
        } else {
            //发现只要权限在AndroidManifest.xml中注册过，均会认为该权限granted  提示一下即可
            ToastUtils.showShortToast(this, "你的版本在Android6.0以下，不需要动态申请权限。");
        }
    }

    // 动态权限申请
    @SuppressLint("CheckResult")
    private void permissionsRequest() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(aBoolean -> {
                    // 申请成功
                    if (aBoolean) {
                        // 调用 - 得到权限之后开始定位
                        startLocation();
                    } else {
                        ToastUtils.showShortToast(MainActivity.this, "权限未开启");
                    }
                });
    }

    // ----------------------数据初始化结束---------------------------

    // ----------------------功能弹窗开始---------------------------

    // 跳转其他页面时才为true
    private boolean flagOther = false;

    @OnClick({R.id.iv_map, R.id.iv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_map:
                // 地图天气
                startActivity(new Intent(context, MapWeatherActivity.class));
                break;
            case R.id.iv_add:
                // 显示更多功能
                showAddWindow();
                // 计算动画时间
                toggleBright();
                break;
        }

    }

    /**
     * 更多功能弹窗
     */
    private void showAddWindow() {
        // 设置布局文件
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.window_add, null));
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        mPopupWindow.setAnimationStyle(work.moonzs.mvplibrary.R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量
        mPopupWindow.showAsDropDown(ivAdd, -100, 0);
        // 设置pop关闭监听，用于改变背景透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });

        // 绑定布局中的控件
        TextView changeCity = mPopupWindow.getContentView().findViewById(R.id.tv_change_city);
        TextView changeBg = mPopupWindow.getContentView().findViewById(R.id.tv_change_bg);
        TextView more = mPopupWindow.getContentView().findViewById(R.id.tv_more);
        TextView searchCity = mPopupWindow.getContentView().findViewById(R.id.tv_search_city);
        // 改变城市
        changeCity.setOnClickListener(view -> {
            showCityWindow();
            mPopupWindow.dismiss();
        });
        // 切换背景
        changeBg.setOnClickListener(view -> {
            // 放入缓存
            SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, true, context);
            SPUtils.putString(Constant.DISTRICT, district, context);
            SPUtils.putString(Constant.CITY, city, context);

            startActivity(new Intent(context, BackGroundManagerActivity.class));
            mPopupWindow.dismiss();
        });
        // 搜索城市
        searchCity.setOnClickListener(view -> {
            SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, false, context);
            startActivity(new Intent(context, SearchCityActivity.class));
            mPopupWindow.dismiss();
        });
        // 更多功能
        more.setOnClickListener(view -> {
            ToastUtils.showShortToast(context, "你点击了更多功能");
            mPopupWindow.dismiss();
        });
    }

    /**
     * 计算动画时间
     */
    private void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimationUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListener(new AnimationUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // 在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }

    /**
     * 用于改变背景的透明度，到达变暗的效果
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在数据请求之前放在加载等待弹窗，返回结果后关闭弹窗
        showLoadingDialog();
        flagOther = SPUtils.getBoolean(Constant.FLAG_OTHER_RETURN, false, context);
        if (flagOther) {
            // 取出缓存
            district = SPUtils.getString(Constant.DISTRICT, "", context);
            city = SPUtils.getString(Constant.CITY, "", context);

            // 先获取城市ID再获取其他数据
            mPresent.newSearchCity(district);
        } else {
            dismissLoadingDialog();
        }
        // 是否开启了切换背景
        isOpenChangeBg();
    }

    /**
     * 判断是否开启了切换背景，没有开启则用默认的背景
     */
    private void isOpenChangeBg() {
        // 每日图片
        boolean isEverydayImg = SPUtils.getBoolean(Constant.EVERYDAY_IMG, false, context);
        // 图片列表
        boolean isImgList = SPUtils.getBoolean(Constant.IMG_LIST, false, context);
        // 手动定义
        boolean isCustomImg = SPUtils.getBoolean(Constant.CUSTOM_IMG, false, context);
        // 因为只有有一个为true，其他两个就都会是false,所以可以一个一个的判断
        if (!isEverydayImg && !isImgList && !isCustomImg) {
            // 当所有开关都没有打开的时候用默认的图片
            bg.setBackgroundResource(R.drawable.bg);
        } else {
            if (isEverydayImg) {
                // 开启每日一图
                mPresent.biying(context);
            } else if (isImgList) {
                // 开启图片列表
                int position = SPUtils.getInt(Constant.IMG_POSITION, -1, context);
                switch (position) {
                    case 0:
                        bg.setBackgroundResource(R.drawable.img_1);
                        break;
                    case 1:
                        bg.setBackgroundResource(R.drawable.img_2);
                        break;
                    case 2:
                        bg.setBackgroundResource(R.drawable.img_3);
                        break;
                    case 3:
                        bg.setBackgroundResource(R.drawable.img_4);
                        break;
                    case 4:
                        bg.setBackgroundResource(R.drawable.img_5);
                        break;
                    case 5:
                        bg.setBackgroundResource(R.drawable.img_6);
                        break;
                }
            } else if (isCustomImg) {
                // 手动定义
                String imgPath = SPUtils.getString(Constant.CUSTOM_IMG_PATH, "", context);
                Glide.with(context)
                        .asBitmap()
                        .load(imgPath)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                                bg.setBackground(drawable);
                            }
                        });

            }
        }
    }

    // ----------------------功能弹窗结束---------------------------

    // ----------------------获取定位信息开始---------------------------

    // 声明定位器
    private AMapLocationClient mLocationClient = null;
    // 声明定位器回调监听器
    private MyLocationListener mLocationListener = new MyLocationListener();

    // 区/县 保存当前位置信息，也方便更换城市后也能进行下拉刷新
    private String district;
    // 国控站点数据，用于请求空气质量
    private String city;
    //城市id，用于查询城市数据 V7版本中才有
    private String locationId = null;

    /**
     * 定位返回结果
     */
    private class MyLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation.getErrorCode() != 0) {
                // 定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AMapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                return;
            }
            // 获取区/县
            district = aMapLocation.getDistrict();
            // 获取城市信息
            city = aMapLocation.getCity();
            // 在数据请求之前放在加载等待弹窗，返回结果后关闭弹窗
            showLoadingDialog();
            // 定位返回时，先获取到城市ID，再在结果返回值中再进行下一步的数据查询
            mPresent.newSearchCity(district);
            // 下拉刷新
            refresh.setOnRefreshListener(refreshLayout -> {
                // 下拉刷新时，先获取到城市ID，再在结果返回值中再进行下一步的数据查询
                mPresent.newSearchCity(district);
            });
        }
    }

    /**
     * 开启定位
     */
    private void startLocation() {
        AMapLocationClient.updatePrivacyShow(this, true, true);
        AMapLocationClient.updatePrivacyAgree(this, true);

        try {
            // 初始化定位
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
            // 设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            // 初始化AMapLocationClientOption对象
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            // 设置是否返回地址信息
            mLocationOption.setNeedAddress(true);
            // 设置获取一次结果
            mLocationOption.setOnceLocation(true);
            // 设置定位模式为高精度模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 启动定位
            mLocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 判断GPS是否开启
    private boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    // ----------------------获取定位数据结束---------------------------


    // ----------------------城市弹窗数据的渲染开始---------------------------
    // 字符串列表
    private List<String> list;
    // 省列表数据
    private List<CityResponse> provinceList;
    // 市列表数据
    private List<CityResponse.CityBean> cityList;
    // 区/县列表数据
    private List<CityResponse.CityBean.AreaBean> areaList;
    // 省数据适配器
    ProvinceAdapter provinceAdapter;
    // 市数据适配器
    CityAdapter cityAdapter;
    // 区/县数据适配器
    AreaAdapter areaAdapter;
    // 标题
    String provinceTitle;
    // 自定义弹窗
    LiWindow liWindow;

    /**
     * 城市弹窗
     */
    private void showCityWindow() {
        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();
        list = new ArrayList<>();
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_city_list, null);
        ImageView areaBack = view.findViewById(R.id.iv_back_area);
        ImageView cityBack = view.findViewById(R.id.iv_back_city);
        TextView windowTitle = view.findViewById(R.id.tv_title);
        RecyclerView recyclerView = view.findViewById(R.id.rv);
        liWindow.showRightPopupWindow(view);
        // 调用 - 加载城市列表数据
        initCityData(recyclerView, areaBack, cityBack, windowTitle);
    }

    /**
     * 初始化省数据，读取省数据并显示到列表中
     *
     * @param recyclerView
     * @param areaBack
     * @param cityBack
     * @param windowTitle
     */
    @SuppressLint("NotifyDataSetChanged")
    private void initCityData(RecyclerView recyclerView, ImageView areaBack, ImageView cityBack, TextView windowTitle) {
        try {
            // 数据存储在City.txt文件中
            InputStream inputStream = getResources().getAssets().open("City.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String lines = bufferedReader.readLine();
            while (lines != null) {
                stringBuilder.append(lines);
                lines = bufferedReader.readLine();
            }
            final JSONArray Data = new JSONArray(stringBuilder.toString());
            // 循环这个文件数据，获取数组中每个省对象的名字
            for (int i = 0; i < Data.length(); i++) {
                JSONObject provinceJsonObject = Data.getJSONObject(i);
                String provinceName = provinceJsonObject.getString("name");
                CityResponse response = new CityResponse();
                response.setName(provinceName);
                provinceList.add(response);
            }
            // 定义省份显示适配器
            provinceAdapter = new ProvinceAdapter(R.layout.item_city_list, provinceList);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(provinceAdapter);
            provinceAdapter.notifyDataSetChanged();
            // 动画展示
            runLayoutAnimationRight(recyclerView);

            provinceAdapter.addChildClickViewIds(R.id.tv_city);
            provinceAdapter.setOnItemChildClickListener((adapter1, view1, position1) -> {
                try {
                    // 返回上一级数据
                    cityBack.setVisibility(View.VISIBLE);
                    cityBack.setOnClickListener(view11 -> {
                        recyclerView.setAdapter(provinceAdapter);
                        provinceAdapter.notifyDataSetChanged();
                        cityBack.setVisibility(View.GONE);
                        windowTitle.setText("中国");
                    });

                    // 根据当前位置的省份所在数组位置，获取城市的数组
                    JSONObject provinceObject = Data.getJSONObject(position1);
                    windowTitle.setText(provinceList.get(position1).getName());
                    provinceTitle = provinceList.get(position1).getName();
                    final JSONArray cityArray = provinceObject.getJSONArray("city");

                    // 更新列表数据
                    if (cityList != null) {
                        cityList.clear();
                    }

                    for (int i = 0; i < cityArray.length(); i++) {
                        JSONObject cityObj = cityArray.getJSONObject(i);
                        String cityName = cityObj.getString("name");
                        CityResponse.CityBean response = new CityResponse.CityBean();
                        response.setName(cityName);
                        cityList.add(response);
                    }

                    cityAdapter = new CityAdapter(R.layout.item_city_list, cityList);
                    LinearLayoutManager manager1 = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(manager1);
                    recyclerView.setAdapter(cityAdapter);
                    cityAdapter.notifyDataSetChanged();
                    runLayoutAnimationRight(recyclerView);

                    cityAdapter.addChildClickViewIds(R.id.tv_city);
                    cityAdapter.setOnItemChildClickListener((adapter2, view2, position2) -> {
                        try {
                            // 返回上一级数据
                            areaBack.setVisibility(View.VISIBLE);
                            areaBack.setOnClickListener(view21 -> {
                                recyclerView.setAdapter(cityAdapter);
                                cityAdapter.notifyDataSetChanged();
                                areaBack.setVisibility(View.GONE);
                                windowTitle.setText(provinceTitle);
                                areaList.clear();
                            });
                            // 获取区/县的上级市，用于请求空气质量数据API接口
                            city = cityList.get(position2).getName();
                            // 根据当前城市数组位置，获取地区数据
                            windowTitle.setText(cityList.get(position2).getName());
                            JSONObject cityJsonObj = cityArray.getJSONObject(position2);
                            JSONArray areaJsonArray = cityJsonObj.getJSONArray("area");
                            if (areaList != null) {
                                areaList.clear();
                            }
                            if (list != null) {
                                list.clear();
                            }
                            for (int i = 0; i < areaJsonArray.length(); i++) {
                                list.add(areaJsonArray.getString(i));
                            }
                            for (int j = 0; j < list.size(); j++) {
                                CityResponse.CityBean.AreaBean response = new CityResponse.CityBean.AreaBean();
                                response.setName(list.get(j));
                                areaList.add(response);
                            }
                            areaAdapter = new AreaAdapter(R.layout.item_city_list, areaList);
                            LinearLayoutManager manager2 = new LinearLayoutManager(context);

                            recyclerView.setLayoutManager(manager2);
                            recyclerView.setAdapter(areaAdapter);
                            areaAdapter.notifyDataSetChanged();
                            runLayoutAnimationRight(recyclerView);

                            areaAdapter.addChildClickViewIds(R.id.tv_city);
                            areaAdapter.setOnItemChildClickListener((adapter3, view3, position3) -> {
                                showLoadingDialog();
                                // 选中的区/县赋值给这个全局变量
                                district = areaList.get(position3).getName();

                                // 切换城市时 获取城市ID再获取其他数据
                                mPresent.newSearchCity(district);

                                // 切换城市得到的城市不属于定位，因此这里隐藏定位图标
                                flag = false;
                                // 关闭弹窗
                                liWindow.closePopupWindow();
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------城市弹窗数据的渲染结束---------------------------


    // ----------------------重写天气订阅器方法开始---------------------------

    /**
     * 通过定位到的地址 / 城市切换得到的地址  都需要查询出对应的城市id才行，所以在V7版本中，这是第一步接口
     *
     * @param response response
     */
    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {
        // 关闭刷新
        refresh.finishRefresh();
        // 关闭弹窗
        dismissLoadingDialog();
        if (mLocationClient != null) {
            // 数据返回后关闭定位
            mLocationClient.stopLocation();
        }
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            if (response.body().getLocation() != null && response.body().getLocation().size() > 0) {
                // 城市名
                tvCity.setText(response.body().getLocation().get(0).getName());
                // 城市Id
                locationId = response.body().getLocation().get(0).getId();

                showLoadingDialog();
                // 查询实况天气
                mPresent.nowWeather(locationId);
                // 查询天气预报
                mPresent.dailyWeather(locationId);
                // 查询逐小时天气预报
                mPresent.hourlyWeather(locationId);
                // 空气质量
                mPresent.airNowWeather(locationId);
                // 生活指数
                mPresent.lifestyle(locationId);
            } else {
                ToastUtils.showShortToast(context, "数据为空");
            }
        } else {
            tvCity.setText("查询城市失败");
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 实况天气数据返回
     *
     * @param response
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void getNowResult(Response<NowResponse> response) {
        // 关闭弹窗
        dismissLoadingDialog();
        // 200则成功返回数据
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            // 根据V7版本的原则，只要是200就一定有数据，我们可以不用做判空处理，但是，为了使程序不ANR，还是要做的，信自己得永生
            NowResponse data = response.body();
            if (data != null) {
                // 数据渲染显示出来
                // 温度
                tvTemperature.setText(data.getNow().getTemp());
                // 显示定位图标
                ivLocation.setVisibility(flag ? View.VISIBLE : View.GONE);
                // 天气状况
                tvInfo.setText(data.getNow().getText());
                // 截去前面的字符，保留后面所有的字符，就剩下 22:00
                String time = DateUtils.updateTime(data.getUpdateTime());
                tvOldTime.setText("上次更新时间:" + WeatherUtil.showTimeInfo(time) + time);
                // 风向
                tvWindDirection.setText("风向 " + data.getNow().getWindDir());
                // 风向
                tvWindDirection.setText("风向 " + data.getNow().getWindDir());
                // 风力
                tvWindPower.setText("风力 " + data.getNow().getWindScale() + "级");
                // 大小风车转动
                wwBig.startRotate();
                wwSmall.startRotate();
            } else {
                ToastUtils.showShortToast(context, "暂无实况天气数据");
            }
        } else {//其他状态返回提示文字
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 查询天气预报，请求成功后的数据返回
     *
     * @param response
     */
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void getDailyResult(Response<DailyResponse> response) {
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<DailyResponse.DailyDTO> data = response.body().getDaily();
            if (data != null && data.size() > 0) {
                tvLowHeight.setText(data.get(0).getTempMin() + "/"
                        + data.get(0).getTempMax() + "℃");
                // 添加数据之前先清除
                dailyListV7.clear();
                // 添加数据
                dailyListV7.addAll(data);
                // 刷新列表
                mAdapterDailyV7.notifyDataSetChanged();
            } else {
                ToastUtils.showShortToast(context, "天气预报数据为空");
            }
        } else {//异常状态码返回
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 逐小时天气预报返回
     *
     * @param response
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void getHourlyResult(Response<HourlyResponse> response) {
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<HourlyResponse.HourlyDTO> data = response.body().getHourly();
            if (data != null && data.size() > 0) {
                hourlyListV7.clear();
                hourlyListV7.addAll(data);
                mAdapterHourlyV7.notifyDataSetChanged();
            } else {
                ToastUtils.showShortToast(context, "逐小时预报数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 空气质量数据返回
     */
    @Override
    public void getAirNowResult(Response<AirNowResponse> response) {
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            AirNowResponse.NowDTO data = response.body().getNow();
            if (data != null) {
                // 污染指数
                // 最大进度，用于计算
                rpbAqi.setMaxProgress(300);
                // 设置显示最小值
                rpbAqi.setMinText("0");
                rpbAqi.setMinTextSize(32f);
                // 设置显示最大值
                rpbAqi.setMaxText("300");
                rpbAqi.setMaxTextSize(32f);
                // 当前进度
                rpbAqi.setProgress(Float.parseFloat(data.getAqi()));
                // 圆弧的颜色
                rpbAqi.setArcBgColor(getResources().getColor(work.moonzs.mvplibrary.R.color.arc_bg_color));
                // 进度圆弧的颜色
                rpbAqi.setProgressColor(getResources().getColor(work.moonzs.mvplibrary.R.color.arc_progress_color));
                // 空气质量描述  取值范围：优，良，轻度污染，中度污染，重度污染，严重污染
                rpbAqi.setFirstText(data.getCategory());
                // 第一行文本的字体大小
                rpbAqi.setFirstTextSize(44f);
                // 空气质量值
                rpbAqi.setSecondText(data.getAqi());
                // 第二行文本的字体大小
                rpbAqi.setSecondTextSize(64f);
                rpbAqi.setMinText("0");
                rpbAqi.setMinTextColor(getResources().getColor(work.moonzs.mvplibrary.R.color.arc_progress_color));

                //PM10
                tvPm10.setText(data.getPm10());
                //PM2.5
                tvPm25.setText(data.getPm2p5());
                //二氧化氮
                tvNo2.setText(data.getNo2());
                //二氧化硫
                tvSo2.setText(data.getSo2());
                //臭氧
                tvO3.setText(data.getO3());
                //一氧化碳
                tvCo.setText(data.getCo());
            } else {
                ToastUtils.showShortToast(context, "空气质量数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 查询生活指数，请求成功后数据返回
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void getLifestyleResult(Response<LifestyleResponse> response) {
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<LifestyleResponse.DailyDTO> data = response.body().getDaily();
            if (data != null && data.size() > 0) {
                for (LifestyleResponse.DailyDTO dailyDTO : data) {
                    TextView textView = null;
                    switch (dailyDTO.getType()) {
                        case "1":
                            textView = tvSport;
                            break;
                        case "2":
                            textView = tvCw;
                            break;
                        case "3":
                            textView = tvDrsg;
                            break;
                        case "5":
                            textView = tvUv;
                            break;
                        case "6":
                            textView = tvTrav;
                            break;
                        case "8":
                            textView = tvComf;
                            break;
                        case "9":
                            textView = tvFlu;
                            break;
                        case "10":
                            textView = tvAir;
                            break;
                    }
                    if (textView != null) {
                        textView.setText(dailyDTO.getName() + ":" + dailyDTO.getText());
                    }
                }
            } else {
                ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
            }
        }
    }

    /**
     * 获取必应每日一图
     */
    @Override
    public void getBiYingResult(Response<BiYingImgResponse> response) {
        dismissLoadingDialog();
        if (response.body() != null) {
            if (response.body().getImages() != null) {
                // 得到的图片地址是没有前缀的，所以加上前缀否则显示不出来
                String imgUrl = "http://cn.bing.com" + response.body().getImages().get(0).getUrl();
                Glide.with(context)
                        .asBitmap()
                        .load(imgUrl)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                                bg.setBackground(drawable);
                            }
                        });
            } else {
                ToastUtils.showShortToast(context, "没有得到图片");
            }
        } else {
            ToastUtils.showShortToast(context, "没有必应每日一图数据");
        }
    }


    /**
     * 天气数据请求失败返回
     */
    @Override
    public void getWeatherDataFailed() {
        // 关闭刷新
        refresh.finishRefresh();
        // 关闭弹窗
        dismissLoadingDialog();
        ToastUtils.showShortToast(context, "天气数据获取异常");
    }


    // 数据请求失败返回
    @Override
    public void getDataFailed() {
        // 关闭刷新
        refresh.finishRefresh();
        // 关闭弹窗
        dismissLoadingDialog();
        //这里的context是框架中封装好的，等同于this
        ToastUtils.showShortToast(context, "网络异常");
    }

    // ----------------------重写天气订阅器方法结束---------------------------

    // ----------------------页面销毁开始---------------------------

    @Override
    protected void onDestroy() {
        // 停止风车转动
        wwBig.stop();
        wwSmall.stop();
        // 解注
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    // ----------------------页面销毁结束---------------------------
}