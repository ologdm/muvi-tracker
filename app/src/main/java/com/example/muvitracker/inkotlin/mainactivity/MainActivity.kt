package com.example.muvitracker.inkotlin.mainactivity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.mainactivity.boxo.BoxoFragment
import com.example.muvitracker.inkotlin.mainactivity.popu.PopuFragment
import com.example.muvitracker.inkotlin.mainactivity.prefs.PrefsFragment
import com.example.muvitracker.inkotlin.mainactivity.sear.SearFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


// Kotlin novità
// 1) Dichiarazione - lateinit var (ammettere solo var)
// 2) this@MainActivity == MainActivity.this in java
// 3) return@OnItemSelectedListener true ???perche cosi???


// OK
class MainActivity() : AppCompatActivity() {


    // Dichiarazione - OK -
    private lateinit var bottomNavigationView: BottomNavigationView


    // ATTRIBUTI
    val navigator = MainNavigator()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigation)

        // homepage default - popular
        navigator.replaceFragment(this, PopuFragment())


        // ha come return un booleano
        bottomNavigationView.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->

                val itemId = item.itemId // prelevo item dove clicko

                // paragono id con quelli esistenti
                if (itemId == R.id.button1Popular) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        PopuFragment() // di koltin
                    )
                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.button2Boxoffice) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        BoxoFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.button3MyList) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        PrefsFragment()
                    )
                    return@OnItemSelectedListener true
                }

                if (itemId == R.id.button4Search) {
                    navigator.replaceFragment(
                        this@MainActivity,
                        SearFragment()
                    )
                    return@OnItemSelectedListener true
                }
                return@OnItemSelectedListener false // Non reagisco al false -> torno al padre
            })
    }




    // eugi esempio
    /* TODO
    appBarLayout.setOnClickListener {
        reqPermissLauncher.launch(Manifest.permission.CAMERA)

    }

}

     */


    /* CAMERA PERMISSION
    // 1) dipendenza a manifest

    // classe anonima per
    val reqPermissLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->

            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }


    private fun requestPermiss() {
        when {
            // 1init - permesso dato. utilizzo
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 1fine - Permission is already granted
                // allow to use the camera
            }

            // 2init - richiesta
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                // 2fine - Additional rationale should be displayed
            }

            // 3init
            else -> {
                // 3fine permission has not been asket yet
            }


        }

     */


}

