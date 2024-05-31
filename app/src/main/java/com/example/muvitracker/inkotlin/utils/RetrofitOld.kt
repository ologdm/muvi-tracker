package com.example.muvitracker.myappunti.kotlin


/** RETROFIT UTILS
 * Interfacce retrofit
 *
 * funzioni: createMuviTrackerRetrofit() -> crea Istanza Retrofit
 */


// TODO sostituire con IoResponse
// boxo OK

interface RetrofitCallbackList<T> {
    fun onSuccess(serverList: List<T>);
    fun onError(throwable: Throwable)
}


interface RetrofitCallback<T> {
    fun onSuccess(serverItem: T);
    fun onError(throwable: Throwable)
}


