package com.example.muvitracker.mainactivity.popular

import com.example.muvitracker.repository.dto.PopularDtoK
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum
import com.example.muvitracker.utils.kotlin.RetrofitListCallback
import java.io.IOException


class PopularPresenterK(

    private val view: PopularContractK.View // view nel costruttore, posso inserire chi implementa view

) : PopularContractK.Presenter {


    // ATTRIBUTI OK
    private val repositoryPopular = PopularRepoK


    // CONTRACT METHODS
    // 2.1 OK
    override fun serverCallAndUpdate(forceRefresh: Boolean) {

        if (forceRefresh)
            view.emptyStatesFlow(EmptyStatesEnum.ON_FORCE_REFRESH)
        else
            view.emptyStatesFlow(EmptyStatesEnum.ON_START)


        repositoryPopular.callPopular(
            object : RetrofitListCallback<PopularDtoK> {
                override fun onSuccess(serverList: List<PopularDtoK>) {
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