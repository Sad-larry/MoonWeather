package work.moonzs.ui;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;
import work.moonzs.R;
import work.moonzs.adapter.SevenDailyAdapter;
import work.moonzs.adapter.TodayDetailAdapter;
import work.moonzs.bean.AirNowResponse;
import work.moonzs.bean.DailyResponse;
import work.moonzs.bean.NewSearchCityResponse;
import work.moonzs.bean.NowResponse;
import work.moonzs.bean.TodayDetailBean;
import work.moonzs.contract.MapWeatherContract;
import work.moonzs.mvplibrary.mvp.MvpActivity;
import work.moonzs.utils.CodeToStringUtils;
import work.moonzs.utils.Constant;
import work.moonzs.utils.DateUtils;
import work.moonzs.utils.SPUtils;
import work.moonzs.utils.StatusBarUtil;
import work.moonzs.utils.ToastUtils;
import work.moonzs.utils.WeatherUtil;

/**
 * 地图天气
 */
public class MapWeatherActivity extends MvpActivity<MapWeatherContract.MapWeatherPresenter> implements MapWeatherContract.IMapWeatherView {
    // 地图控件
    @BindView(R.id.map_view)
    MapView mapView;
    // 重新定位按钮
    @BindView(R.id.btn_auto_location)
    FloatingActionButton btnAutoLocation;

    // 城市
    @BindView(R.id.tv_city)
    TextView tvCity;
    // 天气状态图片描述
    @BindView(R.id.iv_weather)
    ImageView ivWeather;
    // 温度
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    // 天气状态文字描述
    @BindView(R.id.tv_weather_state_tv)
    TextView tvWeatherStateTv;
    // 风力风向
    @BindView(R.id.tv_wind_info)
    TextView tvWindInfo;
    // 今日详情数据列表
    @BindView(R.id.rv_today_detail)
    RecyclerView rvTodayDetail;
    // 七天天气预报列表
    @BindView(R.id.rv_seven_day_daily)
    RecyclerView rvSevenDayDaily;
    // 15日天气预报数据
    @BindView(R.id.tv_more_daily)
    TextView tvMoreDaily;
    // 紫外线强度
    @BindView(R.id.tv_uvIndex)
    TextView tvUvIndex;
    // 湿度
    @BindView(R.id.tv_humidity)
    TextView tvHumidity;
    // 大气压
    @BindView(R.id.tv_pressure)
    TextView tvPressure;
    // 今日天气简要描述
    @BindView(R.id.tv_today_info)
    TextView tvTodayInfo;
    // 空气质量
    @BindView(R.id.tv_air)
    TextView tvAir;
    // 底部拖动布局
    @BindView(R.id.bottom_sheet_ray)
    RelativeLayout bottomSheetRay;

    // 城市id
    private String locationId;
    // 七天天气预报
    private List<DailyResponse.DailyDTO> dailyList = new ArrayList<>();
    // 当前天气详情
    List<TodayDetailBean> todayDetailList = new ArrayList<>();
    // 七天天气预报适配器
    private SevenDailyAdapter mSevenDailyAdapter;
    // 今天白天天气描述
    private String dayInfo;
    // 今天晚上天气描述
    private String nightInfo;
    // 底部控件
    private BottomSheetBehavior bottomSheetBehavior;


    // 定位
    private AMapLocationClient mLocationClient;
    private MyLocationListener mLocationListener = new MyLocationListener();
    // 高德地图
    private AMap aMap;
    // 标点也可以说是覆盖物
    private Marker marker;
    // 标点的图标
    private BitmapDescriptor bitmap;
    // 标点纬度
    private double markerLatitude = 0;
    // 标点经度
    private double markerLongitude = 0;
    // 定位纬度
    private double latitude;
    // 定位经度
    private double longitude;
    // 高德地图地址解析
    private GeocodeSearch geocodeSearch;

    @Override
    public void initData(Bundle saveInstanceState) {
        mapView.onCreate(saveInstanceState);
        // 控件初始化
        initView();
        // 初始化定位
        initLocation();
        // 初始化地图点击
        initMapOnClick();
    }

