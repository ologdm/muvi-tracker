package com.example.muvitracker.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.muvitracker.R;
import com.example.muvitracker.mainactivity.boxoffice.BoxofficeFragment;
import com.example.muvitracker.mainactivity.popular.PopularFragment;
import com.example.muvitracker.mainactivity.search.SearchFragment;
import com.example.muvitracker.repository.SearchRepo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


//          1°STEP-> inizio
// 1. test Api data, RV semplice in Activity
// 2. Button Navigation semplice
// 3. ToolBar -> name + catogories
// 4. Navigator


//          2°STEP
// 1. Manage the interaction with fragments - Navigator
// 2. Refactoring design
// 2. Button Navigation -> popular, boxoffice, mylist, search
//                         funzionamento al click
//


// TODO
//    1 Search
//    2 Mylist


public class MainActivity extends AppCompatActivity {

    // 0. COSTANTI
    private final String POPULAR = "Popular";
    private final String BOX_OFFICE = "Box Office";
    private final String MY_LIST = "My List";
    private final String SEARCH = "Search";


    // 1. ATTRIBUTI
    BottomNavigationView bottomNavigationView;
    MainNavigator navigator = new MainNavigator();

    TextView testoCategoria;


    // 2.

    // 3. METODO ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 3.1 Attribuzione
        testoCategoria = findViewById(R.id.testoCategoria);
        bottomNavigationView = findViewById(R.id.bottomNavigation);


        // 3.2 Default HomePage -> Popular
        navigator.replaceFragment(this, new PopularFragment());
        testoCategoria.setText(POPULAR);


        // 3.3 Click Elemento
        //     se id corrisdponde -> true, {..fai..}
        //     se id non corrisp -> false, torna al padre
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override // !!! item -> del bottom nav
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                // POPULAR
                if (itemId == R.id.button1Popular) {
                    // new Fragment popular
                    navigator.replaceFragment(
                        MainActivity.this,
                        new PopularFragment());
                    testoCategoria.setText(POPULAR);
                    return true;
                }

                // BOXOFFICE
                if (itemId == R.id.button2Boxoffice) {
                    navigator.replaceFragment(
                        MainActivity.this,
                        new BoxofficeFragment()
                    );
                    testoCategoria.setText(BOX_OFFICE);
                    return true;
                }

                // SEARCH
                if (itemId == R.id.button4Search) {
                    navigator.replaceFragment(
                        MainActivity.this,
                        new SearchFragment()
                    );
                    testoCategoria.setText(SEARCH);
                    return true;
                }

                // MYLIST - da fare
                if (itemId == R.id.button3MyList) {
                    testoCategoria.setText(MY_LIST);
                    // TODO 2
                    return true;
                }

                // Non reagisco false -> torno al padre
                return false;
            }
        });


    }


}
