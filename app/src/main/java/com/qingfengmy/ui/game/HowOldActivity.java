package com.qingfengmy.ui.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.BaseActivity;
import com.qingfengmy.ui.game.utils.FaceppDetect;
import com.qingfengmy.ui.game.utils.FaceppParseException;
import com.qingfengmy.ui.utils.Constants;
import com.qingfengmy.ui.utils.ImageTools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/5/13.
 */
public class HowOldActivity extends BaseActivity {
    // 弹出对话框的按钮值
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;

    private static final int SCALE = 2;// 照片缩小比例
    private static final int MSG_SUCCESS = 6;
    private static final int MSG_ERROR = 7;

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

    @InjectView(R.id.tv_tip)
    TextView tip;

    @InjectView(R.id.iv_photo)
    ImageView photo;

    @InjectView(R.id.loading)
    FrameLayout loading;

    @InjectView(R.id.tv_age)
    TextView ageText;

    Bitmap mPhotoImg;
    private Paint mPaint;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_SUCCESS:
                    loading.setVisibility(View.GONE);
                    JSONObject rs = (JSONObject) msg.obj;
                    prepareRsBitmap(rs);
                    photo.setImageBitmap(mPhotoImg);
                    break;
                case MSG_ERROR:
                    loading.setVisibility(View.GONE);
                    FaceppParseException exception = (FaceppParseException) msg.obj;
                    if (!TextUtils.isEmpty(exception.getErrorMessage())) {
                        tip.setText(exception.getErrorMessage());
                    } else if (!TextUtils.isEmpty(exception.getMessage())) {
                        tip.setText(exception.getErrorMessage());
                    }
            }
        }
    };

    private void prepareRsBitmap(JSONObject rs) {

        Bitmap bitmap = Bitmap.createBitmap(mPhotoImg.getWidth(), mPhotoImg.getHeight(), mPhotoImg.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mPhotoImg, 0, 0, null);

        try {
            JSONArray faces = rs.getJSONArray("face");
            int faceCount = faces.length();
            tip.setText("find " + faceCount);

            for (int i = 0; i < faceCount; i++) {
                JSONObject face = faces.getJSONObject(i);
                JSONObject posObj = face.getJSONObject("position");
                // 取中心点，其值表示相对于图片的百分比
                float x = (float) posObj.getJSONObject("center").getDouble("x");
                float y = (float) posObj.getJSONObject("center").getDouble("y");
                // 取长宽
                float w = (float) posObj.getDouble("width");
                float h = (float) posObj.getDouble("height");
                // 百分比转换
                x = x / 100 * bitmap.getWidth();
                y = y / 100 * bitmap.getHeight();

                w = w / 100 * bitmap.getWidth();
                h = h / 100 * bitmap.getHeight();

                // 画线
                canvas.drawLine(x - w / 2, y - h / 2, x - w / 2, y + h / 2, mPaint);
                canvas.drawLine(x - w / 2, y - h / 2, x + w / 2, y - h / 2, mPaint);
                canvas.drawLine(x + w / 2, y - h / 2, x + w / 2, y + h / 2, mPaint);
                canvas.drawLine(x - w / 2, y + h / 2, x + w / 2, y + h / 2, mPaint);

                // get age and gender
                int age = face.getJSONObject("attribute").getJSONObject("age").getInt("value");
                String gender = face.getJSONObject("attribute").getJSONObject("gender").getString("value");

                Bitmap ageBitmap = buildAgeBitmap(age, "Male".equals(gender));

                int ageWidth = ageBitmap.getWidth();
                int ageHeight = ageBitmap.getHeight();

                if (w < ageBitmap.getWidth() && h < ageBitmap.getHeight()) {
                    float ratio = Math.max(w * 1.0f / ageBitmap.getWidth(), h * 1.0f / ageBitmap.getHeight());
                    if (w < ageWidth / 3) {
                        ratio = ratio * 1.5f;
                    }else if (w > ageWidth / 2){
                        ratio = ratio /3;
                    }
                    ageBitmap = Bitmap.createScaledBitmap(ageBitmap, (int) (ageWidth * ratio), (int) (ageHeight * ratio), false);
                }
                canvas.drawBitmap(ageBitmap, x - ageBitmap.getWidth() / 2, y - h / 2 - ageBitmap.getHeight(), null);
            }

            mPhotoImg = bitmap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap buildAgeBitmap(int age, boolean isMale) {

        ageText.setText(age + "");
        if (isMale) {
            ageText.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male), null, null, null);
        } else {
            ageText.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female), null, null, null);
        }

        ageText.setDrawingCacheEnabled(true);
        Bitmap ageBitmap = Bitmap.createBitmap(ageText.getDrawingCache());
        ageText.destroyDrawingCache();
        return ageBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_old);
        ButterKnife.inject(this);

        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setStrokeWidth(2);
    }


    @OnClick(R.id.bt_detect)
    public void detect() {
        loading.setVisibility(View.VISIBLE);
        if (mPhotoImg == null) {
            mPhotoImg = BitmapFactory.decodeResource(getResources(), R.drawable.t4);
        }
        FaceppDetect.detect(mPhotoImg, new FaceppDetect.CallBack() {
            @Override
            public void success(JSONObject jsonObject) {
                Message message = Message.obtain();
                message.what = MSG_SUCCESS;
                message.obj = jsonObject;
                mHandler.sendMessage(message);
            }

            @Override
            public void error(FaceppParseException exception) {
                Message message = Message.obtain();
                message.what = MSG_ERROR;
                message.obj = exception;
                mHandler.sendMessage(message);
            }
        });
    }

    @OnClick(R.id.bt_getImage)
    public void getImage() {
        showPicturePicker(this, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GET_PIC_FROM_PHOTO_REQUEST) {
            createCropImg(data);
        } else if (requestCode == Constants.GET_PIC_FROM_CAMERA_REQUEST) {
            cropImageUri(imageUri, 200, 200);
        } else if (requestCode == Constants.CROP_PIC_REQUEST) {
            createCropImg(data);
        }
    }

    public void showPicturePicker(Context context, boolean isCrop) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "相册"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case TAKE_PICTURE:
                                Intent intent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent,
                                        Constants.GET_PIC_FROM_CAMERA_REQUEST);
                                break;
                            case CHOOSE_PICTURE:
                                intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                                intent.setType("image/*");
                                intent.putExtra("crop", "true");
                                intent.putExtra("aspectX", 1);
                                intent.putExtra("aspectY", 1);
                                intent.putExtra("outputX", 800);
                                intent.putExtra("outputY", 800);
                                intent.putExtra("scale", true);
                                intent.putExtra("return-data", false);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                intent.putExtra("outputFormat",
                                        Bitmap.CompressFormat.JPEG.toString());
                                intent.putExtra("noFaceDetection", false);
                                startActivityForResult(intent,
                                        Constants.GET_PIC_FROM_PHOTO_REQUEST);
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void cropImageUri(Uri uri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, Constants.CROP_PIC_REQUEST);
    }

    private void createCropImg(Intent data) {
        Bitmap tempPhoto = decodeUriAsBitmap(imageUri);
        if (tempPhoto != null) {
            mPhotoImg = resizePhoto(tempPhoto);
//            mPhotoImg = tempPhoto;

            photo.setImageBitmap(mPhotoImg);
            tip.setText("detect ==>");
        }else{
            tip.setText("error");
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    private Bitmap resizePhoto(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while( image.getByteCount() / 1024>1024*3) {//判断如果图片大于3M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            // 压缩
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            InputStream temp = new ByteArrayInputStream(baos.toByteArray());

            BitmapFactory.Options options = new BitmapFactory.Options();
            // 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
            options. inJustDecodeBounds = true;
            // 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
            // 这个参数表示 新生成的图片为原始图片的几分之一。
            options. inSampleSize = 2;
            // 这里之前设置为了true，所以要改为false，否则就创建不出图片
            options. inJustDecodeBounds = false;

            image = BitmapFactory. decodeStream(temp, null, options);
        }
        // 返回
        return image;
    }

}
