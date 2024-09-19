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

    companion object {
        const val LAST_SELECTED_ID = "last_selected_id"
    }

    private lateinit var bottomNavigationView: BottomNavigationView
    private val prefs by lazy { getSharedPreferences("app_prefs", MODE_PRIVATE) }

    @Inject
    lateinit var navigator: Navigator

    // Mappa tra ID e fragment
    private val fragmentMap = mapOf(
        R.id.buttonMovies to MoviesFragment(),
        R.id.buttonSeries to ShowsFragment(),
        R.id.buttonMyList to PrefsViewpagerFragment(),
        R.id.buttonSearch to SearchFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // sequesta corretta per save istance
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        // 2 Recupera l'ID salvato delle SharedPreferences
        val lastSelectedId = prefs.getInt(LAST_SELECTED_ID, R.id.buttonSeries) // tv_series, default value
        // 3 seleziona button corretto
        bottomNavigationView.selectedItemId = lastSelectedId // default
        // Carica il fragment corretto in base all'ID salvato
        navigator.replaceFragment(
            fragmentMap[lastSelectedId] ?: ShowsFragment()
        ) // Default a ShowsFragment se non trovato


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
                prefs.edit().putInt(LAST_SELECTED_ID, clickedId).apply()
                true// return di ogni elemento selezionato
            })

    }
}



