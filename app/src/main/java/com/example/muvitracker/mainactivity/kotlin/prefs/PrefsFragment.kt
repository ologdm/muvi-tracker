package com.example.muvitracker.mainactivity.kotlin.prefs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.R
import com.example.muvitracker.mainactivity.kotlin.MainNavigatorK
import com.example.muvitracker.repo.kotlin.dto.DetaDto


class PrefsFragment() : Fragment(), PrefsContract.View {


    // ATTRIBUTI

    private lateinit var recyclerView: RecyclerView

    private val adapter = PrefsAdapter()
    private val presenter = PrefsPresenter(this)
    private val navigator = MainNavigatorK()


    // FRAGMENT METHODS

    // creazione
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragm_prefs, container, false)
    }


    // logica
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.mylistRV)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // ok


        adapter.setCallbackVH { movieId ->
            presenter.onVHolderClick(movieId)
            // .> poi startDetails()
        }


        // OK
        adapter.setCallbackLiked {dtoToToggle ->
            presenter.toggleFovoriteItem(dtoToToggle)
            presenter.getPrefsList()
        }


        // OK
        adapter.setCallbackWatched {updatedDto ->
            presenter.updateWatchedItem(updatedDto)
            presenter.getPrefsList()
        }


        // GET OK
        presenter.getPrefsList()


    }




    // CONTRACT

    // OK
    override fun updateUi(list: List<DetaDto>) {
        adapter.updateList(list)
    }


    // OK
    override fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
    }




}