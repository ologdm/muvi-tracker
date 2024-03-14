package com.example.muvitracker.repository.dto


data class BoxofficeDtoK(
    val revenue: Int,
    val movie: PopularDtoK
) {

    fun getUrlImage(): String =
        "http://img.omdbapi.com/" + "?apikey=ef6d3d4c" + "&i=${movie.ids.imdb}"

}


// #########################################################################

// revenue + datamodelmovie


/* JSON Boxoffice
  {
    "revenue": 48464322,
    "movie": {
      "title": "Hotel Transylvania 2",
      "year": 2015,
      "ids": {
        "trakt": 103449,
        "slug": "hotel-transylvania-2-2015",
        "imdb": "tt2510894",
        "tmdb": 159824
      }
    }
  },
 */

// Returns the top 10 grossing movies in the U.S. box office last weekend. (10 con piu incassi)
// Updated every Monday morning.