    private void initView() {
        // 透明状态栏
        StatusBarUtil.transparencyBar(context);
        // 状态栏黑色字体
        StatusBarUtil.StatusBarLightMode(context);

        AMapLocationClient.updatePrivacyShow(this, true, true);
        AMapLocationClient.updatePrivacyAgree(this, true);

        aMap = mapView.getMap();
        // 删除高德地图Logo 去不掉
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setLogoPosition(-150);

        try {
            geocodeSearch = new GeocodeSearch(this);
            geocodeSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
        } catch (AMapException e) {
            e.printStackTrace();
        }

        mSevenDailyAdapter = new SevenDailyAdapter(R.layout.item_seven_day_daily_list, dailyList);
        rvSevenDayDaily.setLayoutManager(new LinearLayoutManager(context));
        rvSevenDayDaily.setAdapter(mSevenDailyAdapter);

        // 获取behavior 控制 bottomsheet 的 显示 与隐藏
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetRay);
        // bottomSheet 的 状态改变 根据不同的状态 做不同的事情
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    // 收缩
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        // 手动定位时,收缩就要显示浮动按钮,因为这时候你可以操作地图了
                        if (markerLatitude != 0) {
                            //自动定位 显示自动定位按钮
                            btnAutoLocation.show();
                        }
                        break;
                    // 展开
                    case BottomSheetBehavior.STATE_EXPANDED:
                        // 手动定位时,展开就要隐藏浮动按钮,因为这时候你是没有办法操作地图的
                        if (markerLatitude != 0) {
                            // 自动定位 隐藏自动定位按钮
                            btnAutoLocation.hide();
                        }
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // slideOffset bottomSheet 的 移动距离
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_map_weather;
    }

    @Override
    protected MapWeatherContract.MapWeatherPresenter createPresent() {
        return new MapWeatherContract.MapWeatherPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        // 退出时销毁定位
        mLocationClient.stopLocation();
        // 关闭定位图层
        aMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @OnClick(R.id.btn_auto_location)
    public void onViewClicked() {
        markerLatitude = 0;
        markerLongitude = 0;
        // 清除标点
        marker.remove();
        initLocation();
    }

    /**
     * 搜索城市返回
     *
     * @param response
     */
    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<NewSearchCityResponse.LocationDTO> data = response.body().getLocation();
            if (data != null && data.size() > 0) {
                // 城市名
                tvCity.setText(data.get(0).getName());
                // 城市Id
                locationId = data.get(0).getId();

                showLoadingDialog();
                // 查询实况天气
                mPresent.nowWeather(locationId);
                // 查询天气预报
                mPresent.dailyWeather(locationId);
                // 空气质量
                mPresent.airNowWeather(locationId);
            } else {
                ToastUtils.showShortToast(context, "数据为空");
            }
        } else {
            tvCity.setText("查询城市失败");
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 实况天气返回
     *
     * @param response
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void getNowResult(Response<NowResponse> response) {
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            // 根据V7版本的原则，只要是200就一定有数据，我们可以不用做判空处理，但是，为了使程序不ANR，还是要做的，信自己得永生
            NowResponse data = response.body();
            if (data != null) {
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
                // 数据渲染显示出来
                // 温度
                tvTemperature.setText(data.getNow().getTemp());
                // 使用字体
                tvTemperature.setTypeface(typeface);
                // 天气状态文字描述
                tvWeatherStateTv.setText(data.getNow().getText());
                WeatherUtil.changeIcon(ivWeather, Integer.parseInt(data.getNow().getIcon()));
                tvWindInfo.setText(data.getNow().getWindDir() + data.getNow().getWindScale() + "级");
                // 湿度
                tvHumidity.setText(data.getNow().getHumidity() + "%");
                // 大气压
                tvPressure.setText(data.getNow().getPressure() + "hPa");
                // 今日详情
                todayDetailList.clear();
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_temp, data.getNow().getFeelsLike() + "°", "体感温度"));
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_precip, data.getNow().getPrecip() + "mm", "降水量"));
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_humidity, data.getNow().getHumidity() + "%", "湿度"));
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_pressure, data.getNow().getPressure() + "hPa", "大气压强"));
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_vis, data.getNow().getVis() + "KM", "能见度"));
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_cloud, data.getNow().getCloud() + "%", "云量"));

                TodayDetailAdapter adapter = new TodayDetailAdapter(R.layout.item_today_detail, todayDetailList);
                rvTodayDetail.setLayoutManager(new GridLayoutManager(context, 3));
                rvTodayDetail.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.showShortToast(context, "暂无实况天气数据");
            }
        } else {//其他状态返回提示文字
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 未来七天天气预报数据返回
     *
     * @param response
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void getDailyResult(Response<DailyResponse> response) {
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<DailyResponse.DailyDTO> data = response.body().getDaily();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    // 今天
                    if (data.get(i).getFxDate().equals(DateUtils.getNowDate())) {
                        dayInfo = data.get(i).getTextDay();
                        nightInfo = data.get(i).getTextNight();
                        tvUvIndex.setText(WeatherUtil.uvIndexInfo(data.get(i).getUvIndex()));
                    }
                }
                dailyList.clear();
                dailyList.addAll(data);
                mSevenDailyAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showShortToast(context, "天气预报数据为空");
            }
        } else {//异常状态码返回
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 空气质量数据返回
     *
     * @param response
     */
    @Override
    public void getAirNowResult(Response<AirNowResponse> response) {
        dismissLoadingDialog();
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            AirNowResponse.NowDTO data = response.body().getNow();
            if (data != null) {
                tvAir.setText("AQI " + data.getCategory());
                tvTodayInfo.setText("今天白天" + dayInfo + ",晚上" + nightInfo + "，现在" + tvTemperature.getText().toString() + "," + WeatherUtil.apiToTip(data.getCategory()));
            } else {
                ToastUtils.showShortToast(context, "空气质量数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 错误返回
     */
    @Override
    public void getDataFailed() {
        ToastUtils.showShortToast(context, "连接超时，稍后尝试。");
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.btn_auto_location, R.id.tv_more_daily})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 重新定位
            case R.id.btn_auto_location:
                markerLatitude = 0;
                markerLongitude = 0;
                // 清除标点
                marker.remove();
                break;
            // 15日天气预报详情
            case R.id.tv_more_daily:
                // TODO
//                ToastUtils.showShortToast(context, "15日天气预报");
                SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, true, context);
                SPUtils.putString(Constant.DISTRICT, locationId, context);
                SPUtils.putString(Constant.CITY, tvCity.getText().toString(), context);
                finish();
//                Intent intent = new Intent(context, MoreDailyActivity.class);
//                intent.putExtra("locationId", locationId);
//                intent.putExtra("cityName", tvCity.getText().toString());
//                startActivity(intent);
                break;
        }
    }

    /**
     * 定位SDK监听,
     */
    private class MyLocationListener implements AMapLocationListener {

        /**
         * 监听返回数据 MapView 销毁后不在处理新接收的位置
         *
         * @param aMapLocation 定位信息
         */
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //做null处理，避免APP崩溃
            if (aMapLocation == null || mapView == null) {
                return;
            }
            if (aMapLocation.getErrorCode() == 0) {
                if (markerLatitude == 0) {
                    // 自动定位
                    latitude = aMapLocation.getLatitude();
                    longitude = aMapLocation.getLongitude();
                    // 隐藏自动定位按钮
                    btnAutoLocation.hide();
                } else {
                    // 标点定位
                    latitude = markerLatitude;
                    longitude = markerLongitude;
                    // 显示自动定按钮
                    btnAutoLocation.show();
                }
                // 地理位置坐标
                LatLng latLng = new LatLng(latitude, longitude);
                LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);

                // 根据经纬度进行反编码
                geocodeSearch.getFromLocationAsyn(new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP));

                // 设置缩放级别
                aMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
                // 将地图移动到定位点
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AMapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * 开启定位
     */
    private void initLocation() {
        try {
            // 开启定位图层
            aMap.setMyLocationEnabled(true);
            // 初始化定位
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
            // 设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            // 设置定位蓝点的样式，无样式
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.radiusFillColor(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
            myLocationStyle.strokeColor(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
            aMap.setMyLocationStyle(myLocationStyle);

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

    /**
     * 初始化地图点击
     */
    private void initMapOnClick() {
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            // 地图单击事件回调函数
            @Override
            public void onMapClick(LatLng latLng) {
                // 设置marker图标
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);
                // 获取经纬度
                markerLatitude = latLng.latitude;
                markerLongitude = latLng.longitude;
                // 清除之前的图层
                aMap.clear();
                // 创建标点marker设置对象
                MarkerOptions options = new MarkerOptions()
                        // 设置标点的定位
                        .position(latLng)
                        // 设置标点图标
                        .icon(bitmap);
                // 在地图上显示标点
                marker = aMap.addMarker(options);
                // 点击地图之后重新定位
                initLocation();
            }
        });
    }

    private GeocodeSearch.OnGeocodeSearchListener onGeocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener() {

        /**
         * 反编码结果返回  就是通过坐标获取具体位置信息
         * @param regeocodeResult 反编码返回结果
         */

        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            if (regeocodeResult == null || i != AMapException.CODE_AMAP_SUCCESS) {
                // 没有检测到结果
                return;
            }
            RegeocodeAddress addressDetail = regeocodeResult.getRegeocodeAddress();
            // 需要的地址信息就在RegeocodeAddress里
            mPresent.searchCity(addressDetail.getDistrict());
        }

        /**
         * 编码结果返回  就是通过具体位置信息获取坐标
         * @param geocodeResult  编码返回结果
         */
        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };

}