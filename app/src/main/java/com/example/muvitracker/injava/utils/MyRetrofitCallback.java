package com.example.muvitracker.injava.utils;

public interface MyRetrofitCallback<T> {
    public void onSuccess(T obj);
    public void onError(Throwable throwable);
}