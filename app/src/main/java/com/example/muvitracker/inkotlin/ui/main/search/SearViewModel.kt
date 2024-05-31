package com.example.muvitracker.inkotlin.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muvitracker.inkotlin.data.dto.search.SearDto
import com.example.muvitracker.inkotlin.data.search.OldSearRepo
import com.example.muvitracker.inkotlin.data.search.SearRepo
import com.example.muvitracker.inkotlin.utils.IoResponse
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import java.io.IOException

class SearViewModel() : ViewModel() {

    val searchList = MutableLiveData<List<SearDto>>()


    fun loadNetworkResult(queryText: String) {
        SearRepo.getNetworkResult(
            queryText,
            onResponse = { response ->
                if (response is IoResponse.Success) {
                    searchList.value = response.dataValue
                }
            })
    }

}