package com.example.muvitracker.domain.model

data class Provider(
    val providerId :Int, // uguale per buy, streraming ecc
    val providerName: String,
    val serviceType: String,
    val logoPath: String, // unire

    val displayPriority: Int

)
