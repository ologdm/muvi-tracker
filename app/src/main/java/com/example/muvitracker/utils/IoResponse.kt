package com.example.muvitracker.utils


sealed interface IoResponse2<out T> {
    data class Success<T>(val dataValue: T) : IoResponse2<T>
    data class Error(val t: Throwable) : IoResponse2<Nothing>

}


fun <T, R> IoResponse2<T>.ioMapper(mapper: (T) -> R): IoResponse2<R> {
    return when (this) {
        is IoResponse2.Success -> IoResponse2.Success(mapper(dataValue))
        is IoResponse2.Error -> this
    }
}







