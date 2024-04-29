package com.example.muvitracker.inkotlin.ui.mainactivity.prefs

import android.content.Context
import com.example.muvitracker.inkotlin.data.prefs.PrefsRepo
import com.example.muvitracker.inkotlin.data.dto.DetaDto

class PrefsPresenter(
    private val view: PrefsContract.View,
    val context: Context
) : PrefsContract.Presenter {

    val prefsRepo = PrefsRepo.getInstance(context)


    // LOAD
    override fun loadPrefsListAndUpdateUi() {
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


    // ALTRO
    override fun onVHolderClick(movieId: Int) {
        view.startDetailsFragment(movieId)
    }

}