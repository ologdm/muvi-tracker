package com.example.muvitracker.mainactivity;

// Navigatore per creazioni fragment e sostituzione in (R.id.frameLayout)

// Funzioni - Replace, Backstack ecc

// ** mi serve Fragment Manager nella funzione, quindi activity come paramentro




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.muvitracker.R;


public class MainNavigator {

    // 1. Sostituisco Fragment
    public void replaceFragment(FragmentActivity fragmActivity, Fragment fragment) {
        //if (fragment == null) { // crea solo se è nullo
            FragmentManager manager = fragmActivity.getSupportFragmentManager();
            manager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        //}
    }

    // 2. Aggiungo Fragment al Backstack
    public void addToBackstackFragment(FragmentActivity fragmActivity, Fragment fragment) {
        //if (fragment == null) { // crea solo se è nullo
        FragmentManager manager = fragmActivity.getSupportFragmentManager();
        manager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit();

    }


}
