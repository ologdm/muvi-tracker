package com.example.muvitracker.data.utils

import com.example.muvitracker.data.dto.utilsdto.Ids

data class ShowRequestKeys(
    val showIds: Ids,
    val seasonNr: Int = -1,
    val episodeNr: Int = -1,
)
