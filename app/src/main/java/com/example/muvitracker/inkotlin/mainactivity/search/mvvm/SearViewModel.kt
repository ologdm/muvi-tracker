package com.example.muvitracker.inkotlin.mainactivity.search.mvvm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muvitracker.inkotlin.model.dto.search.SearDto
import com.example.muvitracker.inkotlin.model.search.SearRepo
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import java.io.IOException

class SearViewModel(private val context: Context) : ViewModel() {


    val searchList = MutableLiveData<List<SearDto>>()


    fun getNetworkResult(queryText: String) {

        SearRepo.getNetworkResult(
            queryText,
            object : RetrofitCallbackList<SearDto> {
                override fun onSuccess(serverList: List<SearDto>) {
                    searchList.value = serverList
                }

                override fun onError(throwable: Throwable) {
                    if (throwable is IOException) {
                        throwable.printStackTrace()
                    } else {
                        throwable.printStackTrace()
                    }
                }

            })

    }


}