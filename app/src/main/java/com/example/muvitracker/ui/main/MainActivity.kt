package com.example.muvitracker.ui.main

import android.content.SharedPreferences
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

    companion object {
        private const val LAST_SELECTED_ID = "last_selected_id"
    }

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    @Inject
    lateinit var navigator: Navigator

    private lateinit var bottomNavigationView: BottomNavigationView

    // Mappa tra ID e fragment
    private val fragmentMap = mapOf(
        R.id.buttonMovies to MoviesFragment(),
        R.id.buttonSeries to ShowsFragment(),
        R.id.buttonMyList to PrefsViewpagerFragment(),
        R.id.buttonSearch to SearchFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomNavigationView = findViewById(R.id.bottomNavigation)

        // 1 Get saved Id from SharedPrefs
        val lastSelectedId =
            sharedPrefs.getInt(LAST_SELECTED_ID, R.id.buttonSeries) // tv_series, default value
        // 2 select the correct button
        bottomNavigationView.selectedItemId = lastSelectedId // default
        // 3 load the correct fragment based on saved Id
        navigator.replaceFragment(
            fragmentMap[lastSelectedId] ?: ShowsFragment()
        )


        bottomNavigationView.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                val clickedId = item.itemId

                when (clickedId) {
                    R.id.buttonMovies -> {
                        navigator.replaceFragment(MoviesFragment())
                    }

                    R.id.buttonSeries -> {
                        navigator.replaceFragment(ShowsFragment())
                    }

                    R.id.buttonMyList -> {
                        navigator.replaceFragment(PrefsViewpagerFragment())
                    }

                    R.id.buttonSearch -> {
                        navigator.replaceFragment(SearchFragment())
                    }

                    else -> return@OnItemSelectedListener false
                }

                // Salva l'ID dell'elemento selezionato nelle SharedPreferences
                sharedPrefs.edit().putInt(LAST_SELECTED_ID, clickedId).apply()

                // return di ogni elemento selezionato
                true
            })

    }
}



