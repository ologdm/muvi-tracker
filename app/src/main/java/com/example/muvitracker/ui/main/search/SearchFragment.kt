package com.example.muvitracker.ui.main.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.databinding.FragmSearchBinding
import com.example.muvitracker.ui.main.Navigator

/*
 *  keyboard visibility management:
 *    - Manifest - ("adjustNothing")
 *    - xml layout - (android:imeOptions="actionDone") + (inputType="text")
 */


class SearchFragment : Fragment() {

    private var _binding: FragmSearchBinding? = null
    private val binding
        get() = _binding

    private val navigator = Navigator()
    private val viewModel by viewModels<SearchViewModel>()

    // Debouncing
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val DEBOUNCE_DELAY: Long = 300L

    private val adapter = SearchAdapter(onClickVH = { movieId ->
        startDetailsFragment(movieId)
    })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmSearchBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        viewModel.state().observe(viewLifecycleOwner, Observer { searchList ->
            adapter.submitList(searchList)
            println("XXX SEARC FRAGMENT OBSERVING STATE: $searchList")
        })


        with(binding!!) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            recyclerView.adapter = adapter

            searchEditText.doAfterTextChanged { s ->  // kotlin abbreviated method
                    searchRunnable
                        ?.let {// 1. cancel the previous runnable to implement debouncing
                            handler.removeCallbacks(it)
                        }
                    searchRunnable =
                        Runnable { // 2. defines a new runnable that will perform the search
                            viewModel.updateSearch(s.toString()) // ###
                        }

                    searchRunnable
                        ?.let {// 3. schedule the new runnable with a specified delay to perform the debouncing
                            handler.postDelayed(it, DEBOUNCE_DELAY)
                        }
                }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragment(requireActivity(), movieId)
    }

}
