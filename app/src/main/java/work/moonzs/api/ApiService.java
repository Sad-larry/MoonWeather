package work.moonzs.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import work.moonzs.bean.AirNowCityResponse;
import work.moonzs.bean.AirNowResponse;
import work.moonzs.bean.BiYingImgResponse;
import work.moonzs.bean.DailyResponse;
import work.moonzs.bean.HourlyResponse;
import work.moonzs.bean.LifestyleResponse;
import work.moonzs.bean.NewSearchCityResponse;
import work.moonzs.bean.NowResponse;
import work.moonzs.bean.SearchCityResponse;
import work.moonzs.bean.TodayResponse;
import work.moonzs.bean.WeatherForecastResponse;

/**
 * API服务接口
 */
public interface ApiService {
    /**
     * 当天天气查询
     * https://devapi.qweather.com/v7/weather/now?key=fa242ae2dd774d2f859210f2e1724bb4&location=101010100
     * 将地址进一步拆分，将可变的一部分放在注解@GET的地址里面，其中
     * /v7/weather/now?key=fa242ae2dd774d2f859210f2e1724 这一部分在这个接口中又是不变的，变的是location的值
     * 所以将location的参数放入@Query里面，因为是使用的GET请求，所以里面的内容会拼接到地址后面，并且自动会加上 & 符号
     * Call是retrofit2框架里面的，这个框架是对OKHttp的进一步封装，会让你的使用更加简洁明了，里面放入之前通过接口返回
     * 的JSON字符串生成返回数据实体Bean,Retrofit支持Gson解析实体类,所以，后面的返回值就不用做解析了。
     * getTodayWeather是这个接口的方法名。这样说应该很清楚了吧
     *
     * @param location 区/县
     * @return
     */
    @GET("/v7/weather/now?key=fa242ae2dd774d2f859210f2e1724bb4")
    Call<TodayResponse> getTodayWeather(@Query("location") String location);

    /**
     * 未来3-7天天气预报
     * https://devapi.qweather.com/v7/weather/7d?key=fa242ae2dd774d2f859210f2e1724bb4&location=101010100
     *
     * @param location 区/县
     * @return
     */
    @GET("/v7/weather/7d?key=fa242ae2dd774d2f859210f2e1724bb4")
    Call<WeatherForecastResponse> getWeatherForecast(@Query("location") String location);

    /**
     * 获取生活指数
     * https://devapi.qweather.com/v7/indices/1d?key=fa242ae2dd774d2f859210f2e1724bb4&type=0&location=101010100
     *
     * @param location
     * @return
     */
    @GET("/v7/indices/1d?key=fa242ae2dd774d2f859210f2e1724bb4&type=0")
    Call<LifestyleResponse> getLifestyle(@Query("location") String location);

    /**
     * 必应每日一图
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    Call<BiYingImgResponse> biying();

    /**
     * 逐小时预报
     */
    @GET("/v7/weather/24h?key=fa242ae2dd774d2f859210f2e1724bb4")
    Call<HourlyResponse> getHourly(@Query("location") String location);

    /**
     * 空气质量数据
     */
    @GET("/v7/air/now?key=fa242ae2dd774d2f859210f2e1724bb4")
    Call<AirNowCityResponse> getAirNowCity(@Query("location") String location);

    /**
     * 搜索城市
     */
    @GET("/v2/city/lookup?key=fa242ae2dd774d2f859210f2e1724bb4&range=cn")
    Call<SearchCityResponse> searchCity(@Query("location") String location);


    /**
     * ----- v7 -----
     * 当天天气查询
     */
    @GET("/v7/weather/now?key=fa242ae2dd774d2f859210f2e1724bb4")
    Call<NowResponse> nowWeather(@Query("location") String location);

    /**
     * 天气预报  因为是开发者所以最多可以获得15天的数据，但是如果你是普通用户，那么最多只能获得三天的数据
     * 分为 3天、7天 情况，这是时候就需要动态的改变请求的url
     *
     * @param type     天数类型  传入3d / 7d 通过Path拼接到请求的url里面
     * @param location 城市名
     * @return 返回天气预报数据
     */
    @GET("/v7/weather/{type}?key=fa242ae2dd774d2f859210f2e1724bb4")
    Call<DailyResponse> dailyWeather(@Path("type") String type, @Query("location") String location);

    /**
     * 逐小时预报（未来24小时）之前是逐三小时预报
     *
     * @param location 城市名
     * @return 返回逐小时数据
     */
    @GET("/v7/weather/24h?key=fa242ae2dd774d2f859210f2e1724bb4")
    Call<HourlyResponse> hourlyWeather(@Query("location") String location);

    /**
     * 当天空气质量
     *
     * @param location 城市名
     * @return 返回当天空气质量数据
     */
    @GET("/v7/air/now?key=fa242ae2dd774d2f859210f2e1724bb4")
    Call<AirNowResponse> airNowWeather(@Query("location") String location);

    /**
     * 生活指数
     *
     * @param type     可以控制定向获取那几项数据 全部数据 0, 运动指数	1 ，洗车指数	2 ，穿衣指数	3 ，
     *                 钓鱼指数	4 ，紫外线指数  5 ，旅游指数  6，花粉过敏指数	7，舒适度指数	8，
     *                 感冒指数	9 ，空气污染扩散条件指数	10 ，空调开启指数	 11 ，太阳镜指数	12 ，
     *                 化妆指数  13 ，晾晒指数  14 ，交通指数  15 ，防晒指数	16
     * @param location 城市名
     * @return 返回当前生活指数数据
     * @return
     */
    @GET("/v7/indices/1d?key=fa242ae2dd774d2f859210f2e1724bb4")
    Call<LifestyleResponse> lifestyle(@Query("type") String type, @Query("location") String location);


    /**
     * 搜索城市  V7版本  模糊搜索，国内范围 返回10条数据
     *
     * @param location 城市名
     * @param mode     exact 精准搜索  fuzzy 模糊搜索
     * @return
     */
    @GET("/v2/city/lookup?key=fa242ae2dd774d2f859210f2e1724bb4&range=cn")
    Call<NewSearchCityResponse> newSearchCity(@Query("location") String location, @Query("mode") String mode);

}
// https://geoapi.qweather.com/v2/city/lookup?key=fa242ae2dd774d2f859210f2e1724bb4&range=cn&location=南康