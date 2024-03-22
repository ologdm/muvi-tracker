package com.example.muvitracker.mainactivity.kotlin.prefs

import com.example.muvitracker.repo.kotlin.dto.DetaDto

class PrefsPresenter(

    private val view: PrefsContract.View

) : PrefsContract.Presenter {


    // OK
    override fun onVHolderClick(movieId: Int) {
        view.startDetailsFragment(movieId)
    }


    // GET

    // OK
    override fun getPrefsList() {
        //da repo passa a update
        view.updateUi(
            list = PrefsRepo.filterPrefsFromDetails()
        )

    }


    // SET
    // solo passare
    override fun toggleFovoriteItem(dtoToToggle: DetaDto) {
        PrefsRepo.toggleFavoriteOnDB(dtoToToggle)
    }

    // passare e modificare
    override fun updateWatchedItem(updatedDto: DetaDto) {
        PrefsRepo.updateWatchedOnDB(updatedDto)
    }



}