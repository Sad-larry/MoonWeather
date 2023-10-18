package work.moonzs.mvplibrary.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 服务构建器 API服务设置在里面
 */
public class ServiceGenerator {

    // https://devapi.qweather.com/v7/weather/now?location=101010100&key=fa242ae2dd774d2f859210f2e1724bb4
    // 将上方的API接口地址进行拆分得到不变的一部分,实际开发中可以将这一部分作为服务器的ip访问地址
    public static String BASE_URL = null;

    private static String urlType(int type) {
        switch (type) {
            // 和风天气
            case 0:
            case 3:
                BASE_URL = "https://devapi.qweather.com";
                break;
            // 必应每日一图
            case 1:
                BASE_URL = "https://cn.bing.com";
                break;
            // 搜索城市
            case 2:
            case 4:
                BASE_URL = "https://geoapi.qweather.com";
                break;
        }
        return BASE_URL;
    }

    // 创建服务，参数就是API服务
    public static <T> T createService(Class<T> serviceClass, int type) {
        // 创建okHttpClient构建器对象
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        // 设置请求超时时间，单位ms
        okHttpClientBuilder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        // 消息拦截器，因为有时候接口不同在排错的时候，需要先从接口中做分析，利用了消息拦截器可以清楚的看到接口返回的所有内容
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // setLevel用来设置日志打印的级别，NONE、BASIC、HEADER、BODY
        // BASIC：请求/响应行
        // HEADER：请求/响应行+头
        // BODY：请求/响应行+头+体
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 为OkHttp添加消息拦截器
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
        // 在Retrofit中设置httpclient
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlType(type))
                // 用Gson把服务器端返回的json数据解析成实体
                .addConverterFactory(GsonConverterFactory.create())
                // 放入OKHttp，之前说过retrofit是对OkHttp的进一步封装
                .client(okHttpClientBuilder.build())
                .build();
        // 返回这个创建好的API服务
        return retrofit.create(serviceClass);
    }
}
