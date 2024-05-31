package com.example.muvitracker.inkotlin.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.ui.main.allmovies.BoxoFragment
import com.example.muvitracker.inkotlin.ui.main.allmovies.PopuFragment
import com.example.muvitracker.inkotlin.ui.main.prefs.PrefsFragmentVM
import com.example.muvitracker.inkotlin.ui.main.search.mvvm.SearFragmentVM
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

// MVVM OK


class MainActivity() : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    val navigator = Navigator()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigation)

        // homepage default - popular
        navigator.replaceFragment(this, PopuFragment())


        // ha come return un booleano
        bottomNavigationView.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                val clickedId = item.itemId

                if (clickedId == R.id.buttonPopular) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        PopuFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (clickedId == R.id.buttonBoxoffice) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        BoxoFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (clickedId == R.id.buttonMyList) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        PrefsFragmentVM()
                    )
                    return@OnItemSelectedListener true
                }

                if (clickedId == R.id.buttonSearch) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        SearFragmentVM()
                    )
                    return@OnItemSelectedListener true
                }
                return@OnItemSelectedListener false // Non reagisco al false -> torno al padre
            })
    }
}


// Kotlin novità
// 1) Dichiarazione - lateinit var (ammettere solo var)
// 2) this@MainActivity == MainActivity.this in java


