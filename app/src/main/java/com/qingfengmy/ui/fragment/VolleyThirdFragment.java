package com.qingfengmy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.qingfengmy.R;
import com.qingfengmy.ui.VolleyActivity;
import com.qingfengmy.ui.entity.Weather;
import com.qingfengmy.ui.entity.WeatherInfo;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/1/15.
 */
public class VolleyThirdFragment extends Fragment {

    @InjectView(R.id.result)
    TextView result;

    RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volley_third, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GsonRequest<Weather> gsonRequest = new GsonRequest<Weather>(
                "http://www.weather.com.cn/data/sk/101010100.html", Weather.class,
                new Response.Listener<Weather>() {
                    @Override
                    public void onResponse(Weather weather) {
                        WeatherInfo weatherInfo = weather.getWeatherinfo();
                        result.setText("city is " + weatherInfo.getCity()+";temp is " + weatherInfo.getTemp()+";time is " + weatherInfo.getTime());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue = getRequestQueue();
        mQueue.add(gsonRequest);

    }

    public RequestQueue getRequestQueue() {
        if (mQueue == null)
            mQueue = ((VolleyActivity) getActivity()).getRequestQueue();
        return mQueue;
    }

    public class GsonRequest<T> extends Request<T> {

        private final Response.Listener<T> mListener;

        private Gson mGson;

        private Class<T> mClass;

        public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener,
                           Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            mGson = new Gson();
            mClass = clazz;
            mListener = listener;
        }

        public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                           Response.ErrorListener errorListener) {
            this(Method.GET, url, clazz, listener, errorListener);
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                return Response.success(mGson.fromJson(jsonString, mClass),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            }
        }

        @Override
        protected void deliverResponse(T response) {
            mListener.onResponse(response);
        }

    }
}
