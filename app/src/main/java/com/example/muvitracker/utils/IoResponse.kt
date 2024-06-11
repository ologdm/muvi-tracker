package com.example.muvitracker.utils


sealed interface IoResponse<out T> {
    data class Success<T>(val dataValue: T) : IoResponse<T>
    data object NetworkError : IoResponse<Nothing>
    data object OtherError : IoResponse<Nothing>


    companion object {
        // for generic type IoResponse<T> - use this function
        // for specific type IoResponse.Success<T> - using the constructor
        fun <T> success(dataValue: T): IoResponse<T> {
            return Success(dataValue)
        }
    }
}


fun <T, R> IoResponse<T>.ioMapper(mapper: (T) -> R): IoResponse<R> {
    return when (this) {
        is IoResponse.Success -> IoResponse.Success(mapper(dataValue))
        is IoResponse.NetworkError -> this
        is IoResponse.OtherError -> this
    }
}







