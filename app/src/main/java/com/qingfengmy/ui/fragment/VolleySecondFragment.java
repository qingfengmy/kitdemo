package com.qingfengmy.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.qingfengmy.R;
import com.qingfengmy.ui.VolleyActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/1/15.
 */
public class VolleySecondFragment extends Fragment {

    @InjectView(R.id.result_1)
    ImageView imageView_1;
    @InjectView(R.id.result_2)
    ImageView imageView_2;
    @InjectView(R.id.network_image_view)
    NetworkImageView networkImageView ;

    RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volley_second, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /**
         * ImageRequest的构造函数接收六个参数，第一个参数就是图片的URL地址，
         * 第二个参数是图片请求成功的回调，这里我们把返回的Bitmap参数设置到ImageView中。
         * 第三第四个参数分别用于指定允许图片最大的宽度和高度，
         * 如果指定的网络图片的宽度或高度大于这里的最大值，则会对图片进行压缩，
         * 指定成0的话就表示不管图片有多大，都不会进行压缩。
         * 第五个参数用于指定图片的颜色属性，Bitmap.Config下的几个常量都可以在这里使用，
         * 其中ARGB_8888可以展示最好的颜色属性，每个图片像素占据4个字节的大小，而RGB_565则表示每个图片像素占据2个字节大小。
         * 第六个参数是图片请求失败的回调，这里我们当请求失败时在ImageView中显示一张默认图片。
         */
        ImageRequest imageRequest = new ImageRequest(
                "http://ww2.sinaimg.cn/bmiddle/9e5389bbjw1eoa9o1aztyj20ci0eh3yv.jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView_1.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView_1.setImageResource(R.drawable.ic_launcher);
            }
        });

        mQueue = getRequestQueue();
        mQueue.add(imageRequest);

        /**
         * ImageLoader也可以用于加载网络上的图片，并且它的内部也是使用ImageRequest来实现的，
         * 不过ImageLoader明显要比ImageRequest更加高效，因为它不仅可以帮我们对图片进行缓存，
         * 还可以过滤掉重复的链接，避免重复发送请求。
         *
         * ImageLoader的构造函数接收两个参数，
         * 第一个参数就是RequestQueue对象，
         * 第二个参数是一个ImageCache对象.
         */
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());


        /**
         * 我们通过调用ImageLoader的getImageListener()方法能够获取到一个ImageListener对象，
         * getImageListener()方法接收三个参数，
         * 第一个参数指定用于显示图片的ImageView控件，
         * 第二个参数指定加载图片的过程中显示的图片，
         * 第三个参数指定加载图片失败的情况下显示的图片。
         */
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView_2,
                R.drawable.default_image, R.drawable.default_image);

        /**
         * get()方法接收两个参数，第一个参数就是图片的URL地址，
         * 第二个参数则是刚刚获取到的ImageListener对象。
         * 当然，如果你想对图片的大小进行限制，也可以使用get()方法的重载，指定图片允许的最大宽度和高度
         * 如下：
         * imageLoader.get("http://xxx.jpeg", listener, 200, 200);
         */
        imageLoader.get("http://ww2.sinaimg.cn/bmiddle/9e5389bbjw1eoa9o1rmrqj20go0fiac0.jpg", listener);


        /**
         * NetworkImageView是一个自定义控制，它是继承自ImageView的，
         * 具备ImageView控件的所有功能，并且在原生的基础之上加入了加载网络图片的功能。
         * NetworkImageView控件的用法要比前两种方式更加简单
         */
        networkImageView.setDefaultImageResId(R.drawable.default_image);
        networkImageView.setErrorImageResId(R.drawable.default_image);
        networkImageView.setImageUrl("http://ww1.sinaimg.cn/bmiddle/9e5389bbjw1eoa9o2eeq7j20fa0n3q4o.jpg",
                imageLoader);


    }


    public RequestQueue getRequestQueue() {
        if (mQueue == null)
            mQueue = ((VolleyActivity) getActivity()).getRequestQueue();
        return mQueue;
    }

    /**
     * 如果写一个ImageCache也非常简单，但是如果想要写一个性能非常好的ImageCache，最好就要借助Android提供的LruCache功能了
     */
    public class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            // 我们将缓存图片的大小设置为10M
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e("aaa", "2-onAttach");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("aaa","2-onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("aaa","2-onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("aaa","2-onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("aaa","2-onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("aaa","2-onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("aaa","2-onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("aaa","2-onDestroy");
    }
}
