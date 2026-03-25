package com.example.data.utils

import com.example.domain.model.Ids


data class ShowRequestKeys(
    val showIds: Ids,
    val seasonNr: Int = -1,
    val episodeNr: Int = -1,
)
