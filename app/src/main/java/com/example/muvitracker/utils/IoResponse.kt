package com.example.muvitracker.utils


/*
 * TODO
 *  IoResponse OK
 *  Mapper (per quando serve) OK
 *
 */


sealed interface IoResponse<out T> {
    data class Success<T>(val dataValue: T) : IoResponse<T>
    data object NetworkError : IoResponse<Nothing> // senza costruttore
    data object OtherError : IoResponse<Nothing>
}


fun <T, R> IoResponse<T>.ioMapper(mapper: (T) -> R): IoResponse<R> {
    return when (this) {
        is IoResponse.Success -> IoResponse.Success(mapper(dataValue)) // ricostruisco il success || (mapper(dataValue) passaggio non chiaro
        is IoResponse.NetworkError -> this
        is IoResponse.OtherError -> this
    }
}


/* // !!!! esempio semplificato
data class Success<T>(val dataValue: T) : IoResponse<T>
data object NetworkError : IoResponse<Nothing> // senza costruttore
data object OtherError : IoResponse<Nothing>
 */


//fun <R> Success<List<BoxoDto>>.ioMapper(mapper: (List<BoxoDto>) -> R): IoResponse<R> {
//    return Success(mapper(dataValue))
//}


// ### notes ###
// data object da v 1.9
// <out T> - out serve altrimenti in enqueue non funzione







