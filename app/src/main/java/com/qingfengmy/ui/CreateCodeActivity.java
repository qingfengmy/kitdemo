package com.qingfengmy.ui;

/**
 * author zhouxin@easier.cn
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.qingfengmy.R;
import com.r0adkll.slidr.Slidr;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 用于创建和扫描二维码
 */
public class CreateCodeActivity extends BaseActivity {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    /**
     * 用于跳转到开始扫描用来回传值的resultCode
     */
    private static final int TO_SCAN = 99;
    /**
     * 将要生成二维码的内容
     */
    private EditText codeEdit;

    /**
     * 生成二维码代码
     */
    private Button codeBtn;
    /**
     * 用于展示生成二维码的imageView
     */
    private ImageView codeImg;
    /**
     * 开始扫描按钮
     */
    private Button scanBtn;

    /**
     * 生成一维码按钮
     */
    private Button oneCodeBtn;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    /**
     * 界面的初始化事件
     *
     * @param savedInstanceState Bundle对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcode);
        ButterKnife.inject(this);
        Slidr.attach(this);
        // 先设置title，再设置action，否则无效
        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initView();
        setListener();

    }

    /**
     * 用于初始化界面展示的view
     */
    private void initView() {
        codeEdit = (EditText) findViewById(R.id.code_edittext);
        codeEdit.setText("http://www.baidu.com");
        codeBtn = (Button) findViewById(R.id.code_btn);
        codeImg = (ImageView) findViewById(R.id.code_img);
        scanBtn = (Button) findViewById(R.id.scan_btn);
        oneCodeBtn = (Button) findViewById(R.id.btn_code);
    }

    /**
     * 设置生成二维码和扫描二维码的事件
     */
    private void setListener() {
        codeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = codeEdit.getText().toString().trim();
                Bitmap bmp = null;
                try {
                    if (str != null && !"".equals(str)) {
                        bmp = CreateTwoDCode(str);
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                if (bmp != null) {
                    codeImg.setImageBitmap(bmp);
                }

            }
        });

        scanBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CreateCodeActivity.this, MipcaActivityCapture.class);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });

        oneCodeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = codeEdit.getText().toString().trim();
                int size = str.length();
                for (int i = 0; i < size; i++) {
                    int c = str.charAt(i);
                    if ((19968 <= c && c < 40623)) {
                        Toast.makeText(CreateCodeActivity.this,
                                "生成条形码的时刻不能是中文", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                Bitmap bmp = null;
                try {
                    if (str != null && !"".equals(str)) {
                        bmp = CreateOneDCode(str);
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                if (bmp != null) {
                    codeImg.setImageBitmap(bmp);
                }
            }
        });
    }

    /**
     * 将指定的内容生成成二维码
     *
     * @param content 将要生成二维码的内容
     * @return 返回生成好的二维码事件
     * @throws WriterException WriterException异常
     */
    public Bitmap CreateTwoDCode(String content) throws WriterException {
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
     *
     * @param content 将要生成一维码的内容
     * @return 返回生成好的一维码bitmap
     * @throws WriterException WriterException异常
     */
    public Bitmap CreateOneDCode(String content) throws WriterException {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.CODE_128, 500, 200);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 界面回传的事件
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TO_SCAN:
                if (resultCode == RESULT_OK) {
                    String scanStr = data.getStringExtra("RESULT");
                    codeEdit.setText(scanStr);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(CreateCodeActivity.this, "扫描取消",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateCodeActivity.this, "扫描异常",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    codeEdit.setText(bundle.getString("result"));
                    //显示
                    codeImg.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
            default:
                break;
        }
    }

}