package com.example.muvitracker.domain.model

import com.example.muvitracker.domain.model.base.PersonBase

data class CastAndCrew(
    val castMembers: List<CastMember>,
//    val crew: Crew
)


data class CastMember(
    val character: String, // more than one
    val episodeCount: String, // only for shows
    val personBase: PersonBase
)


//data class Crew()


