package com.qingfengmy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.qingfengmy.R;
import com.qingfengmy.ui.VolleyActivity;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/1/15.
 */
public class VolleyFirstFragment extends Fragment {

    @InjectView(R.id.result)
    TextView result;
    @InjectView(R.id.json_result)
    TextView json;

    RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volley_first, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /**
         * 这里new出了一个StringRequest对象，StringRequest的构造函数需要传入三个参数，
         * 第一个参数就是目标服务器的URL地址，
         * 第二个参数是服务器响应成功的回调，
         * 第三个参数是服务器响应失败的回调。
         */
        StringRequest stringRequest = new StringRequest("http://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue = getRequestQueue();
        mQueue.add(stringRequest);

        /**
         * 关于post请求，额可以使用四个参数的方法，其中第一个参数可以指定请求方式。
         * StringRequest中并没有提供设置POST参数的方法，但是当发出POST请求的时候，
         * Volley会尝试调用StringRequest的父类——Request中的getParams()方法来获取POST参数，
         * 那么我们只需要在StringRequest的匿名类中重写getParams()方法，在这里设置POST参数就可以了
         */
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url,  listener, errorListener) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("params1", "value1");
//                map.put("params2", "value2");
//                return map;
//            }
//        };

        /**
         * 类似于StringRequest，JsonRequest也是继承自Request类的，
         * 不过由于JsonRequest是一个抽象类，因此我们无法直接创建它的实例，那么只能从它的子类入手了。
         * JsonRequest有两个直接的子类，JsonObjectRequest和JsonArrayRequest，
         * 从名字上看一个是用于请求一段JSON数据的，一个是用于请求一段JSON数组的。
         */
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://m.weather.com.cn/data/101010100.html", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        json.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue = getRequestQueue();
        mQueue.add(jsonObjectRequest);
    }


    public RequestQueue getRequestQueue() {
        if (mQueue == null)
            mQueue = ((VolleyActivity) getActivity()).getRequestQueue();
        return mQueue;
    }
}
