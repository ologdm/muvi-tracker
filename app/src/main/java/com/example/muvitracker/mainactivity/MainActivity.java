package com.example.muvitracker.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.muvitracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Manage the interaction with fragments
// ToolBar -> name + refresh + account(firebase)
// Bottom Nav -> main categories cinema, popolari, watchlist, search




public class MainActivity extends AppCompatActivity {


    MainAdapter mainAdapter = new MainAdapter();
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavigation);
        // TODO if selezione
        // TODO bottom1 - fragment Popular

        // OK
        RecyclerView recyclerView = findViewById(R.id.RVMainMuviTracker);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // OK
        mainAdapter.chiamataRetrofit();



    }


}
