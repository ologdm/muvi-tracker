package com.example.muvitracker.data.dto.person

import com.example.muvitracker.domain.model.CastAndCrew
import com.example.muvitracker.domain.model.CastMember
import com.example.muvitracker.domain.model.base.PersonBase

// cast & crew
// https://api.trakt.tv/movies/id/people
// https://api.trakt.tv/shows/id/people
//https://api.trakt.tv/shows/id/seasons/season/episodes/episode/people


data class CastResponseDto(
    val cast: List<CastMemberDto>?,
//    val crew: Crew
)

fun CastResponseDto.toDomain() :CastAndCrew {
    return CastAndCrew(
        castMembers = cast?.map { it.toDomain() } ?: emptyList()
    )
}


data class CastMemberDto(
    val character: String?, // provide all the characters
//    val characters: List<String>?,<
    val episodeCount :Int?, // only for shows
    val person: PersonBaseDto?
)

fun CastMemberDto.toDomain() :CastMember{
    return CastMember(
        character = character ?: "N/A",
        episodeCount = episodeCount?.toString() ?: "N/A",
        personBase = person?.toDomain() ?: PersonBase()
    )
}


// TODO
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