package work.moonzs.contract;

import retrofit2.Call;
import retrofit2.Response;
import work.moonzs.api.ApiService;
import work.moonzs.bean.NewSearchCityResponse;
import work.moonzs.mvplibrary.base.BasePresenter;
import work.moonzs.mvplibrary.base.BaseView;
import work.moonzs.mvplibrary.net.NetCallBack;
import work.moonzs.mvplibrary.net.ServiceGenerator;

public class SearchCityContract {
    public static class SearchCityPresenter extends BasePresenter<ISearchCityView> {
        /**
         * 搜索城市  V7版本中  模糊搜索  返回10条相关数据
         * @param location 城市名
         */
        public void newSearchCity(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 2);
            service.newSearchCity(location, "fuzzy").enqueue(new NetCallBack<NewSearchCityResponse>() {
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
    }

    public interface ISearchCityView extends BaseView {
        // 查询城市返回数据
        void getNewSearchCityResult(Response<NewSearchCityResponse> response);

        // 错误返回
        void getDataFailed();
    }
}
