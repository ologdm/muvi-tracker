package com.example.muvitracker.data.dto.basedto

import com.example.muvitracker.domain.model.base.Movie

// used for popular category - Popularity is calculated using the rating percentage and the number of ratings.

data class MovieDto(
    val title: String,
    val year: Int,
    val ids: Ids
)


fun MovieDto.toDomain(): Movie {
    return Movie(title, year, ids)
}


/* JSON ###################################################################

movie{
    "title": "The Dark Knight",
    "year": 2008,
    "ids": {
      "trakt": 16,
      "slug": "the-dark-knight-2008",
      "imdb": "tt0468569",
      "tmdb": 155
    }
 },

 show{
    "title": "Breaking Bad",
    "year": 2008,
    "ids": {
        "trakt": 1,
        "slug": "breaking-bad",
        "tvdb": 81189,
        "imdb": "tt0903747",
        "tmdb": 1396
    }
}
season{
    "number": 0,
    "ids": {
        "trakt": 1,
        "tvdb": 439371,
        "tmdb": 3577
    }
}
episode{
    "season": 1,
    "number": 1,
    "title": "Pilot",
    "ids": {
        "trakt": 16,
        "tvdb": 349232,
        "imdb": "tt0959621",
        "tmdb": 62085
    }
}
person{
    "name": "Bryan Cranston",
    "ids": {
        "trakt": 142,
        "slug": "bryan-cranston",
        "imdb": "nm0186505",
        "tmdb": 17419
    }
}
user{
    "username": "sean",
    "private": false,
    "name": "Sean Rudford",
    "vip": true,
    "vip_ep": true,
    "ids": {
        "slug": "sean"
    }
}
 */








