package com.heavygari.heavygaricustomer.network;

public interface ListenerResponse {
    void onResponse(Object response);
    void onError(Object error);
}
