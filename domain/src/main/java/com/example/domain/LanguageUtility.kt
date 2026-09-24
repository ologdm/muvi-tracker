package com.example.domain

import java.util.Locale


object LanguageManager {

    /**
     * Returns the current system language in standard format (e.g., "it-IT", "en-US").
     * used for glide images, retrofit api calls, entity mapping
     */
    fun getAppLocaleLanguageTag(): String = Locale.getDefault().toLanguageTag()

}