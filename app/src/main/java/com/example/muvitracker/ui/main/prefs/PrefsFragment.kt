package com.example.muvitracker.ui.main.prefs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muvitracker.databinding.FragmPrefsBinding
import com.example.muvitracker.ui.main.Navigator


class PrefsFragment() : Fragment() {

    private var _binding: FragmPrefsBinding? = null
    private val binding
        get() = _binding
    private val viewModel by viewModels<PrefsViewModel>()
    private val navigator = Navigator()


    private val adapter = PrefsAdapter(
        onClickVH = { movieId ->
            startDetailsFragment(movieId)
        },
        onCLickLiked = { item ->
            viewModel.toggleFovoriteItem(itemToToggle = item)
        },
        onClickWatched = { item, watched ->
            viewModel.updateWatchedItem(updatedItem = item, watched)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmPrefsBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.adapter = adapter
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        viewModel.preftList.observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragment(
            requireActivity(),
            movieId
        )
    }

}

