package com.example.muvitracker.ui.main.prefs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.R
import com.example.muvitracker.ui.main.Navigator


class PrefsFragment() : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = PrefsAdapter()
    private val navigator = Navigator()

    private val viewModel by viewModels<PrefsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragm_prefs, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // ok

        viewModel.preftList.observe(viewLifecycleOwner, Observer {
            adapter.updateList(it)
        })

        adapter.setCallbackVH { movieId ->
            startDetailsFragment(movieId)
        }

        adapter.setCallbackLiked { dtoToToggle ->
            viewModel.toggleFovoriteItem(dtoToToggle)
            viewModel.updatePrefList()
        }

        adapter.setCallbackWatched { updatedDto ->
            viewModel.updateWatchedItem(updatedDto)
            viewModel.updatePrefList()
        }

        viewModel.updatePrefList()
    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragment(
            requireActivity(),
            movieId
        )
    }
}