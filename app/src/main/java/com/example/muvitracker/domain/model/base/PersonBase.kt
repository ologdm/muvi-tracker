package com.example.muvitracker.domain.model.base

import com.example.muvitracker.data.dto._support.Ids

data class PersonBase(
    val name: String = "N/A",
    val ids: Ids = Ids()
)
