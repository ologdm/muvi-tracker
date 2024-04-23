package com.example.muvitracker.inkotlin.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.muvitracker.inkotlin.mainactivity.base.`mvvm-test`.BoxoViewModel
import com.example.muvitracker.inkotlin.mainactivity.base.`mvvm-test`.PopuViewModel
import com.example.muvitracker.inkotlin.mainactivity.details.mvvm_test.DetaViewModel
import com.example.muvitracker.inkotlin.mainactivity.prefs.mvvm_test.PrefsViewModel
import com.example.muvitracker.inkotlin.mainactivity.search.mvvm.SearViewModel

// -mvvm-test
// base: popu e boxo
// details
// prefs

// completa
class PopuVMFactory (private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PopuViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class BoxoVMFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BoxoViewModel(context) as T
    }
}


class DetaVMFactory(val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetaViewModel(context) as T
    }
}


class PrefsVMFactory (val context: Context):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PrefsViewModel(context) as T
    }
}

class SearchVMFactory (val context: Context):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearViewModel(context) as T
    }
}







