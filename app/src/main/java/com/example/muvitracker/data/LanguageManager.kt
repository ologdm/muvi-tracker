package com.example.muvitracker.data

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import java.util.Locale
import androidx.core.content.edit

object LanguageManager {

    /** Ritorna la lingua di sistema corrente (es. "en", "it", "fr") */
    fun getAppLocaleLanguage(): String {
        return Locale.getDefault().language
    }

    /** Ritorna il locale completo (es. "en-US", "it-IT") */
    fun getAppLocaleLanguageTag(): String {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.getDefault()
        } else {
            @Suppress("DEPRECATION")
            Locale.getDefault()
        }
        return locale.toLanguageTag()
    }

    // SET/ GET LANGUAGE FROM SHARED PREFS -----------------------------------------------------------

    /** Salva la lingua di sistema iniziale (ad esempio nel tuo repository o prefs) */
    fun saveSystemLanguage(prefs: SharedPreferences) {
        val lang = getAppLocaleLanguage()
        prefs.edit { putString(OLD_APP_LANGUAGE, lang) }
    }

    /** Recupera la lingua salvata */
    fun getSavedSystemLanguage(prefs: SharedPreferences): String? {
        return prefs.getString(OLD_APP_LANGUAGE, DEF_LANGUAGE) // lingua default
    }


    // --------------------------------------------------------------------------------------

    //TODO 1.1.3  forza la lingua app per test OK
    fun setAppLocale(context: Context, languageCode: String?): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        // ✅ Supporto per Android 13 e superiori
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(android.app.LocaleManager::class.java)
                ?.applicationLocales = android.os.LocaleList.forLanguageTags(languageCode)
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }


    // TODO fare test e differenze con
//    fun getSystemLanguage(): String { // it
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Resources.getSystem().configuration.locales[0].language
//        } else {
//            @Suppress("DEPRECATION")
//            Resources.getSystem().configuration.locale.language
//        }
//    }
//
//
//    fun getSystemLanguage1(): String { // it-IT
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            android.os.LocaleList.getDefault()[0].toLanguageTag()
//        } else {
//            @Suppress("DEPRECATION")
//            java.util.Locale.getDefault().toLanguageTag()  // su Android < 7 coincide con sistema
//        }
//    }


    // per sharedPrefs
    const val OLD_APP_LANGUAGE = "old_app_language"
    const val DEF_LANGUAGE = "en"
}