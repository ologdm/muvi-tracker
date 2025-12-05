package com.example.muvitracker

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }


    companion object {
        // Context globale accessibile ovunque
        lateinit var appContext: Context
            private set
    }

}



/*  TODO: - solo per test forzare lingua !!!!!!!!! -------------------------------------------------------------
@HiltAndroidApp
class MyApp : Application() {

    // Forza lingua app - bisogna sembre riaprire app
//        val localeCode = "es"
//        val localeCode = "it"
//        val localeCode = "en"
        val localeCode = "fr"
//    val localeCode = "ro"
//    val localeCode = "ru"


    // !!! solo se forzo lingua !!!
    // base:Context -> 'context grezzo' che Android fornisce all' app prima che venga inizializzata.
    // cambiare la lingua prima -> le risorse vengono lette PRIMA dell’esecuzione di onCreate(), quindi serve fare qua il cambio
    // IMP!!! se forzo sovrascrittura lingua, poi dopo 'Locale.getDefault().language' non restituisce lingua sistema
    override fun attachBaseContext(base: Context) {
        val newContext = LanguageManager.setAppLocale(base, localeCode)
        super.attachBaseContext(newContext)
    }

    override fun onCreate() {
        super.onCreate()

        // !!! solo se forzo lingua !!!
        // Per Android 13+ (API 33), applica anche qui la lingua
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            val localeManager = getSystemService(android.app.LocaleManager::class.java)
//            localeManager?.applicationLocales =
//                android.os.LocaleList.forLanguageTags(localeCode)
//        }
    }

}
 */