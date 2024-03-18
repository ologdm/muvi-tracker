package com.example.muvitracker.mainactivity.kotlin.boxo

import com.example.muvitracker.repo.kotlin.dto.BoxoDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum
import com.example.muvitracker.utils.kotlin.RetrofitListCallback
import java.io.IOException

class BoxoPresenter(

    private val view: BoxoContract.View // costruttore view

) : BoxoContract.Presenter {


    private val boxoRepository = BoxoRepo



    // CONTRACT METHODS

    // chiamata OK
    // stati OK
    override fun serverCallAndUpdateUi(forceRefresh: Boolean) {

        if (forceRefresh)
            view.emptyStatesManagment(EmptyStatesEnum.ON_FORCE_REFRESH)
        else
            view.emptyStatesManagment(EmptyStatesEnum.ON_START)

        BoxoRepo.callBoxoServer(object : RetrofitListCallback<BoxoDto> {

            // success
            override fun onSuccess(serverList: List<BoxoDto>) {
                view.updateUi(serverList)
                view.emptyStatesManagment(EmptyStatesEnum.ON_SUCCESS)
            }

            // error
            override fun onError(throwable: Throwable) {
                throwable.printStackTrace()

                if (throwable is IOException)
                    view.emptyStatesManagment(EmptyStatesEnum.ON_ERROR_IO)
                else
                    view.emptyStatesManagment(EmptyStatesEnum.ON_ERROR_OTHER)
            }
        })
    }


    // OK
    override fun onVHolderClick(traktMovieId: Int) {
        view.startDetailsFragment(traktMovieId)
    }



}


