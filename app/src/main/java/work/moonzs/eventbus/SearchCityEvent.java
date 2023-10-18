package work.moonzs.eventbus;

/**
 * 搜索城市消息事件
 */
public class SearchCityEvent {
    public final String mLocation;
    public final String mCity;

    public SearchCityEvent(String mLocation, String mCity) {
        this.mLocation = mLocation;
        this.mCity = mCity;
    }
}
