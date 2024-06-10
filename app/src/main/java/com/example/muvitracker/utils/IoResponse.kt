package com.example.muvitracker.utils


sealed interface IoResponse<out T> {
    data object Loading : IoResponse<Nothing> // TODO TEST
    data class Success<T>(val dataValue: T) : IoResponse<T>
    data object NetworkError : IoResponse<Nothing>
    data object OtherError : IoResponse<Nothing>


    companion object {
        // se mi serve il tipo generico <IoResponse<T>> uso questa funzione,
        // se mi serve il tipo specific <IoResponse.Success<T>> uso il costruttore
        fun <T> success(dataValue: T): IoResponse<T> {
            return IoResponse.Success(dataValue)
        }
    }
}


fun <T, R> IoResponse<T>.ioMapper(mapper: (T) -> R): IoResponse<R> {
    return when (this) {
        is IoResponse.Success -> IoResponse.Success(mapper(dataValue))
        is IoResponse.NetworkError -> this
        is IoResponse.OtherError -> this
        is IoResponse.Loading -> this // TODO TEST
    }
}







