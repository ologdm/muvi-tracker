package com.example.muvitracker.mainactivity.kotlin.sear.repo

import com.example.muvitracker.repo.kotlin.dto.search.SearDto
import com.example.muvitracker.utils.kotlin.RetrofitCallbackList

object SearRepo {


    fun getNetworkResult(queryText: String, callback: RetrofitCallbackList<SearDto>) {

        SearNetworkDS.getServer(
            queryText, object : RetrofitCallbackList<SearDto> {

                override fun onSuccess(serverList: List<SearDto>) {
                    callback.onSuccess(serverList.sortedByDescending { it.score })

                }

                override fun onError(throwable: Throwable) {
                    callback.onError(throwable)
                }
            })
    }

    fun sort(input: List<SearDto>): List<SearDto> {
        // insertion sort
        // quicksort
        // merge sort
        // bubble sort

        return input.sortedByDescending { it.score }
    }

    /*
        private fun orderResultsByScore(list: List<SearDto>)
                : List<SearDto> {
            //var orderedList = list.

        }

     */



}