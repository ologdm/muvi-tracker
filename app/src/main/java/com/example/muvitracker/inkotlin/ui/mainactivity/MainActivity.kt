package com.example.muvitracker.inkotlin.ui.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.ui.mainactivity.boxo.BoxoFragment
import com.example.muvitracker.inkotlin.ui.mainactivity.boxo.mvvm.BoxoFragmentVM
import com.example.muvitracker.inkotlin.ui.mainactivity.popu.PopuFragment
import com.example.muvitracker.inkotlin.ui.mainactivity.popu.mvvm.PopuFragmentVM
import com.example.muvitracker.inkotlin.ui.mainactivity.prefs.PrefsFragment
import com.example.muvitracker.inkotlin.ui.mainactivity.prefs.mvvm_test.PrefsFragmentVM
import com.example.muvitracker.inkotlin.ui.mainactivity.search.SearFragment
import com.example.muvitracker.inkotlin.ui.mainactivity.search.mvvm.SearFragmentVM
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


// OK
class MainActivity() : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    val navigator = MainNavigator()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigation)

        // homepage default - popular
//        navigator.replaceFragment(this, PopuFragmentVM()) // MVVM
        navigator.replaceFragment(this, PopuFragment())


        // ha come return un booleano
        bottomNavigationView.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                val itemId = item.itemId // prelevo item dove clicko

                // paragono id con quelli esistenti
                if (itemId == R.id.buttonPopular) {
                    navigator.replaceFragment(
                        this@MainActivity,
//                        PopuFragmentVM() // MVVM
                        PopuFragment() // di koltin
                    )
                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.buttonBoxoffice) {
                    navigator.replaceFragment(
                        this@MainActivity,
//                        BoxoFragmentVM() // MVVM
                        BoxoFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.buttonMyList) {
                    navigator.replaceFragment(
                        this@MainActivity,
//                        PrefsFragmentVM() // MVVM
                        PrefsFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.buttonSearch) {
                    navigator.replaceFragment(
                        this@MainActivity,
//                        SearFragmentVM() // MVVM
                        SearFragment()
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


