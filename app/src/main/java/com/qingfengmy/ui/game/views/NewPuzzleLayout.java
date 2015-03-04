package com.qingfengmy.ui.game.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qingfengmy.R;
import com.qingfengmy.ui.game.bean.ImagePiece;
import com.qingfengmy.ui.game.utils.ImageSplitterUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/3/3.
 */
public class NewPuzzleLayout extends RelativeLayout implements View.OnClickListener {

    private Bitmap bitmap;
    private int mColumn;
    private int mPadding;
    private int mMargin;
    private ImageView[] mGamePintuItems;
    private List<ImagePiece> imagePieces;
    private Context context;
    private int mWidth;
    private boolean once;

    public NewPuzzleLayout(Context context) {
        this(context, null);
    }

    public NewPuzzleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewPuzzleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

        mColumn = 2;
        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(),
                getPaddingBottom());
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 取宽和高中的小值
        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());

        if (!once) {
            // 进行切图，以及排序
            initBitmap();
            // 设置ImageView(Item)的宽高等属性
            initItem();

            once = true;
        }
        setMeasuredDimension(mWidth, mWidth);
    }

    /**
     * 进行切图，以及排序
     */
    private void initBitmap() {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image6);
        }

        imagePieces = ImageSplitterUtil.splitImage(bitmap, mColumn);

        // 乱序
        Collections.sort(imagePieces, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece lhs, ImagePiece rhs) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
    }

    /**
     * 设置ImageView(Item)的宽高等属性
     */
    private void initItem() {

        // imageView的宽度
        int width = (mWidth - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn;
        mGamePintuItems = new ImageView[mColumn * mColumn];
        for (int i = 0; i < imagePieces.size(); i++) {
            ImageView item = new ImageView(context);
            // id避免设为0，所以加1
            item.setId(i + 1);
            // tag里存放的是顺序数据
            item.setTag(imagePieces.get(i).getIndex());
            item.setImageBitmap(imagePieces.get(i).getBitmap());
            item.setOnClickListener(this);
            mGamePintuItems[i] = item;

            RelativeLayout.LayoutParams lp = new LayoutParams(width, width);
            // 除了第一列，其他有marging_right,还要设置leftOf
            if (i % mColumn != 0) {
                lp.leftMargin = mMargin;
                lp.addRule(RelativeLayout.RIGHT_OF, mGamePintuItems[i - 1].getId());
            }
            // 除了第一行，都有margin_top，且都有below
            if (i >= mColumn) {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW, mGamePintuItems[i - mColumn].getId());
            }
            addView(item, lp);
        }

    }


    /**
     * 获取多个参数的最小值
     */
    private int min(int... params) {
        int min = params[0];

        for (int param : params) {
            if (param < min)
                min = param;
        }
        return min;
    }

    // 用作交互时使用
    private ImageView mFirst, mSecond;

    @Override
    public void onClick(View v) {
        // 正在执行动画则返回
        if (isAniming)
            return;

        // 两次按下的是同一个
        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }

        // 第一次按下
        if (mFirst == null) {
            mFirst = (ImageView) v;
            // 按下效果
            mFirst.setColorFilter(Color.parseColor("#55ff0000"));
            return;
        } else {
            mSecond = (ImageView) v;
            mFirst.setColorFilter(null);
            // 交换
            exchangeView();
        }

    }

    private boolean isAniming;

    private void exchangeView() {
        // 构造我们的动画层
        setUpAnimLayout();

        // 实例化两个imageView，作为firstView和secondView在动画层上进行动画
        ImageView mFirstView = new ImageView(context);
        mFirstView.setImageDrawable(mFirst.getDrawable());
        RelativeLayout.LayoutParams lp1 = new LayoutParams(mFirst.getWidth(), mFirst.getHeight());
        lp1.topMargin = mFirst.getTop() - mPadding;
        lp1.leftMargin = mFirst.getLeft() - mPadding;
        mAnimLayout.addView(mFirstView, lp1);

        ImageView mSecondView = new ImageView(context);
        mSecondView.setImageDrawable(mSecond.getDrawable());
        RelativeLayout.LayoutParams lp2 = new LayoutParams(mSecond.getWidth(), mSecond.getHeight());
        lp2.topMargin = mSecond.getTop() - mPadding;
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        mAnimLayout.addView(mSecondView, lp2);

        // 设置动画
        TranslateAnimation anim = new TranslateAnimation(0, mSecond.getLeft()
                - mFirst.getLeft(), 0, mSecond.getTop() - mFirst.getTop());
        anim.setDuration(300);
        anim.setFillAfter(true);
        mFirstView.startAnimation(anim);

        TranslateAnimation animSecond = new TranslateAnimation(0,
                -mSecond.getLeft() + mFirst.getLeft(), 0, -mSecond.getTop()
                + mFirst.getTop());
        animSecond.setDuration(300);
        animSecond.setFillAfter(true);
        mSecondView.startAnimation(animSecond);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mFirst.setVisibility(View.INVISIBLE);
                mSecond.setVisibility(View.INVISIBLE);

                isAniming = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Object firstTag = mFirst.getTag();
                Object secondTag = mSecond.getTag();
                Drawable firstDrawable = mFirst.getDrawable();
                Drawable secondDrawable = mSecond.getDrawable();
                mFirst.setImageDrawable(secondDrawable);
                mFirst.setTag(secondTag);

                mSecond.setImageDrawable(firstDrawable);
                mSecond.setTag(firstTag);


                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);
                // 移出动画层
                mAnimLayout.removeAllViews();

                mFirst = null;
                mSecond = null;

                // 判断用户游戏是否成功
                checkSuccess();
                isAniming = false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    /**
     * 判断用户游戏是否成功
     */
    private void checkSuccess() {
        boolean isSuccess = true;

        for (int i = 0; i < mGamePintuItems.length; i++) {
            ImageView imageView = mGamePintuItems[i];
            int index = (int) imageView.getTag();
            if (index != i) {
                isSuccess = false;
            }
        }

        if (isSuccess) {

            Toast.makeText(getContext(), "Success ， level up !!!",
                    Toast.LENGTH_LONG).show();
            if (mListener != null)
                mListener.nextLevel(mColumn-1);
        }

    }

    private RelativeLayout mAnimLayout;

    /**
     * 构造我们的动画层
     */
    private void setUpAnimLayout() {
        if (mAnimLayout == null) {
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public GamePintuListener mListener;

    /**
     * 设置接口回调
     *
     * @param mListener
     */
    public void setOnGamePintuListener(GamePintuListener mListener) {
        this.mListener = mListener;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        init();
        initBitmap();
        initItem();
    }

    public interface GamePintuListener {
        void nextLevel(int nextLevel);
    }

    public void nextLevel()
    {
        this.removeAllViews();
        mAnimLayout = null;
        mColumn++;
        initBitmap();
        initItem();
    }



}
