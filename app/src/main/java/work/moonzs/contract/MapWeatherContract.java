package work.moonzs.contract;

import retrofit2.Call;
import retrofit2.Response;
import work.moonzs.api.ApiService;
import work.moonzs.bean.AirNowResponse;
import work.moonzs.bean.DailyResponse;
import work.moonzs.bean.NewSearchCityResponse;
import work.moonzs.bean.NowResponse;
import work.moonzs.mvplibrary.base.BasePresenter;
import work.moonzs.mvplibrary.base.BaseView;
import work.moonzs.mvplibrary.net.NetCallBack;
import work.moonzs.mvplibrary.net.ServiceGenerator;

/**
 * 地图天气订阅器
 */
public class MapWeatherContract {
    public static class MapWeatherPresenter extends BasePresenter<IMapWeatherView> {
        /**
         * 搜索城市  需要把定位城市的id查询出来，然后通过这个id来查询详细的数据
         *
         * @param location 城市名
         */
        public void searchCity(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 2);
            service.newSearchCity(location, "exact").enqueue(new NetCallBack<NewSearchCityResponse>() {
                @Override
                public void onSuccess(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
                    if (getView() != null) {
                        getView().getNewSearchCityResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        /**
         * 实况天气
         *
         * @param location 城市名
         */
        public void nowWeather(String location) {
            // 得到构建之后的网络请求服务，这里的地址已经拼接完成，只差一个location
            ApiService service = ServiceGenerator.createService(ApiService.class, 0);
            // 设置请求回调 NetCallBack是重写请求回调
            service.nowWeather(location).enqueue(new NetCallBack<NowResponse>() {
                // 成功回调
                @Override
                public void onSuccess(Call<NowResponse> call, Response<NowResponse> response) {
                    // 当视图不为空时返回请求数据
                    if (getView() != null) {
                        getView().getNowResult(response);
                    }
                }

                // 失败回调
                @Override
                public void onFailed() {
                    // 当视图不为空时获取错误信息
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        /**
         * 天气预报 7天(白嫖的就只能看到3天)
         *
         * @param location 城市名
         */
        public void dailyWeather(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 0);
            service.dailyWeather("7d", location).enqueue(new NetCallBack<DailyResponse>() {
                @Override
                public void onSuccess(Call<DailyResponse> call, Response<DailyResponse> response) {
                    if (getView() != null) {
                        getView().getDailyResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        /**
         * 空气质量数据
         *
         * @param location 城市名
         */
        public void airNowWeather(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 0);
            service.airNowWeather(location).enqueue(new NetCallBack<AirNowResponse>() {
                @Override
                public void onSuccess(Call<AirNowResponse> call, Response<AirNowResponse> response) {
                    if (getView() != null) {
                        getView().getAirNowResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }


    }

    public interface IMapWeatherView extends BaseView {
        //搜索城市返回城市id  通过id才能查下面的数据,否则会提示400  V7
        void getNewSearchCityResult(Response<NewSearchCityResponse> response);

        //实况天气
        void getNowResult(Response<NowResponse> response);

        //天气预报  7天
        void getDailyResult(Response<DailyResponse> response);

        //空气质量
        void getAirNowResult(Response<AirNowResponse> response);

        //错误返回
        void getDataFailed();
    }
}
