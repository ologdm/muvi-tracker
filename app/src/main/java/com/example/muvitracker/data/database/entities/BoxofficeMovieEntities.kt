package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.domain.model.base.Movie


// ##############################################################################
@Entity(tableName = "boxoffice_movie_entities")
data class BoxofficeMovieEntity(
    @PrimaryKey val traktId: Int,
    val title: String,
    val year: Int,
    @TypeConverters(ConvertersUtils::class) val ids: Ids
)


fun BoxofficeMovieEntity.toDomain(): Movie {
    return Movie(
        title = title,
        year = year,
        ids = ids
    )
}