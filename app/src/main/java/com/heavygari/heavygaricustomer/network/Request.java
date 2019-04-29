package com.heavygari.heavygaricustomer.network;

public class Request {

    private ListenerResponse listenerResponse;
    private APIs apis;

    public Request(ListenerResponse listenerResponse){
        this.listenerResponse = listenerResponse;
        apis = Client.getInstance().getApis();
    }
}
