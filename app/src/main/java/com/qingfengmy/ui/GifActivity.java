package com.qingfengmy.ui;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.felipecsl.gifimageview.library.GifImageView;
import com.qingfengmy.R;

import java.io.ByteArrayOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/2/6.
 */
public class GifActivity extends BaseActivity {

    @InjectView(R.id.gifImageView)
    GifImageView gifView;
    byte[] bitmapData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        ButterKnife.inject(this);

        Bitmap bitmap = getIntent().getParcelableExtra("bitmap");

        bitmapData = bitmap2Bytes(bitmap);
        gifView.setBytes(bitmapData);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        gifView.setBackgroundColor(Color.WHITE);
//        parent.addView(gifView,lp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gifView.startAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gifView.stopAnimation();
    }

    public byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
