package com.example.data.dto._support

// FIXME: Ids Refactor
//@SuppressLint("UnsafeOptInUsageError") // (kotlin.serialization + parcelize insieme) -> non è un errore critico, tutto funziona corettamente
//@Serializable
//data class Ids(
//    val trakt: Int = -1,
//    val slug: String = "",
//    val tvdb: Int = -1,
//    val imdb: String = "", // for images
//    val tmdb: Int = -1
//)
//
//fun Ids.toDomain(): IdsDomain {
//   return IdsDomain(
//        trakt = trakt,
//        slug = slug,
//        tvdb = tvdb,
//        imdb = imdb,
//        tmdb = tmdb
//    )
//}