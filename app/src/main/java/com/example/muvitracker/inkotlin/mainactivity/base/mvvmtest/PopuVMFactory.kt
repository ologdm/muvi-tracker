package com.example.muvitracker.inkotlin.mainactivity.base.a

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.muvitracker.inkotlin.mainactivity.base.mvvmtest.PopuViewModel

// -mvvm-test

class PopuVMFactory (private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PopuViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
