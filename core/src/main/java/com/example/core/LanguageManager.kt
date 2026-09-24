package com.example.core

import android.app.LocaleManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.core.content.edit
import java.util.Locale

// NOTE: only for testing purposes r.1.1.3

//object LanguageManager {
//
//
//    /** Ritorna la lingua di sistema corrente (es. "en", "it", "fr") */
//    // NOTE: not used
//    fun getAppLocaleLanguage(): String {
//        return Locale.getDefault().language
//    }
//
//    /* SET/ GET LANGUAGE FROM SHARED PREFS ------------------------
//     * NOTE: not used, only for testing purposes r.1.1.3
//     */

    // Salva la lingua di sistema iniziale (ad esempio nel tuo repository o prefs)
//    private const val OLD_APP_LANGUAGE = "old_app_language"
//    private const val DEF_LANGUAGE = "en"
//
//    fun saveSystemLanguage(prefs: SharedPreferences) {
//        val lang = getAppLocaleLanguage()
//        prefs.edit { putString(OLD_APP_LANGUAGE, lang) }
//    }
//
//    /** Recupera la lingua salvata */
//    fun getSavedSystemLanguage(prefs: SharedPreferences): String? {
//        return prefs.getString(OLD_APP_LANGUAGE, DEF_LANGUAGE) // lingua default
//    }


    // -----------------------------------------------------

    // NOTE:
    //  - force the app language
    //  - note used, only for testing purposes r.1.1.3
//    fun setAppLocale(context: Context, languageCode: String?): Context {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//
//        val config = Configuration(context.resources.configuration)
//        config.setLocale(locale)
//
//        // ✅ Supporto per Android 13 e superiori
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            context.getSystemService(LocaleManager::class.java)
//                ?.applicationLocales = LocaleList.forLanguageTags(languageCode)
//        }
//
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            context.createConfigurationContext(config)
//        } else {
//            @Suppress("DEPRECATION")
//            context.resources.updateConfiguration(config, context.resources.displayMetrics)
//            context
//        }
//    }


    // TODO: RELEASE fare test e differenze con
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
//
//
//}