package com.example.muvitracker.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.muvitracker.R
import com.example.muvitracker.ui.main.allmovies.MoviesFragment
import com.example.muvitracker.ui.main.allshows.ShowsFragment
import com.example.muvitracker.ui.main.prefs.viewpager.PrefsViewpagerFragment
import com.example.muvitracker.ui.main.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {


    private lateinit var bottomNavigationView: BottomNavigationView

    @Inject
    lateinit var navigator: Navigator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomNavigationView = findViewById(R.id.bottomNavigation)

        // todo - last open
        navigator.replaceFragment(ShowsFragment()) // default
        bottomNavigationView.selectedItemId = R.id.buttonSeries // default

        bottomNavigationView.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                val clickedId = item.itemId
                if (clickedId == R.id.buttonMovies) {
                    navigator.replaceFragment(
                        MoviesFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (clickedId == R.id.buttonSeries) {
                    navigator.replaceFragment(
                        ShowsFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (clickedId == R.id.buttonMyList) {
                    navigator.replaceFragment(
                        PrefsViewpagerFragment()
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



