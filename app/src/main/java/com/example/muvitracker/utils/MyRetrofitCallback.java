package com.example.muvitracker.utils;

import java.util.List;

public interface MyRetrofitCallback<T> {
    public void onSuccess(T obj);
    public void onError(Throwable throwable);
}