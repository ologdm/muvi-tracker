package com.example.muvitracker.data.dto.provider

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: 1.1.4 - providers
// response tipe:
// 1. buy -  acquista
// 2. rent - noleggia
// 3. flatrate - streaming abbonamento
// 4. free - Film disponibile gratuitamente, senza costi di acquisto o abbonamento.
// 5. ads - Film disponibile gratuitamente ma con pubblicità.
// link - link per tmdb alla sezione providers


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MovieProvidersResponseDto(
    val id: Int, // movie id
    val results: Map<String, RegionProvidersDto> = emptyMap() // "AO" -> RegionProviders
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
class RegionProvidersDto(
    // 1. link generale
    val link: String?,

    // 2. una delle 5 opzioni:
    val buy: List<ProviderDto> = emptyList(), // acquisto
    val flatrate: List<ProviderDto> = emptyList(), // streaming con abbonnamewnto
    val rent: List<ProviderDto> = emptyList(), // noleggio
    val free: List<ProviderDto> = emptyList(), // acquisto
    val ads: List<ProviderDto> = emptyList() // acquisto
)


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ProviderDto(
    @SerialName("provider_id") val providerId: Int,
    @SerialName("provider_name") val providerName: String?,
    @SerialName("logo_path") val logoPath: String?,
    @SerialName("display_priority") val displayPriority: Int?, //,1,2,3
)


/*
ignoreUnknownKeys = true →
        se il JSON restituito dall’API ha campi extra che non sono definiti nei tuoi data class,
        li ignora invece di lanciare un errore.

isLenient = true →
        permette al parser di essere più permissivo su JSON “non standard”
        (ad esempio virgolette mancanti o valori nullabili).

encodeDefaults = true →  TODO importante !!!!!
        quando serializzi oggetti, include anche i valori di default delle proprietà.

coerceInputValues = true → TODO importante !!!!!
        se un campo del JSON è null ma la tua data class ha un valore di default,
        lo userà automaticamente invece di fallire.
 */