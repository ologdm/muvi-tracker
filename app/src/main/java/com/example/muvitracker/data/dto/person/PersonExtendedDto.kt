package com.example.muvitracker.data.dto.person

import com.example.muvitracker.data.dto.base.Ids
import com.google.gson.annotations.SerializedName


data class PersonExtendedDto(
    val name: String? = "",
    val ids: Ids = Ids(),
    @SerializedName("social_ids") val socialIds: SocialIds? = SocialIds(),
    val biography: String? = "",
    val birthday: String?= "",
    val death: String?= "",
    val birthplace: String?= "",
    val homepage: String?= "",
    val gender: String?= "",
    @SerializedName("known_for_department") val knownForDepartment: String? = "",
//    @SerializedName("updated_at") val updatedAt: String?
)

// TODO
//fun PersonExtendedDto.toDomain():PersonExt{
//
//}


// only for 'PersonExtendedDto class'
data class SocialIds(
    val twitter: String?= "",
    val facebook: String?= "",
    val instagram: String?= "",
    val wikipedia: String?= ""
)