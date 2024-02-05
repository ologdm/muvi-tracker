package com.example.muvitracker.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import javax.security.auth.callback.Callback;


// funzione per progress bar

// TODO  - fare singleView, doubleView


public class UiUtils {

    // eugi OK
    @NonNull
    public static void setSingleViewVisibility(
        View view,
        Visibility visibility) {
        if (visibility == Visibility.SHOW) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    // OK
    public static void setDoubleViewVisibility(View view1, View view2, Visibility visibility) {
        if (visibility == Visibility.SHOW) {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        } else {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }

    }



    /* SWIPE TO REFRESH IN ACTIVITY - PROVA
    public static void refreshButton (View view, Fragment fragment){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    public static void prova (View.OnClickListener o, Fragment fragment){

    }

     */




}
