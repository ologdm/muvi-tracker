package com.example.muvitracker.mainactivity.kotlin.prefs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.muvitracker.R
import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum


class PrefsFragment() : Fragment(), PrefsContract.View {


    // ATTRIBUTI

    //private val


    // FRAGMENT METHODS

    // creazione
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prefs, container, false)
    }


    // logica
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }


    // CONTRACT METHODS
    override fun undateUi(list: List<DetaDto>) {
        TODO("Not yet implemented")
    }

    override fun startDetailsFragment(movieId: Int) {
        TODO("Not yet implemented")
    }

    override fun emptyStatesFlow(emptyStates: EmptyStatesEnum) {
        TODO("Not yet implemented")
    }
}