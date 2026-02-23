package com.example.muvitracker.ui.main.providers

import android.os.Bundle
import android.view.View
import com.example.muvitracker.R
import com.example.muvitracker.databinding.DialogCountriesBinding
import com.example.muvitracker.databinding.FragmentEpisodeBottomsheetBinding.bind
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CountriesDialog : BottomSheetDialogFragment(R.layout.dialog_countries) {

    val b by viewBinding (DialogCountriesBinding::bind)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO: lista paesi da selezionare
//        b.countriesRecycler.adapter =
        println("XXX")

    }










}