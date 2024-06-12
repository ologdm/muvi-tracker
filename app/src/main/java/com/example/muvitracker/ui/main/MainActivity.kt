package com.example.muvitracker.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.muvitracker.R
import com.example.muvitracker.ui.main.allmovies.BoxoFragment
import com.example.muvitracker.ui.main.allmovies.PopularFragment
import com.example.muvitracker.ui.main.prefs.PrefsFragment
import com.example.muvitracker.ui.main.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity() : AppCompatActivity(R.layout.activity_main) {

    private lateinit var bottomNavigationView: BottomNavigationView
    @Inject
    lateinit var navigator :Navigator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomNavigationView = findViewById(R.id.bottomNavigation)

        navigator.replaceFragment( PopularFragment())

        bottomNavigationView.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                val clickedId = item.itemId

                if (clickedId == R.id.buttonPopular) {
                    navigator.replaceFragment(
                        PopularFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (clickedId == R.id.buttonBoxoffice) {
                    navigator.replaceFragment(
                        BoxoFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (clickedId == R.id.buttonMyList) {
                    navigator.replaceFragment(
                        PrefsFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (clickedId == R.id.buttonSearch) {
                    navigator.replaceFragment(
                        SearchFragment()
                    )
                    return@OnItemSelectedListener true
                }
                return@OnItemSelectedListener false
            })
    }
}



