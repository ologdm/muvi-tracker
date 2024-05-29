package com.example.muvitracker.inkotlin.ui.main.search.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muvitracker.inkotlin.data.dto.search.SearDto
import com.example.muvitracker.inkotlin.data.search.SearRepo
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import java.io.IOException

class SearViewModel() : ViewModel() {


    val searchList = MutableLiveData<List<SearDto>>()


    fun loadNetworkResult(queryText: String) {

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