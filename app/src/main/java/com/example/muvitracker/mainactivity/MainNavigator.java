package com.example.muvitracker.mainactivity;

// Navigatore per creazioni fragment e sostituzione in (R.id.frameLayout)

// Funzioni - Replace, Backstack ecc

// ** mi serve Fragment Manager nella funzione, quindi activity come paramentro




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.muvitracker.R;
import com.example.muvitracker.mainactivity.details.DetailsFragment;


public class MainNavigator {

    // 1. Sostituisco Fragment
    public void replaceFragment(FragmentActivity fragmActivity, Fragment fragment) {
            FragmentManager manager = fragmActivity.getSupportFragmentManager();
            manager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        //}
    }

    // 2. Aggiungo Fragment al Backstack
    public void addToBackstackFragment(FragmentActivity fragmActivity, Fragment fragment) {
        FragmentManager manager = fragmActivity.getSupportFragmentManager();
        manager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit();

    }


    // Crea Details - add to backstack
    public void startDetailsFragmentAndAddToBackstack (FragmentActivity fragmActivity, int traktMovieId){
        FragmentManager manager = fragmActivity.getSupportFragmentManager();
        manager.beginTransaction()
            .replace(R.id.frameLayout, DetailsFragment.create(traktMovieId))
            .addToBackStack(null)
            .commit();
    }


}
