package com.example.muvitracker.inkotlin.model.search

import com.example.muvitracker.inkotlin.model.dto.search.SearDto
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList

object SearRepo {


    fun getNetworkResult(queryText: String, callback: RetrofitCallbackList<SearDto>) {

        SearNetworkDS.getServer(
            queryText,
            object : RetrofitCallbackList<SearDto> {
                override fun onSuccess(serverList: List<SearDto>) {
                    callback.onSuccess(sort(serverList))
                }

                override fun onError(throwable: Throwable) {
                    callback.onError(throwable)
                }
            })
    }


    // OK
    // funzione per ordine decrescente score
    fun sort(input: List<SearDto>): List<SearDto> {
        return input.sortedByDescending { it.score }
    }


}