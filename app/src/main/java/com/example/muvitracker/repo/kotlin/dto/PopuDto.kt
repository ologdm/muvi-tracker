package com.example.muvitracker.repo.kotlin.dto

// koltin
// data class - costruttore primario obbligatorio
// tutto val
// - set con costruttore,
// - get con costruttorer implicito
// valori default non servono


// è uguale a MovieDto standard

data class PopuDto(
    val title: String,
    val year: Int,
    val ids: Ids
) {

    // single expression
    fun getImageUrl(): String =
        "http://img.omdbapi.com/" + "?apikey=ef6d3d4c" + "&i=${ids.imdb}"


    data class Ids(
        val trakt: Int = 0,
        var slug: String = "",
        val imdb: String = "",  // utilizzata per immagini
        val tmdb: Int = 0
    )




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






