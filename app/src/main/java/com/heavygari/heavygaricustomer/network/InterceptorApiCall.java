package com.heavygari.heavygaricustomer.network;


import com.heavygari.heavygaricustomer.base.utils.Print;
import com.heavygari.heavygaricustomer.base.broadcastListeners.ReceiverConnectivity;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Rezwan on 27/4/19.
 */

public class InterceptorApiCall implements Interceptor {
    private static final String TAG = "InterceptorApiCall";
    private static final int RETRY_COUNT = 3;

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (ReceiverConnectivity.isNetworkAvailable()) {
            Request request =  getRequest(chain.request());
            Response response =  chain.proceed(request);

            if(response.code() == 403){
                return tryAgainCauseForbidden(request, chain, RETRY_COUNT);
            }

            return response;
        } else{
           throw new RuntimeException("No Internet");
        }
    }

    private Response tryAgainCauseForbidden(Request request, Chain chain, int retryCount) throws IOException {
        Print.e(TAG, "RETRYING REQUEST DUE TO 403 - "+retryCount);
        Request newRequest;
        newRequest = getRequest(request);
        Response another =  chain.proceed(newRequest);
        retryCount--;
        if (another.code() == 403 && retryCount > 0) {
            tryAgainCauseForbidden(newRequest, chain, retryCount);
        }
        return another;
    }

    private Request getRequest(Request request){
        HttpUrl rootUrl = request.url();
        HttpUrl callUrl = rootUrl.newBuilder().build();
        Print.e(TAG, "Header:"+request.headers().toString());
        Print.e(TAG, "Url:"+request.url().toString());
        return request.newBuilder()
                .url(callUrl)
                .build();

    }

}
