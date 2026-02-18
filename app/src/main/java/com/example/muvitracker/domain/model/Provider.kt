package com.example.muvitracker.domain.model


data class Provider(
    val id: Int,
    val name: String,
    val serviceType: String,
    private val logoPath: String,
    val displayPriority: Int,
    val allProvidersLink: String, // es: https://www.themoviedb.org/movie/293660-deadpool/watch?locale=IT

    private val logoBaseUrl: String = "https://image.tmdb.org/t/p/w500"
) {

    val providerMediaName = "The Hateful Eight"

    val logoUrl: String? // !! Glide gestisce i null
        get() = logoPath
            .takeIf { path -> path.isNotBlank() }
            ?.let { path ->
                "$logoBaseUrl$path"
            }

}
