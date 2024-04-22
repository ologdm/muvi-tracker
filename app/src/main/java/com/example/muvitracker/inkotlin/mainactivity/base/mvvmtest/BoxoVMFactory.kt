package com.example.muvitracker.inkotlin.mainactivity.base.a

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.muvitracker.inkotlin.mainactivity.base.mvvmtest.BoxoViewModel
import com.example.muvitracker.inkotlin.mainactivity.base.mvvmtest.PopuViewModel


class BoxoVMFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BoxoViewModel(context) as T
    }
}




