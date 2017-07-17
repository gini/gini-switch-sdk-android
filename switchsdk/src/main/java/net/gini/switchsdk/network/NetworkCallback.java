package net.gini.switchsdk.network;


public interface NetworkCallback<T> {

    void onError(Exception e);

    void onSuccess(T t);
}
