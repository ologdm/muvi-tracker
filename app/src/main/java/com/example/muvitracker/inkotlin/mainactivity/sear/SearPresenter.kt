package com.example.muvitracker.inkotlin.mainactivity.sear

import com.example.muvitracker.inkotlin.mainactivity.sear.repo.SearRepo
import com.example.muvitracker.inkotlin.repo.dto.search.SearDto
import com.example.muvitracker.myappunti.kotlin.RetrofitCallbackList

class SearPresenter(val view: SearContract.View) : SearContract.Presenter {


/* eugi chiamata veloce
    init {
        getNetworkResult("dima")
    }

 */


    // OK
    override fun getNetworkResult(queryText: String) {

        SearRepo.getNetworkResult(queryText, object : RetrofitCallbackList<SearDto>{

            override fun onSuccess(serverList: List<SearDto>) {
                // aggiorna fragment
                view.updateUi(serverList)
            }

            override fun onError(throwable: Throwable) {
                // TODO: gestire - no results, no internet
            }

        })

    }


    override fun onVHolderClick(traktMovieId: Int) {
        view.startDetailsFragment(traktMovieId)
    }


}