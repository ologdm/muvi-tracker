package com.example.muvitracker.mainactivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import androidx.annotation.NonNull;


// funzione per progress bar

// TODO  - fare singleView, doubleView


public class UiUtils {

    // eugi OK
    @NonNull
    public static void setSingleViewVisibility(
        View view,
        VisibilityEnum visibility) {
        if (visibility == VisibilityEnum.SHOW) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    // OK
    public static void setDoubleViewVisibility(View view1, View view2, VisibilityEnum visibility) {
        if (visibility == VisibilityEnum.SHOW) {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        } else {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }

    }






}
