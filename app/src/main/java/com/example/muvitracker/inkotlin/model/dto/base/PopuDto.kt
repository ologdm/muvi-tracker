package com.example.muvitracker.inkotlin.model.dto.base

import com.example.muvitracker.inkotlin.model.dto.support.Ids

// koltin
// data class - costruttore primario obbligatorio
// tutto val
// - set con costruttore,
// - get con costruttorer implicito
// valori default non servono



data class PopuDto(
    val title: String,
    val year: Int,
    val ids: Ids
) {

    // single expression
    fun getImageUrl(): String =
        "http://img.omdbapi.com/?apikey=ef6d3d4c&i=${ids.imdb}"
}






// #########################

// DTO = data transfer object
// serialized serve? - no, solo per passare una classe complessa da un activity fragment ad un altro


/* JSON - Popular (lista standard Movie)
  {
    "title": "The Dark Knight",
    "year": 2008,
    "ids": {
      "trakt": 16,
      "slug": "the-dark-knight-2008",
      "imdb": "tt0468569",
      "tmdb": 155
    }
  },
  */


// Returns the most popular movies.
// Popularity is calculated using the rating percentage and the number of ratings.





