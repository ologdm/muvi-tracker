package com.example.muvitracker.utils;

import java.util.List;

public interface MyRetrofitListCallback<T> {
    public void onSuccess(List<T> list);
    public void onError(Throwable throwable);
}