package com.example.muvitracker.inkotlin.mainactivity.search

import com.example.muvitracker.inkotlin.model.search.SearRepo
import com.example.muvitracker.inkotlin.model.dto.search.SearDto
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList
import java.io.IOException

class SearPresenter(val view: SearContract.View) : SearContract.Presenter {


    // OK
    override fun getNetworkResult(queryText: String) {

        SearRepo.getNetworkResult(
            queryText,
            object : RetrofitCallbackList<SearDto> {
                override fun onSuccess(serverList: List<SearDto>) {
                    view.updateUi(serverList)
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


    override fun onVHolderClick(traktMovieId: Int) {
        view.startDetailsFragment(traktMovieId)
    }


}