package com.qingfengmy.ui.network;

import com.qingfengmy.ui.network.services.JokeApi;
import com.qingfengmy.ui.utils.Constants;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by Administrator on 2015/1/21.
 */
public class ApiClient {

    private RestAdapter restAdapter;

    private final RestAdapter.LogLevel logLevel;

    public ApiClient() {
        this.logLevel = RestAdapter.LogLevel.NONE;
    }

    protected RestAdapter.Builder newRestAdapterBuilder() {
        return new RestAdapter.Builder();
    }

    protected RestAdapter getRestAdapter() {
        if (restAdapter == null) {
            RestAdapter.Builder builder = newRestAdapterBuilder();

            // 测试连测试服务器，正式连正式服务器
            if (Constants.DEBUG) {
                builder.setEndpoint("http://japi.juhe.cn");
            } else {
                builder.setEndpoint("http://japi.juhe.cn");
            }
            // 默认就是gson
//            builder.setConverter(new GsonConverter(ApiHelper.getGsonBuilder().create()));
            // 添加请求头
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade requestFacade) {
                    requestFacade.addHeader("info", "android");
                }
            });

            // 加log日志
            if (Constants.DEBUG) {
                builder.setLogLevel(RestAdapter.LogLevel.FULL);
            }

            restAdapter = builder.build();
        }

        return restAdapter;
    }

    public JokeApi getJokeApi() {
        return getRestAdapter().create(JokeApi.class);
    }
}
