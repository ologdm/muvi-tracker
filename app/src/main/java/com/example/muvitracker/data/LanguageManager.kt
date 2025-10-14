package com.example.muvitracker.data

import android.content.Context
import android.os.Build
import java.util.Locale

object LanguageManager {

    /** Ritorna la lingua di sistema corrente (es. "en", "it", "fr") */
    fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }

    /** Ritorna il locale completo (es. "en-US", "it-IT") */
    fun getSystemLocaleTag(): String {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.getDefault()
        } else {
            @Suppress("DEPRECATION")
            Locale.getDefault()
        }
//        return locale.toLanguageTag()
//        return "it-IT"
//        return "en-EN"
        return "es-ES"
    }


    /** Salva la lingua di sistema iniziale (ad esempio nel tuo repository o prefs) */
    fun saveSystemLanguage(context: Context) {
        val lang = getSystemLanguage()
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("system_language", lang)
            .apply()
    }

    /** Recupera la lingua salvata */
    fun getSavedSystemLanguage(context: Context): String? {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("system_language", null)
    }

}