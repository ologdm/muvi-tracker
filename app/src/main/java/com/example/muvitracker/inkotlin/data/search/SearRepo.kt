package com.example.muvitracker.inkotlin.data.search

import com.example.muvitracker.inkotlin.data.dto.search.SearDto
import com.example.muvitracker.inkotlin.utils.IoResponse

object SearRepo {

    // senza
    fun getNetworkResult(queryText: String, onResponse: (IoResponse<List<SearDto>>) -> Unit) {

        SearNetworkDS.getServer(
            queryText = queryText,
            onResponse = { retrofitResponse ->

                var list: List<SearDto>? = when (retrofitResponse){
                    is IoResponse.Success ->retrofitResponse.dataValue
                    else -> null
                }
                list
                onResponse(retrofitResponse) // solo bypass dati
                // TODO mapper e ordina
            })
    }


    // funzione per ordine decrescente score
    private fun sort(input: List<SearDto>): List<SearDto> {
        return input.sortedByDescending { it.score }
    }

}



//        OldSearNetworkDS.getServer(
//            queryText,
//            object : RetrofitCallbackList<SearDto> {
//                override fun onSuccess(serverList: List<SearDto>) {
//                    callback.onSuccess(sort(serverList))
//                }
//
//                override fun onError(throwable: Throwable) {
//                    callback.onError(throwable)
//                }
//            })

