package work.moonzs.mvplibrary.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.lang.ref.SoftReference;

import work.moonzs.mvplibrary.R;

/**
 * 加载框
 */
public class LoadingView extends AppCompatImageView {
    // 图片旋转点X
    private int mCenterRotateX;
    // 图片旋转点Y
    private int mCenterRotateY;
    private LoadingRunnable mRunnable;

    public LoadingView(@NonNull Context context) {
        this(context, null);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
        setImageBitmap(bitmap);
        mCenterRotateX = bitmap.getWidth() / 2;
        mCenterRotateY = bitmap.getHeight() / 2;
    }

    /**
     * onDraw()之前调用
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mRunnable == null) {
            mRunnable = new LoadingRunnable(this);
        }
        if (!mRunnable.isLoading) {
            mRunnable.start();
        }
    }

    /**
     * view销毁前调用
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRunnable != null) {
            mRunnable.stop();
        }
        mRunnable = null;
    }

    class LoadingRunnable implements Runnable {
        private boolean isLoading;
        private Matrix mMatrix;
        private SoftReference<LoadingView> mLoadingViewSoftReference;
        private float mDegrees = 0f;

        public LoadingRunnable(LoadingView loadingView) {
            mLoadingViewSoftReference = new SoftReference<>(loadingView);
            mMatrix = new Matrix();
        }

        @Override
        public void run() {
            if (mLoadingViewSoftReference.get().mRunnable != null && mMatrix != null) {
                mDegrees += 30f;
                mMatrix.setRotate(mDegrees, mCenterRotateX, mCenterRotateY);
                mLoadingViewSoftReference.get().setImageMatrix(mMatrix);
                if (mDegrees == 360) {
                    mDegrees = 0f;
                }
                if (isLoading) {
                    mLoadingViewSoftReference.get().postDelayed(mLoadingViewSoftReference.get().mRunnable, 100);
                }
            }
        }

        public void stop() {
            isLoading = false;
        }

        public void start() {
            isLoading = true;
            if (mLoadingViewSoftReference.get().mRunnable != null && mMatrix != null) {
                mLoadingViewSoftReference.get().postDelayed(mLoadingViewSoftReference.get().mRunnable, 100);
            }
        }
    }
}
