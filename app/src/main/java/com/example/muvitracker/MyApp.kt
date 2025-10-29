package com.example.muvitracker

import android.app.Application
import android.content.Context
import android.os.Build
import com.bumptech.glide.Glide
import com.example.muvitracker.data.LanguageManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@HiltAndroidApp
class MyApp : Application() {

    // TODO 1.1.3 - funzioni per test lingue app
    // Forza lingua app - bisogna sembre riaprire app
//        val localeCode = "es"
//        val localeCode = "it"
//        val localeCode = "en"
//        val localeCode = "fr"
    val localeCode = "ro"
//    val localeCode = "ru"

    companion object {
        // per sharedPrefs
        const val OLD_APP_LANGUAGE_PREF = "glide_language"

        // Context globale accessibile ovunque
        lateinit var appContext: Context
            private set
    }

    override fun attachBaseContext(base: Context) {
        val newContext = LanguageManager.setAppLocale(base, localeCode)
        super.attachBaseContext(newContext)
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        // Per Android 13+ (API 33), applica anche qui la lingua
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = getSystemService(android.app.LocaleManager::class.java)
            localeManager?.applicationLocales =
                android.os.LocaleList.forLanguageTags(localeCode)
        }

        // 1.1.3 - invalida immagini glide
        // Controllo e pulizia cache Glide se lingua cambiata
        checkAndClearGlideCacheIfLanguageChanged()
    }


    // TODO - spostare in GlideUtils alla fine
    private fun checkAndClearGlideCacheIfLanguageChanged() {
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val savedLang = prefs.getString(OLD_APP_LANGUAGE_PREF, null)

        if (savedLang != localeCode) {
            // Pulizia cache disco
            CoroutineScope(Dispatchers.IO).launch {
                // 1. Pulizia cache disco su thread I/O - operazione può essere lunga, in background
                Glide.get(this@MyApp).clearDiskCache()

                // 2. Pulizia RAM cache sul main thread - perchè è collegata al thread UI di Android
                withContext(Dispatchers.Main) {
                    Glide.get(this@MyApp).clearMemory()
                }

                // Salva lingua corrente
                prefs.edit().putString(OLD_APP_LANGUAGE_PREF, localeCode).apply()
            }
        }
    }


}