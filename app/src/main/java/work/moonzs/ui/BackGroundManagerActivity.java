package work.moonzs.ui;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import me.shihao.library.XRadioGroup;
import work.moonzs.R;
import work.moonzs.mvplibrary.base.BaseActivity;
import work.moonzs.mvplibrary.utils.AnimationUtil;
import work.moonzs.mvplibrary.utils.LiWindow;
import work.moonzs.mvplibrary.view.SwitchButton;
import work.moonzs.utils.CameraUtils;
import work.moonzs.utils.Constant;
import work.moonzs.utils.SPUtils;
import work.moonzs.utils.StatusBarUtil;
import work.moonzs.utils.ToastUtils;

/**
 * 壁纸管理 三种模式：本地壁纸列表，必应每日一图，自己上传图片
 */
public class BackGroundManagerActivity extends BaseActivity {
    // 标题栏返回
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // 每日一图开关
    @BindView(R.id.wb_everyday)
    SwitchButton wbEveryday;
    // 图片列表
    @BindView(R.id.wb_img_list)
    SwitchButton wbImgList;
    // 手动定义
    @BindView(R.id.wb_custom_)
    SwitchButton wbCustom;

    // 弹窗
    LiWindow liWindow;
    // 权限请求
    RxPermissions rxPermissions;

    // 动画工具类
    private AnimationUtil animUtil;
    // 背景透明度
    private float bgAlpha = 1f;
    // 判断标识
    private boolean bright = false;
    // 0.5s
    private static final long DURATION = 500;
    // 开始透明度
    private static final float START_ALPHA = 0.7f;
    // 结束透明度
    private static final float END_ALPHA = 1f;

    @Override
    public void initData(Bundle saveInstanceState) {
        // 高亮状态栏
        StatusBarUtil.StatusBarLightMode(this);
        Back(toolbar);
        animUtil = new AnimationUtil();
        initSwitchButton();
    }

