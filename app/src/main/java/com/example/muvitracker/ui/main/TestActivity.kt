package com.example.muvitracker.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {

    // inizializzo shared - modo 1
    val prefs1 by lazy { getSharedPreferences("MyAppPreferences", MODE_PRIVATE) }

    // inizializzo shared - modo 2
    lateinit var prefs2: SharedPreferences

    val editor = prefs1.edit()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inizializzo shared - modo 2.1
        prefs2 = applicationContext.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)


        // salvo valore
        editor.putBoolean("isLogged", true) // (key_valore, valore)
            .apply() // uso apply per il salvataggio assincrono


        // pesco valore
        val x = prefs1.getBoolean("isLogged", false)


    }
}

