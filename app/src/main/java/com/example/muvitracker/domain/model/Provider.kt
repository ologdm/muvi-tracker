package com.example.muvitracker.domain.model


data class Provider(
    val id: Int,
    val name: String,
    val serviceType: String,
    private val logoPath: String,
    val displayPriority: Int,

    private val website: String = "https://image.tmdb.org/t/p/w500"
) {

    val logoUrl: String? // !! Glide gestisce i null
        get() = logoPath
            .takeIf { path -> path.isNotBlank() }
            ?.let { path ->
                "$website$path"
            }

}
