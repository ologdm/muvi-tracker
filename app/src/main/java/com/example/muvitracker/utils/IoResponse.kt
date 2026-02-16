package com.example.muvitracker.utils


sealed interface IoResponse<out T> {

    data class Success<T>(val dataValue: T) : IoResponse<T>
    data class Error(val t: Throwable) : IoResponse<Nothing>

    companion object {
        fun <T> success(dataValue: T): IoResponse<T> {
            return IoResponse.Success(dataValue)
        }
    }
}


fun <T, R> IoResponse<T>.map(mapper: (T) -> R): IoResponse<R> {
    return when (this) {
        is IoResponse.Success -> IoResponse.Success(mapper(dataValue))
        is IoResponse.Error -> this
    }
}







