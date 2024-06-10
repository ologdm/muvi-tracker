package com.example.muvitracker.data.detail

import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.data.prefs.PrefsEntity
import com.example.muvitracker.domain.model.DetailMovie

data class DetailEntity(
    val title: String,
    val year: Int,
    val ids: Ids,
    val overview: String,
    val released: String,
    val runtime: Int,
    val country: String,
    val rating: Float,
    val genres: List<String>,
)


// (PrefsEntity?) - can be null as logic
fun DetailEntity.toDomain(prefsEntity: PrefsEntity?): DetailMovie {
    return DetailMovie(
        title = title,
        year = year,
        ids = ids,
        overview = overview,
        released = released,
        runtime = runtime,
        country = country,
        rating = rating,
        genres = genres,

        liked = prefsEntity?.liked ?: false,
        watched = prefsEntity?.watched ?: false
    )
}


