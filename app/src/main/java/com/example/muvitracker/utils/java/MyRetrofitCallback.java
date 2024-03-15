package com.example.muvitracker.utils.java;

import java.util.List;

public interface MyRetrofitCallback<T> {
    public void onSuccess(T obj);
    public void onError(Throwable throwable);
}