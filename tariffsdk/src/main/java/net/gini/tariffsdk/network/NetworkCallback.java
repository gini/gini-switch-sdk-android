package net.gini.tariffsdk.network;


public interface NetworkCallback<T> {

    void onError(Exception e);

    void onSuccess(T t);
}
