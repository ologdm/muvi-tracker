package com.example.muvitracker.injava.mainactivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.muvitracker.R;
import com.example.muvitracker.injava.mainactivity.boxoffice.BoxofficeFragment;
import com.example.muvitracker.injava.mainactivity.mylist.MylistFragment;
import com.example.muvitracker.injava.mainactivity.popular.PopularFragment;
import com.example.muvitracker.injava.mainactivity.search.SearchFragment;
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

    // COSTANTI
    private final String POPULAR_TEXT = "Popular";
    private final String BOX_OFFICE_TEXT = "Box Office";
    private final String MY_LIST_TEXT = "My List";
    private final String SEARCH_TEXT = "Search";


    // ATTRIBUTI
    BottomNavigationView bottomNavigationView;
    MainNavigator navigator = new MainNavigator();
    TextView testoCategoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 3.1 Attribuzione
        bottomNavigationView = findViewById(R.id.bottomNavigation);


        // 3.2 Default HomePage -> Popular
        navigator.replaceFragment(this, new PopularFragment());
        testoCategoria.setText(POPULAR_TEXT);


        // 3.3 Click Elemento
        //     se id corrisdponde -> true, {..fai..}
        //     se id non corrisp -> false, torna al padre
        bottomNavigationView.setOnItemSelectedListener(
            new NavigationBarView.OnItemSelectedListener() {
                @Override // !!! item -> del bottom nav
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    int itemId = item.getItemId();

                    // POPULAR
                    if (itemId == R.id.button1Popular) {
                        // new Fragment popular
                        navigator.replaceFragment(
                            MainActivity.this,
                            new PopularFragment());
                        testoCategoria.setText(POPULAR_TEXT);
                        return true;
                    }

                    // BOXOFFICE
                    if (itemId == R.id.button2Boxoffice) {
                        navigator.replaceFragment(
                            MainActivity.this,
                            new BoxofficeFragment()
                        );
                        testoCategoria.setText(BOX_OFFICE_TEXT);
                        return true;
                    }

                    // SEARCH
                    if (itemId == R.id.button4Search) {
                        navigator.replaceFragment(
                            MainActivity.this,
                            new SearchFragment()
                        );
                        testoCategoria.setText(SEARCH_TEXT);
                        return true;
                    }

                    // MYLIST - da fare
                    if (itemId == R.id.button3MyList) {
                        testoCategoria.setText(MY_LIST_TEXT);
                        navigator.replaceFragment(MainActivity.this, new MylistFragment());
                        return true;
                    }

                    // Non reagisco false -> torno al padre
                    return false;
                }
            });


    }


}
