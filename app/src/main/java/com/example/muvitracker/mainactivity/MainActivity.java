package com.example.muvitracker.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.muvitracker.R;
import com.example.muvitracker.mainactivity.boxoffice.BoxofficeFragment;
import com.example.muvitracker.mainactivity.popular.PopularFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

// Manage the interaction with fragments
// ToolBar -> name + refresh + account(firebase)
// Bottom Nav -> main categories cinema, popolari, watchlist, search


public class MainActivity extends AppCompatActivity {

    // COSTANTI
    private final String POPULAR = "Popular";
    private final String BOXOFFICE = "Box Office";
    private final String MYLIST = "My List";


    // ATTRIBUTI
    BottomNavigationView bottomNavigationView;
    MainNavigator navigator = new MainNavigator();

    TextView testoCategoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testoCategoria = findViewById(R.id.testoCategoria);
        bottomNavigationView = findViewById(R.id.bottomNavigation);


        // default home page  OK
        // 2° STEP mostra caricamento - nei vari fragment
        navigator.replaceFragment(this, new PopularFragment());
        testoCategoria.setText(POPULAR);



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override // item -> del bottom nav
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                // OK
                if (itemId == R.id.button1Popular) {
                    // new Fragment popular
                    navigator.replaceFragment(MainActivity.this, new PopularFragment());
                    testoCategoria.setText(POPULAR);
                    return true;
                }

                // OK
                if (itemId == R.id.button2Boxoffice) {
                    navigator.replaceFragment(MainActivity.this, new BoxofficeFragment());
                    testoCategoria.setText(BOXOFFICE);
                    return true;
                }


                // TODO STEP°3 - MYLIST
                if (itemId == R.id.button3MyList) {
                    testoCategoria.setText(MYLIST);
                    return true;
                }

                // TODO SEARCH
                if (itemId == R.id.button4Search) {

                    return true;
                }
                // non reagisco false -> torno al padre
                return false;
            }
        });

    }



}
