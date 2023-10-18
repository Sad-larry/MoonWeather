package work.moonzs.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 相机。相册工具类
 */
public class CameraUtils {
    public static Intent getTakePhotoIntent(Context context, File outputImagePath) {
        // 获取系统版本
        int currentApiVersion = Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以使用
        if (hasSdCard()) {
            if (currentApiVersion < 24) {
                // 从文件中创建url
                Uri uri = Uri.fromFile(outputImagePath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                // 兼容Android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, outputImagePath.getAbsolutePath());
                Uri uri = context.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }
        return intent;
    }

    /**
     * 判断sdcard是否被挂载
     *
     * @return
     */
    public static boolean hasSdCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static Intent getSelectPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    /**
     * 4.4 以上系统处理图片的方法
     *
     * @param data
     * @param context
     * @return
     */
    public static String getImageOnKitKatPath(Intent data, Context context) {
        String imagePath = null;
        Uri uri = data.getData();

        Log.e("uri=intent.getData : ", "" + uri);
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 数据表里指定的行
            String docId = DocumentsContract.getDocumentId(uri);
            Log.d("getDocumentId(uri) : ", "" + docId);
            Log.d("uri.getAuthority() : ", "" + uri.getAuthority());
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;

                imagePath = getImagePath(uri, selection, context);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                imagePath = getImagePath(contentUri, null, context);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null, context);
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null, context);
        }
        return imagePath;
    }

    /**
     * 通过uri和selection来获取真实的图片路径,从相册获取图片时要用
     */
    @SuppressLint("Range")
    private static String getImagePath(Uri uri, String selection, Context context) {
        String path = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // 从系统表中查询指定Uri对应的照片
        Cursor cursor = context.getContentResolver().query(uri,
                filePathColumn, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 改变拍完照后图片方向不正的问题
     */
    public static void ImgUpdateDirection(String filepath, Bitmap orc_bitmap, ImageView iv) {
        // 图片旋转的角度
        int degree = 0;
        // 根据图片的URI获取图片的绝对路径
        Log.i("tag", ">>>>>>>>>>>>>开始");
        Log.i("tag", "》》》》》》》》》》》》》》》" + filepath);
        // 根据图片的filepath获取到一个ExifInterface的对象
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
            Log.i("tag", "exif》》》》》》》》》》》》》》》" + exif);
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        degree = 0;
                        break;
                }
            }
            // 如果图片不为0
            if (degree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(degree);
                orc_bitmap = Bitmap.createBitmap(orc_bitmap, 0, 0, orc_bitmap.getWidth(), orc_bitmap.getHeight(), m, true);
            }
            if (orc_bitmap != null) {
                iv.setImageBitmap(orc_bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            exif = null;
        }
    }

    /**
     * 4.4以下系统处理图片的方法
     */
    public static String getImageBeforeKitKatPath(Intent data, Context context) {
        Uri uri = data.getData();
        return getImagePath(uri, null, context);
    }

    /**
     * 比例压缩
     */
    public static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 判断如果图片大于5M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 5120) {
            // 重置baos时清空baos
            baos.reset();
            // 这里压缩50%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;
        float ww = 480f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        // be = 1表示不缩放
        int be = 1;
        if (w > h && w > ww) {
            // 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            // 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        // 设置缩放比例
        newOpts.inSampleSize = be;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        // 压缩好比例大小后再进行质量压缩
        return bitmap;
    }
}
