package com.example.muvitracker.ui.main

import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.dto.person.detail.PersonTmdbDto
import com.example.muvitracker.data.dto.person.detail.PersonTraktDto
import com.example.muvitracker.data.dto.person.detail.SocialIds
import com.example.muvitracker.data.dto.person.detail.mergePersonDtoToDomain

fun main() {

    val fakePersonTrakt = PersonTraktDto(
        name = "John Doe",
        ids = Ids(trakt = 12345, slug = "john-doe", imdb = "nm1234567", tmdb = 54321),
        socialIds = SocialIds(instagram = "johndoe", twitter = "johndoe", facebook = "johndoe", wikipedia = "johndoe"),
        biography = "John Doe is a fictional filmmaker known for experimental sci-fi movies.",
        birthday = "1980-01-15",
        death = null,
        birthplace = "Springfield, USA",
        homepage = "https://johndoe.com",
        gender = "male",
        knownForDepartment = "directing"
    )


    // test fallback - list null, empty, item null
    val fakePersonTmdb = PersonTmdbDto(
//        alsoKnownAs = null
//        alsoKnownAs = emptyList(), //  listOf("J. D.", "Johnny D."),
        alsoKnownAs = listOf("J. D.", "Johnny D."),
        biography = "John Doe is a well-known actor and director in indie cinema.",
        birthday = "1980-01-15",
        deathday = null,
        gender = 2,
        homepage = "https://johndoe-film.com",
        id = 99999,
        knownForDepartment = null,
//        knownForDepartment = "Acting",
        name = "John Doe",
        placeOfBirth = "Springfield, USA",
        popularity = 12.3,
        profilePath = "/fake_profile_path.jpg"
    )


//    val person = mergePersonDtoToDomain(fakePersonTrakt,fakePersonTmdb)
    val person = mergePersonDtoToDomain(fakePersonTrakt,null)

    println(person)


}