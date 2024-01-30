package com.example.muvitracker.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.muvitracker.R;
import com.example.muvitracker.mainactivity.boxofficefragment.BoxofficeFragment;
import com.example.muvitracker.mainactivity.popularfragment.PopularFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

// Manage the interaction with fragments
// ToolBar -> name + refresh + account(firebase)
// Bottom Nav -> main categories cinema, popolari, watchlist, search


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    MainNavigator navigator = new MainNavigator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // default home page  OK
        // 2° STEP mostra caricamento - nei vari fragment
        navigator.replaceFragment(this, new PopularFragment());


        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override // item -> del bottom nav
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                // TODO  - WIP POPULAR
                if (itemId == R.id.button1Popular) {
                    // new Fragment popular
                    navigator.replaceFragment(MainActivity.this, new PopularFragment());
                    return true;
                }

                if (itemId == R.id.button2Boxoffice) {
                    navigator.replaceFragment(MainActivity.this, new BoxofficeFragment());
                    return true;
                }


                // TODO STEP°3 ###########################
                if (itemId == R.id.button3MyList) {
                    // TODO new Fragment MyList
                    return true;
                }
                if (itemId == R.id.button4Search) {
                    // TODO new Fragment Search
                    return true;
                }
                // non reagisco false -> torno al padre
                return false;
            }
        });

    }



    /*
    // *--> si può scrivere anche come IOException, errore comunicazione in/out
    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

     */




}
