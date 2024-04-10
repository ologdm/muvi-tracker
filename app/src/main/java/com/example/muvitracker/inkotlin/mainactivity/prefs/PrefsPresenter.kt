package com.example.muvitracker.inkotlin.mainactivity.prefs

import android.content.Context
import com.example.muvitracker.inkotlin.repo.PrefsRepo
import com.example.muvitracker.inkotlin.repo.dto.DetaDto


class PrefsPresenter(

    private val view: PrefsContract.View,
    val context: Context

) : PrefsContract.Presenter {


    val prefsRepo = PrefsRepo.getInstance(context)


    // OK
    override fun onVHolderClick(movieId: Int) {
        view.startDetailsFragment(movieId)
    }


    // GET

    // OK
    override fun getPrefsListAndUpdateUi() {
        //da repo passa a update
        view.updateUi(
            list = prefsRepo.filterPrefsFromDetails()
        )
    }


    // SET
    // solo passare
    override fun toggleFovoriteItem(dtoToToggle: DetaDto) {
        prefsRepo.toggleFavoriteOnDB(dtoToToggle)
    }

    // passare e modificare
    override fun updateWatchedItem(updatedDto: DetaDto) {
        prefsRepo.updateWatchedOnDB(updatedDto)
    }


}