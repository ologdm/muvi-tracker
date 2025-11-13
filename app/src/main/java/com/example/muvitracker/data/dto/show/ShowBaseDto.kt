package com.example.muvitracker.data.dto.show

import android.annotation.SuppressLint
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.domain.model.base.ShowBase
import kotlinx.serialization.Serializable


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ShowBaseDto(
    val title: String,
    val year: Int,
    val ids: Ids
)


fun ShowBaseDto.toDomain(): ShowBase {
    return ShowBase(
        title = this.title,
        year = this.year,
        ids = this.ids
    )
}