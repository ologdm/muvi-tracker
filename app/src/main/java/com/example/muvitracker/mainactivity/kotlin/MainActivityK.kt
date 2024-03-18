package com.example.muvitracker.mainactivity.kotlin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.muvitracker.R
import com.example.muvitracker.mainactivity.kotlin.boxo.BoxoFragment
import com.example.muvitracker.mainactivity.java.mylist.PrefsFragment
import com.example.muvitracker.mainactivity.kotlin.popu.PopuFragment
import com.example.muvitracker.mainactivity.java.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


// Kotlin novità
// 1) Dichiarazione - lateinit var (ammettere solo var)
// 2) this@MainActivity == MainActivity.this in java
// 3) return@OnItemSelectedListener true ???perche cosi???


// OK
class MainActivityK() : AppCompatActivity() {


    // Dichiarazione - OK -
    //TODO (vedere Kotlin Proprieties)
    private lateinit var testoCategoria: TextView
    private lateinit var bottomNavigationView: BottomNavigationView

    // COSTANTI
    private val POPULAR_TEXT = "popular"
    private val BOX_OFFICE_TEXT = "box office"
    private val MY_LIST_TEXT = "my list"
    private val SEARCH_TEXT = "search"


    // ATTRIBUTI
    val navigator = MainNavigatorK()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // inizializzo view
        testoCategoria = findViewById(R.id.testoCategoria)
        bottomNavigationView = findViewById(R.id.bottomNavigation)


        // homepage default - popular
        testoCategoria.setText(POPULAR_TEXT)
        navigator.replaceFragment(
            this,
            PopuFragment() // di kotlin
        )


        // ha come return un booleano
        bottomNavigationView.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->

                val itemId = item.itemId // prelevo item dove clicko

                // paragono id con quelli esistenti
                if (itemId == R.id.button1Popular) {
                    navigator.replaceFragment(
                        this@MainActivityK,
                        PopuFragment() // di koltin
                    )
                    testoCategoria.setText(POPULAR_TEXT)

                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.button2Boxoffice) {
                    navigator.replaceFragment(
                        this@MainActivityK,
                        BoxoFragment()
                    )
                    testoCategoria.setText(BOX_OFFICE_TEXT)

                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.button3MyList) {
                    navigator.replaceFragment(this@MainActivityK,
                        PrefsFragment()
                    )
                    testoCategoria.setText(MY_LIST_TEXT)

                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.button4Search) {
                    navigator.replaceFragment(this@MainActivityK,
                        SearchFragment()
                    )
                    testoCategoria.setText(SEARCH_TEXT)

                    return@OnItemSelectedListener true
                }


                return@OnItemSelectedListener false // Non reagisco al false -> torno al padre

            })

    }
}