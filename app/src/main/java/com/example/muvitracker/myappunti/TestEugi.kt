package com.example.muvitracker.myappunti

// DTO (layer data)
data class PopularDto(
    val title: String,
    val year: Int,
    val ids: Int
)

data class BoxOfficeDto(
    val revenue: Int,
    val movie: PopularDto,
)

// (layer business logic/domain)

data class Movie(
    val title: String,
    val year: Int,
    val ids: Int,
    val revenue: Int?,
)

// funzione estesa mapper per uninificare 2 dto(dati da json)
// serve per non avere l'app troppo legata al dto json
// perche basta modificare il mapper


fun PopularDto.toDomain(): Movie {
    return Movie(
        title = title,
        year = year,
        ids = ids,
        revenue = null,
    )
}


fun BoxOfficeDto.toDomain(): Movie {
    return Movie(
        title = movie.title,
        year = movie.year,
        ids = movie.ids,
        revenue = revenue,
    )
}




class Repository {

    fun getPopularMovie(): Movie {
        val dto = networkCall()
        return dto.toDomain()
    }

    private fun networkCall(): PopularDto {
        // retrofit
        TODO()
    }

}

