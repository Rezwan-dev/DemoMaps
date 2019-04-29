package com.heavygari.heavygaricustomer.network;



import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class Client {
    private static  Client instance;
    private APIs apis;

    private Client() {
        apis = getRetrofit(getOkHttpClient()).create(APIs.class);
    }

    static Client getInstance() {
        if(instance == null){
            instance = new Client();
        }
        return instance;
    }


    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new InterceptorApiCall());
        okHttpClient.connectTimeout(1500 * 60, TimeUnit.MILLISECONDS);
        okHttpClient.readTimeout(1000 * 60, TimeUnit.MILLISECONDS);
        return okHttpClient.build();
    }

    private Retrofit getRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public APIs getApis() {
        return apis;
    }
}
