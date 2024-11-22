package com.example.muvitracker.domain.model.base

import com.example.muvitracker.data.dto.base.Ids

data class Person(
    val name: String = "N/A",
    val ids: Ids = Ids()
)
