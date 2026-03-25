package com.example.presentation.providers

import android.os.Bundle
import android.view.View
import com.example.presentation.R
import com.example.presentation.databinding.DialogCountriesBinding
import com.example.presentation.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CountriesDialog : BottomSheetDialogFragment(R.layout.dialog_countries) {

    val b by viewBinding (DialogCountriesBinding::bind)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO: lista paesi da selezionare
//        b.countriesRecycler.adapter =
        println("XXX")

    }










}