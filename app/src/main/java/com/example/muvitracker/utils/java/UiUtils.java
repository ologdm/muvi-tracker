package com.example.muvitracker.utils.java;

import android.view.View;

import androidx.annotation.NonNull;


// funzione per progress bar

// TODO  - fare singleView, doubleView


public class UiUtils {


    // EMPTY STATES MANAGEMENT

    // 1. Costanti
    public static final String NO_INTERNET = "no internet connection PROVA";
    public static final String OTHER_ERROR_MESSAGE = "something went wrong";


    // 2. Visibility
    // 2.1
    @NonNull
    public static void setSingleViewVisibility(View view, Visibility visibility) {
        if (visibility == Visibility.SHOW) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    // 2.2
    public static void setDoubleViewVisibility(View view1, View view2, Visibility visibility) {
        if (visibility == Visibility.SHOW) {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        } else {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }

    }





}
