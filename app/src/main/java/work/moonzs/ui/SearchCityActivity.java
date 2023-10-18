package work.moonzs.ui;

import static work.moonzs.mvplibrary.utils.RecyclerViewAnimation.runLayoutAnimation;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import work.moonzs.R;
import work.moonzs.adapter.SearchCityAdapter;
import work.moonzs.bean.NewSearchCityResponse;
import work.moonzs.contract.SearchCityContract;
import work.moonzs.eventbus.SearchCityEvent;
import work.moonzs.mvplibrary.mvp.MvpActivity;
import work.moonzs.mvplibrary.view.flowlayout.FlowLayout;
import work.moonzs.mvplibrary.view.flowlayout.RecordsDao;
import work.moonzs.mvplibrary.view.flowlayout.TagAdapter;
import work.moonzs.mvplibrary.view.flowlayout.TagFlowLayout;
import work.moonzs.utils.CodeToStringUtils;
import work.moonzs.utils.Constant;
import work.moonzs.utils.SPUtils;
import work.moonzs.utils.StatusBarUtil;
import work.moonzs.utils.ToastUtils;

/**
 * 搜索城市
 */
public class SearchCityActivity extends MvpActivity<SearchCityContract.SearchCityPresenter> implements SearchCityContract.ISearchCityView {
    // 输入框
    @BindView(R.id.edit_query)
    AutoCompleteTextView editQuery;
    // 清除输入的内容
    @BindView(R.id.iv_clear_search)
    ImageView ivClearSearch;
    // 头部
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // 搜索结果展示列表
    @BindView(R.id.rv)
    RecyclerView rv;
    // 清理所有历史记录
    @BindView(R.id.clear_all_records)
    ImageView clearAllRecords;
    // 搜索历史布局
    @BindView(R.id.fl_search_records)
    TagFlowLayout flSearchRecords;
    // 超过三行就会出现，展开显示更多
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    // 搜索历史主布局
    @BindView(R.id.ll_history_content)
    LinearLayout llHistoryContent;

    // 数据源
    List<NewSearchCityResponse.LocationDTO> mList = new ArrayList<>();
    // 适配器
    SearchCityAdapter mAdapter;

    private RecordsDao mRecordsDao;
    // 默认展示词条个数
    private final int DEFAULT_RECORD_NUMBER = 10;
    private List<String> recordList = new ArrayList<>();
    private TagAdapter mRecordsAdapter;

    @Override
    public void initData(Bundle saveInstanceState) {
        // 白色状态栏
        StatusBarUtil.setStatusBarColor(context, work.moonzs.mvplibrary.R.color.white);
        // 黑色字体
        StatusBarUtil.StatusBarLightMode(context);
        Back(toolbar);

        // 初始化页面数据
        initView();
        initAutoComplete("history", editQuery);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_city;
    }

    @Override
    protected SearchCityContract.SearchCityPresenter createPresent() {
        return new SearchCityContract.SearchCityPresenter();
    }

    /**
     * 搜索城市返回的结果数据
     *
     * @param response
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {
        dismissLoadingDialog();
        if (response.body() != null && response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            mList.clear();
            mList.addAll(response.body().getLocation());
            mAdapter.notifyDataSetChanged();
            runLayoutAnimation(rv);
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 网络请求异常返回提示
     */
    @Override
    public void getDataFailed() {
        // 关闭弹窗
        dismissLoadingDialog();
        ToastUtils.showShortToast(context, "网络异常");
    }

