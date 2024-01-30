package com.example.muvitracker.mainactivity;

// navigazione dei fragment
// mi serve Fragment Manager, quindi activity
// replace, backstack ecc

// debug 2° step - creo/sostituisco fragment solo se ==null, altrimenti no

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.muvitracker.R;


public class MainNavigator {

    // sostituisco Fragment in FrameLayout
    public void replaceFragment(FragmentActivity fragmActivity, Fragment fragment) {
        if (fragment == null) { // crea solo se è nullo
            FragmentManager manager = fragmActivity.getSupportFragmentManager();
            manager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        }
    }


}
