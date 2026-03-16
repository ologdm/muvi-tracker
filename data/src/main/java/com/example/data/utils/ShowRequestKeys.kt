package com.example.data.utils

import com.example.data.dto._support.Ids

data class ShowRequestKeys(
    val showIds: Ids,
    val seasonNr: Int = -1,
    val episodeNr: Int = -1,
)
