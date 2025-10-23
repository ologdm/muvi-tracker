package com.example.muvitracker.data.dto.person

import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.domain.model.PersonExtended
import com.example.muvitracker.utils.calculatePersonAge
import com.example.muvitracker.utils.dateFormatterInddMMMyyy
import com.google.gson.annotations.SerializedName


data class PersonTraktDto(
    val name: String?,
    val ids: Ids?,
    @SerializedName("social_ids") val socialIds: SocialIds?,
    val biography: String?,
    val birthday: String?,
    val death: String?,
    val birthplace: String?,
//    val homepage: String?, // website
    val gender: String?,
    @SerializedName("known_for_department") val knownForDepartment: String?,
//    @SerializedName("updated_at") val updatedAt: String?
)

// only for -> PersonExtendedDto
data class SocialIds(
    val twitter: String?,
    val facebook: String?,
    val instagram: String?,
    val wikipedia: String?
)


fun PersonTraktDto.toDomain(): PersonExtended {
    return PersonExtended(
        name = name ?: "N/A",
        ids = ids ?: Ids(),
        biography = biography ?: "N/A",
        birthday = birthday?.dateFormatterInddMMMyyy() ?: "birthdate N/A",
        death = death?.dateFormatterInddMMMyyy() ?: "",
        age = calculatePersonAge(
            birthDate = birthday,
            deathDate = death
        ), // return always a string: N/A or age
        birthplace = birthplace ?: "birthplace N/A",
        knownForDepartment = knownForDepartment ?: "N/A", // acting, etc

        // social platforms links
        twitter = if (socialIds?.twitter != null) "$TWITTER_DOMAIN${socialIds.twitter}" else "N/A",
        facebook = if (socialIds?.facebook != null) "$FACEBOOK_DOMAIN${socialIds.facebook}" else "N/A",
        instagram = if (socialIds?.instagram != null) "$INSTAGRAM_DOMAIN${socialIds.instagram}" else "N/A",
        wikipedia = if (socialIds?.wikipedia != null) "$WIKIPEDIA_DOMAIN${socialIds.wikipedia}" else "N/A"
    )
}


const val TWITTER_DOMAIN = "https://x.com/"
const val FACEBOOK_DOMAIN = "https://x.com/"
const val INSTAGRAM_DOMAIN = "https://x.com/"
const val WIKIPEDIA_DOMAIN = "https://x.com/"


/*
// person extended
// https://api.trakt.tv/people/bryan-cranston?extended=full

JSON
{
    "name": "Bryan Cranston",
    "ids": {
        "trakt": 297737,
        "slug": "bryan-cranston",
        "imdb": "nm0186505",
        "tmdb": 17419,
        "tvrage": null
    },
    "social_ids": {
        "twitter": "BryanCranston",                 // https://x.com/BryanCranston
        "facebook": "thebryancranston",             // https://www.facebook.com/thebryancranston
        "instagram": "bryancranston",               // https://www.instagram.com/bryancranston/
        "wikipedia": null                           // https://en.wikipedia.org/wiki/Bryan_Cranston
    },

    "biography": "Bryan Lee Cranston (born March 7, 1956) is an American actor, director, and producer who is mainly known for portraying Walter White in the AMC crime drama series Breaking Bad (2008–2013) and Hal in the Fox sitcom Malcolm in the Middle (2000–2006). He has received a number of awards—including six Primetime Emmy Awards, four Screen Actors Guild Awards, two Tony Awards, and a Golden Globe Award—with a nomination for an Academy Award and a BAFTA Award.\n\nBryan Cranston's performance on Breaking Bad earned him the Primetime Emmy Award for Outstanding Lead Actor in a Drama Series four times (2008, 2009, 2010, and 2014). After becoming a producer of the show in 2011, he also won the award for Outstanding Drama Series twice. Breaking Bad also earned Cranston five Golden Globe nominations (with one win) and nine Screen Actors Guild Award nominations (with four wins). He was previously nominated three times for Outstanding Supporting Actor in a Comedy Series for his role in Malcolm in the Middle. Cranston co-developed and occasionally appeared in the crime drama series Sneaky Pete (2015–2019) and served as a director for episodes of Malcolm in the Middle, Breaking Bad, Modern Family, and The Office.\n\nIn 2014, Cranston earned a Tony Award for Best Actor in a Play for his portrayal of President Lyndon B. Johnson in the Broadway play All the Way, a role he reprised in the HBO 2016 television film of the same name. In 2018, he received the Laurence Olivier Award for Best Actor in a Play for his portrayal of Howard Beale in the play Network at London's National Theatre, later winning his second Tony Award for Best Actor in a Play for the same role on Broadway. For portraying Dalton Trumbo in the film Trumbo (2015), he received nominations for an Academy Award, a BAFTA Award, a Screen Actors Guild Award and a Golden Globe Award, all for Best Actor in a Leading Role.\n\nCranston has appeared in several other films, such as Saving Private Ryan (1998), Little Miss Sunshine (2006), Drive (2011), Argo (2012), Godzilla (2014), and The Upside (2017). He also provided voice acting in the films Madagascar 3: Europe's Most Wanted (2012), Kung Fu Panda 3 (2016), and Isle of Dogs (2018).",
    "birthday": "1956-03-07",
    "death": null,
    "birthplace": "Hollywood, Los Angeles, California, USA",
    "homepage": null,
    "known_for_department": "acting",
    "gender": "male",
    "updated_at": "2024-11-18T08:08:34.000Z"
}
 */