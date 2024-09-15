package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.ConvertersUtils
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.domain.model.base.Movie


// not used
//@Entity(tableName = "popular_movie_entities")
//data class PopularMovieEntity(
//    @PrimaryKey val traktId: Int,
//    val title: String,
//    val year: Int,
//    @TypeConverters(ConvertersUtils::class) val ids: Ids
//)
//
//fun PopularMovieEntity.toDomain(): Movie {
//    return Movie(
//        title = title,
//        year = year,
//        ids = ids
//    )
//}


// ##############################################################################
@Entity(tableName = "boxoffice_movie_entities")
data class BoxoMovieEntity(
    @PrimaryKey val traktId: Int,
    val title: String,
    val year: Int,
    @TypeConverters(ConvertersUtils::class) val ids: Ids
)


fun BoxoMovieEntity.toDomain(): Movie {
    return Movie(
        title = title,
        year = year,
        ids = ids
    )
}