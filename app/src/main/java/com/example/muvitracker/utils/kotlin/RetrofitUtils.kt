package com.example.muvitracker.utils.kotlin

// extended function


// RETROFIT
// intesto funzione a Retrofit -> poi retrofit mi genera il codice necessario

/*
fun Retrofit.getMuviTrackerRetrofit()
        : Retrofit {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.trakt.tv/")
}
 */



// INTERFACCE

// liste
interface RetrofitListCallback<T> {
    fun onSuccess(serverList: List<T>);
    fun onError(throwable: Throwable)
}


// singolo
interface RetrofitCallbackK<T> {
    fun onSuccess(serverItem: T);
    fun onError(throwable: Throwable)
}


object RetrofitUtils {

    // TODO: spostare retrofit qua

}