    // 初始化三个开关按钮 三个只能开一个
    private void initSwitchButton() {
        // 每日图片
        boolean isEverydayImg = SPUtils.getBoolean(Constant.EVERYDAY_IMG, false, context);
        // 图片列表
        boolean isImgList = SPUtils.getBoolean(Constant.IMG_LIST, false, context);
        // 手动定义
        boolean isCustomImg = SPUtils.getBoolean(Constant.CUSTOM_IMG, false, context);
        if (isEverydayImg) {
            wbEveryday.setChecked(true);
        } else if (isImgList) {
            wbImgList.setChecked(true);
        } else if (isCustomImg) {
            wbCustom.setChecked(true);
        }
        // 每日一图按钮开关监听
        wbEveryday.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SPUtils.putBoolean(Constant.EVERYDAY_IMG, true, context);
                    wbImgList.setChecked(false);
                    wbCustom.setChecked(false);
                } else {
                    SPUtils.putBoolean(Constant.EVERYDAY_IMG, false, context);
                }
            }
        });
        // 图片列表按钮开关监听
        wbImgList.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SPUtils.putBoolean(Constant.IMG_LIST, true, context);
                    wbEveryday.setChecked(false);
                    wbCustom.setChecked(false);
                    // 弹窗窗口显示布局
                    showImgWindow();
                    toggleBright();
                } else {
                    SPUtils.putBoolean(Constant.IMG_LIST, false, context);
                }
            }
        });
        // 手动自定义按钮开关监听
        wbCustom.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SPUtils.putBoolean(Constant.CUSTOM_IMG, true, context);
                    wbEveryday.setChecked(false);
                    wbImgList.setChecked(false);
                    permissionVersion();
                } else {
                    SPUtils.putBoolean(Constant.CUSTOM_IMG, false, context);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_back_ground_manager;
    }

    /**
     * 显示图片弹窗
     */
    private void showImgWindow() {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_img_list, null);
        XRadioGroup xRadioGroup = view.findViewById(R.id.xrg_img);
        // 显示弹窗的时候，取缓存，判断里面有没有选中过图片
        int position = SPUtils.getInt(Constant.IMG_POSITION, -1, context);
        RadioButton rbImg1 = view.findViewById(R.id.rb_img_1);
        RadioButton rbImg2 = view.findViewById(R.id.rb_img_2);
        RadioButton rbImg3 = view.findViewById(R.id.rb_img_3);
        RadioButton rbImg4 = view.findViewById(R.id.rb_img_4);
        RadioButton rbImg5 = view.findViewById(R.id.rb_img_5);
        RadioButton rbImg6 = view.findViewById(R.id.rb_img_6);
        switch (position) {
            case 0:
                rbImg1.setChecked(true);
                break;
            case 1:
                rbImg2.setChecked(true);
                break;
            case 2:
                rbImg3.setChecked(true);
                break;
            case 3:
                rbImg4.setChecked(true);
                break;
            case 4:
                rbImg5.setChecked(true);
                break;
            case 5:
                rbImg6.setChecked(true);
                break;
        }
        /// xRadioGroup 的选中监听
        xRadioGroup.setOnCheckedChangeListener(new XRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(XRadioGroup xRadioGroup, int i) {
                // 得出选中id对应的RadioButton,从而知道选中的是哪一个，并放入缓存，0~5
                switch (xRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_img_1:
                        SPUtils.putInt(Constant.IMG_POSITION, 0, context);
                        liWindow.closePopupWindow();
                        break;
                    case R.id.rb_img_2:
                        SPUtils.putInt(Constant.IMG_POSITION, 1, context);
                        liWindow.closePopupWindow();
                        break;
                    case R.id.rb_img_3:
                        SPUtils.putInt(Constant.IMG_POSITION, 2, context);
                        liWindow.closePopupWindow();
                        break;
                    case R.id.rb_img_4:
                        SPUtils.putInt(Constant.IMG_POSITION, 3, context);
                        liWindow.closePopupWindow();
                        break;
                    case R.id.rb_img_5:
                        SPUtils.putInt(Constant.IMG_POSITION, 4, context);
                        liWindow.closePopupWindow();
                        break;
                    case R.id.rb_img_6:
                        SPUtils.putInt(Constant.IMG_POSITION, 5, context);
                        liWindow.closePopupWindow();
                        break;
                }
                ToastUtils.showShortToast(context, "已更换壁纸");
            }
        });
        // 弹窗关闭监听 弹窗关闭时，如果什么都没有选中，则自然不会有缓存中的取会0~5,所以当为-1时，关闭图片列表的开关
        PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                toggleBright();
                int position = SPUtils.getInt(Constant.IMG_POSITION, -1, context);
                if (position != -1) {
                    wbImgList.setChecked(true);
                } else {
                    wbImgList.setChecked(false);
                }
            }
        };
        // 显示弹窗 ，传入关闭弹窗监听
        liWindow.showBottomPopupWindow(view, onDismissListener);
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

    /**
     * 权限判断
     */
    private void permissionVersion() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 23) {
            // 动态权限申请
            permissionsRequest();
        } else {
            //发现只要权限在AndroidManifest.xml中注册过，均会认为该权限granted  提示一下即可
            if (Build.VERSION.SDK_INT < 19) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
            } else {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            }
            startActivityForResult(intent, Constant.SELECT_PHOTO);
        }
    }

    /**
     * 动态权限申请
     */
    @SuppressLint("CheckResult")
    private void permissionsRequest() {
        rxPermissions = new RxPermissions(context);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        // 得到权限之后打开本地相册
                        Intent selectPhotoIntent = CameraUtils.getSelectPhotoIntent();
                        startActivityForResult(selectPhotoIntent, Constant.SELECT_PHOTO);
                    } else {
                        wbCustom.setChecked(false);
                        ToastUtils.showShortToast(context, "权限未开启");
                    }
                });
    }

    /**
     * Activity返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 打开相册后返回
            case Constant.SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT > 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        imagePath = CameraUtils.getImageOnKitKatPath(data, this);
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, this);
                    }
                    displayImage(imagePath);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 从相册获取完图片(根据图片路径显示图片)
     */
    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            // 将本地上传选中的图片地址放入缓存,当手动定义开关打开时，取出缓存中的图片地址，显示为背景
            SPUtils.putString(Constant.CUSTOM_IMG_PATH, imagePath, context);
            ToastUtils.showShortToast(context, "已更换为你选择的壁纸");
        } else {
            // 关闭手动定义开关
            wbCustom.setChecked(true);
            ToastUtils.showShortToast(context, "图片获取失败");
        }
    }
}