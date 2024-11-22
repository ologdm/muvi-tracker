package com.example.muvitracker.data.dto.person

// show cast, movie cast, detail cast, search

// movie
// https://api.trakt.tv/movies/id/people

//show
// https://api.trakt.tv/shows/id/people

// episode
//https://api.trakt.tv/shows/id/seasons/season/episodes/episode/people

// detail
// https://api.trakt.tv/people/bryan-cranston?extended=full


data class CastResponseDto(
    val cast: List<CastMember>? = emptyList(),
//    val crew: Crew
)


data class CastMember(
    val character: String? = "",
    val characters: List<String>? = emptyList(),
    val episodeCount :Int? = 0, // only for shows
    val person: PersonExtendedDto? = PersonExtendedDto()
)




// non serve
//data class Crew(
//    val production: List<CrewMember>,
//    val art: List<CrewMember>,
//    val crew: List<CrewMember>,
//    @SerializedName("costume & make-up") val costumeAndMakeUp: List<CrewMember>,
//    val directing: List<CrewMember>,
//    val writing: List<CrewMember>,
//    val sound: List<CrewMember>,
//    val camera: List<CrewMember>,
//    @SerializedName("visual_effects") val visualEffects: List<CrewMember>,
//    val lighting: List<CrewMember>,
//    val editing: List<CrewMember>,
//    @SerializedName("created_by")val createdBy: List<CrewMember>, // only for shows

//)
//
//data class CrewMember(
//    val jobs: List<String>,
//    val person: Person
//)