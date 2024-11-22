package com.example.muvitracker.domain.model

import com.example.muvitracker.domain.model.base.Person

data class CastAndCrew(
    val castMembers: List<CastMember>,
//    val crew: Crew
)


data class CastMember(
    val character: String, // more than one
    val episodeCount: String, // only for shows
    val person: Person
)


//data class Crew()


