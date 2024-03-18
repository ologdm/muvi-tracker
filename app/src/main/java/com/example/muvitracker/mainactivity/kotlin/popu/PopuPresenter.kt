package com.example.muvitracker.mainactivity.kotlin.popu

import com.example.muvitracker.repo.kotlin.dto.PopuDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum
import com.example.muvitracker.utils.kotlin.RetrofitListCallback
import java.io.IOException


class PopuPresenter(

    private val view: PopuContract.View // view nel costruttore, posso inserire chi implementa view

) : PopuContract.Presenter {


    // ATTRIBUTI OK
    private val popuRepository = PopuRepo


    // CONTRACT METHODS
    // 2.1 OK
    override fun serverCallAndUpdate(forceRefresh: Boolean) {

        if (forceRefresh)
            view.emptyStatesFlow(EmptyStatesEnum.ON_FORCE_REFRESH)
        else
            view.emptyStatesFlow(EmptyStatesEnum.ON_START)


        PopuRepo.callPopuServer(
            object : RetrofitListCallback<PopuDto> {

                override fun onSuccess(serverList: List<PopuDto>) {
                    view.UpdateUi(serverList)

                    view.emptyStatesFlow(EmptyStatesEnum.ON_SUCCESS)
                }


                override fun onError(throwable: Throwable) {

                    throwable.printStackTrace()

                    if (throwable is IOException)
                        view.emptyStatesFlow(EmptyStatesEnum.ON_ERROR_IO)
                    else
                        view.emptyStatesFlow(EmptyStatesEnum.ON_ERROR_OTHER)
                }
            })
    }


    // 2.2 OK
    override fun onVHolderCLick(movieId: Int) {
        view.startDetailsFragment(movieId)
    }
}