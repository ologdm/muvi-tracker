package com.example.muvitracker.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.muvitracker.R
import com.example.muvitracker.databinding.ActivityMainBinding
import com.example.muvitracker.ui.main.allmovies.MoviesFragment
import com.example.muvitracker.ui.main.allshows.ShowsFragment
import com.example.muvitracker.ui.main.prefs.viewpager.PrefsViewpagerFragment
import com.example.muvitracker.ui.main.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val LAST_SELECTED_ID = "last_selected_id"
    }


    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    @Inject
    lateinit var navigator: Navigator


    // Mappa tra ID e fragment - ricrea i fragment ad ogni selezione id
    private val fragmentFactory: Map<Int, () -> Fragment> = mapOf(
        R.id.buttonMovies to { MoviesFragment() },
        R.id.buttonSeries to { ShowsFragment() },
        R.id.buttonMyList to { PrefsViewpagerFragment() },
        R.id.buttonSearch to { SearchFragment() }
    )
    // esempio!! - mantiene in memotria
//    private val fragmentMap = mapOf(
//        R.id.buttonMovies to MoviesFragment(),
//        R.id.buttonSeries to ShowsFragment(),
//        R.id.buttonMyList to PrefsViewpagerFragment(),
//        R.id.buttonSearch to SearchFragment()

    private var currentFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inizializzi il binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Applica padding automaticamente per status bar e navigation bar
        edgeToEdgeManagment()

        // 1 Get saved Id from SharedPrefs
        val lastSelectedId =
            sharedPrefs.getInt(LAST_SELECTED_ID, R.id.buttonSeries) // tv_series, default value
        // 2 select the correct button
        binding.bottomNavigation.selectedItemId = lastSelectedId // default
        // 3 load the correct fragment based on saved Id
        navigator.replaceFragment(
            fragmentFactory[lastSelectedId]?.invoke() ?: ShowsFragment()
        ) // IMP!! (?: fragm di fallback)

        binding.bottomNavigation.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                val clickedId = item.itemId
                val fragment =
                    fragmentFactory[clickedId]?.invoke() ?: return@OnItemSelectedListener false
                navigator.replaceFragment(fragment)

                // Salva ID selezionato
                sharedPrefs.edit().putInt(LAST_SELECTED_ID, clickedId).apply()

                // return di ogni elemento selezionato
                return@OnItemSelectedListener true // versione esplicita, (o true), return di lambda
            })
    }

    private fun edgeToEdgeManagment() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // aggiorno solo lati che mi servono
            v.updatePadding(top = systemBars.top)
            insets
        }
    }
}



