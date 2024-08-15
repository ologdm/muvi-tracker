package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.domain.model.DetailMovie

// TODO

@Entity(tableName = "DetailShowEntities")
data class DetailShowEntity(
    @PrimaryKey val traktId: Int,
    val title: String,
    val year: Int,
    @TypeConverters(ConvertersUtils::class) val ids: Ids,
    val overview: String,
    val released: String,
    val runtime: Int,
    val country: String,
    val rating: Float,
    @TypeConverters(ConvertersUtils::class) val genres: List<String>,
)



// TODO
// (PrefsEntity?) - can be null as logic
fun DetailShowEntity.toDomain(prefsEntity: PrefsEntity?): DetailMovie {
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
        watched = prefsEntity?.watched ?: false,
        addedDateTime = prefsEntity?.addedDateTime
    )
}