    /**
     * 返回
     */
    public void Back(androidx.appcompat.widget.Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
                if (!isFastClick()) {
                    context.finish();
                }
            }
        });
    }

    private void initView() {
        // 默认账号
        String username = "007";
        // 初始化数据库
        mRecordsDao = new RecordsDao(this, username);

        initTagFlowLayout();

        // 创建历史标签适配器
        // 为标签设置对应的内容
        mRecordsAdapter = new TagAdapter<String>(recordList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(work.moonzs.mvplibrary.R.layout.tv_history, flSearchRecords, false);
                // 为标签设置对应的内容
                tv.setText(s);
                return tv;
            }
        };

        initEdit();

        flSearchRecords.setAdapter(mRecordsAdapter);
        flSearchRecords.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public void onTagClick(View view, int position, FlowLayout parent) {
                // 清空editText之前的数据
                editQuery.setText("");
                // 将获取到的字符串传到搜索结果界面,点击后搜索对应条目内容
                editQuery.setText(recordList.get(position));
                editQuery.setSelection(editQuery.length());
            }
        });

        // 长按删除某个条目
        flSearchRecords.setOnLongClickListener(new TagFlowLayout.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                showDialog("确定要删除该条历史记录?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 删除某一条记录
                        mRecordsDao.deleteRecord(recordList.get(position));

                        initTagFlowLayout();
                    }
                });
            }
        });

        // view加载完成时回调
        flSearchRecords.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isOverFlow = flSearchRecords.isOverFlow();
                boolean isLimit = flSearchRecords.isLimit();
                if (isLimit && isOverFlow) {
                    ivArrow.setVisibility(View.VISIBLE);
                } else {
                    ivArrow.setVisibility(View.GONE);
                }
            }
        });

        initResultList();
    }

    /**
     * 提示弹窗
     */
    private void showDialog(String dialogTitle, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 历史记录布局
     */
    @SuppressLint("CheckResult")
    private void initTagFlowLayout() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                emitter.onNext(mRecordsDao.getRecordsByNumber(DEFAULT_RECORD_NUMBER));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> s) throws Exception {
                        recordList.clear();
                        recordList = s;
                        if (recordList == null || recordList.size() == 0) {
                            llHistoryContent.setVisibility(View.GONE);
                        } else {
                            llHistoryContent.setVisibility(View.VISIBLE);
                        }
                        if (mRecordsAdapter != null) {
                            mRecordsAdapter.setData(recordList);
                            mRecordsAdapter.notifyDataChanged();
                        }
                    }
                });
    }

    /**
     * 初始化搜索返回的数据列表
     */
    private void initResultList() {
        mAdapter = new SearchCityAdapter(R.layout.item_search_city_list, mList);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(mAdapter);

        mAdapter.addChildClickViewIds(R.id.tv_city_name);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                SPUtils.putString(Constant.DISTRICT, mList.get(position).getName(), context);
                // 发送消息
                EventBus.getDefault().post(new SearchCityEvent(mList.get(position).getName(), mList.get(position).getAdm2()));
                finish();
            }
        });
    }

    /**
     * 初始化输入框
     */
    private void initEdit() {
        // 添加输入监听
        editQuery.addTextChangedListener(textWatcher);
        // 监听软件键盘搜索按钮
        editQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String location = editQuery.getText().toString();
                    if (!TextUtils.isEmpty(location)) {
                        showLoadingDialog();
                        // 添加数据
                        mRecordsDao.addRecords(location);
                        mPresent.newSearchCity(location);
                        // 数据保存
                        saveHistory("history", editQuery);
                    } else {
                        ToastUtils.showShortToast(context, "请输入搜索关键词");
                    }
                }
                return false;
            }
        });
    }

    /**
     * 输入监听
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!"".equals(editable.toString())) {
                // 输入后，显示清除按钮
                ivClearSearch.setVisibility(View.VISIBLE);
            } else {
                // 隐藏按钮
                ivClearSearch.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 点击事件
     */
    @OnClick({R.id.iv_clear_search, R.id.clear_all_records, R.id.iv_arrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clear_search:
                // 清除输入框的内容
                // 清除内容隐藏清除按钮
                ivClearSearch.setVisibility(View.GONE);
                editQuery.setText("");
                break;
            case R.id.clear_all_records:
                // 清除所有记录
                showDialog("确定要删除全部历史记录?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        flSearchRecords.setLimit(true);
                        // 清除所有记录
                        mRecordsDao.deleteUsernameAllRecords();

                        llHistoryContent.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.iv_arrow:
                // 向下展开
                flSearchRecords.setLimit(false);
                mRecordsAdapter.notifyDataChanged();
                break;
        }

    }

    /**
     * 使 AutoCompleteTextView在一开始获得焦点时自动提示
     *
     * @param field                保存在sharedPreference中的字段名
     * @param autoCompleteTextView 要操作的AutoCompleteTextView
     */
    private void initAutoComplete(String field, AutoCompleteTextView autoCompleteTextView) {
        SharedPreferences sp = getSharedPreferences("sp_history", 0);
        // 获取缓存
        String etHistory = sp.getString("history", "南康");
        // 通过,号分割成String数组
        String[] histories = etHistory.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_tv_history, histories);

        // 只保留最近的50条的记录
        if (histories.length > 50) {
            String[] newHistories = new String[50];
            System.arraycopy(histories, 0, newHistories, 0, 50);
            adapter = new ArrayAdapter<>(this, R.layout.item_tv_history, newHistories);
        }
        // AutoCompleteTextView可以直接设置数据适配器，并且在获得焦点的时候弹出，
        // 通常是在用户第一次进入页面的时候，点击输入框输入的时候出现，如果每次都出现
        // 是会应用用户体验的，这里不推荐这么做
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                AutoCompleteTextView v = (AutoCompleteTextView) view;
                if (b) {
                    // 出现历史输入记录
                    v.showDropDown();
                }
            }
        });
    }

    /**
     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
     * 每次输入完之后调用此方法保存输入的值到缓存里
     *
     * @param field                保存在sharedPreference中的字段名
     * @param autoCompleteTextView 要操作的AutoCompleteTextView
     */
    private void saveHistory(String field, AutoCompleteTextView autoCompleteTextView) {
        String text = autoCompleteTextView.getText().toString();
        SharedPreferences sp = getSharedPreferences("sp_history", 0);
        String tvHistory = sp.getString(field, "南康");

        if (!tvHistory.contains(text + ",")) {
            // 如果历史缓存中不存在输入的值则
            StringBuilder sb = new StringBuilder(tvHistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).apply();
        }
    }
}