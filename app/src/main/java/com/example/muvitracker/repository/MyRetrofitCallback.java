package com.example.muvitracker.repository;

import java.util.List;

public interface MyRetrofitCallback<T> {
    public void onSuccess(List<T> list);
    public void onError(Throwable throwable);
}