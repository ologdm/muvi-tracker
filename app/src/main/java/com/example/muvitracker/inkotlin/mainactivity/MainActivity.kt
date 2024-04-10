package com.example.muvitracker.inkotlin.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.mainactivity.base.boxo.BoxoFragment
import com.example.muvitracker.inkotlin.mainactivity.base.popu.PopuFragment
import com.example.muvitracker.inkotlin.mainactivity.prefs.PrefsFragment
import com.example.muvitracker.inkotlin.mainactivity.search.SearFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


// Kotlin novità
// 1) Dichiarazione - lateinit var (ammettere solo var)
// 2) this@MainActivity == MainActivity.this in java


// OK
class MainActivity() : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    val navigator = MainNavigator()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val insets: WindowInsets? = window.decorView.rootWindowInsets
            if (insets != null) {
                val displayCutout: DisplayCutout? = insets.displayCutout
                if (displayCutout != null) {
                    // Il dispositivo ha un notch/cutout
                    // Puoi ottenere ulteriori dettagli come la posizione del notch qui
                }
            }
        }
         */


        bottomNavigationView = findViewById(R.id.bottomNavigation)

        // homepage default - popular
        navigator.replaceFragment(this, PopuFragment())

        // ha come return un booleano
        bottomNavigationView.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                val itemId = item.itemId // prelevo item dove clicko

                // paragono id con quelli esistenti
                if (itemId == R.id.buttonPopular) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        PopuFragment() // di koltin
                    )
                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.buttonBoxoffice) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        BoxoFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.buttonMyList) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        PrefsFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.buttonSearch) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        SearFragment()
                    )
                    return@OnItemSelectedListener true
                }
                return@OnItemSelectedListener false // Non reagisco al false -> torno al padre
            })
    }
}

