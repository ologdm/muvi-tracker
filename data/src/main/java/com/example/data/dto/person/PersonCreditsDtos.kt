package com.example.data.dto.person

import android.annotation.SuppressLint
import com.example.data.dto.movie.detail.MovieTraktDto
import com.example.data.dto.show.detail.ShowTraktDto
import com.example.domain.model.Ids
import com.example.domain.model.PersonCredit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// TODO: 24-09-2026 - person credits need to be finished


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TraktCreditsResponseDto(
    val cast: List<TraktCreditDto>? = null,
//    val crew: Map<String, List<PersonCreditDto>>? = null, // TODO check gestione
)


// NOTE: Mark all optional fields as nullable
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TraktCreditDto(
    val character: String? = null,
//    val characters: List<String>? = null,
    //
    val show: ShowTraktDto? = null,
    val movie: MovieTraktDto? = null,
    // solo x  shows
    @SerialName("episode_count") val episodeCount: Int? = null, // 1
    @SerialName("series_regular") val seriesRegular: Boolean? = null, // false
    // solo crew, es directing
    val job: String? = null, // "Assistant Director"
    val jobs: List<String>? = null, // ["Assistant Director", "Assistant"]
) {

    // TODO: 03-06-2026 - dati aggiuntivi su dto corrispettivi movie o show
//    val releasedYear = released?.substring(0, 3)
//    val firstAiredYear = firstAired?.substring(0, 3)

    // NOTE: isShow -> Essential to prevent the serializer from looking for this field in the JSON, avoiding conflicts
    val isShow get() = show != null

    // from movie/show
    val title get() = if (isShow) show?.title else movie?.title

    val year get() = if (isShow) show?.year else movie?.year

    val ids get() = if (isShow) show?.ids else movie?.ids

    val status get() = if (isShow) show?.status else movie?.status // "in production", "canceled", "released"

    val overview get() = if (isShow) show?.overview else movie?.overview
}


fun TraktCreditDto.toDomain(): PersonCredit {
    return PersonCredit(
        isShow = isShow,
        //
        title = title,
        year = year,
        ids = ids ?: Ids(),
        status = status,
        overview = overview,
        //
        character = character,
        // solo shows
        seriesRegular = seriesRegular,
        episodeCount = episodeCount,
        // solo crew
        job = job,
        jobs = jobs
    )
}

// test person credits:
// slug: david-corennswet | superman
// trakt: 852412
// tmdb: 1785590

