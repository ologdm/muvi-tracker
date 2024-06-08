package com.example.muvitracker.ui.main.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.databinding.FragmSearchBinding
import com.example.muvitracker.ui.main.Navigator

/*
 *  gestione visibilità tastiera:
 *    - da Manifest - "adjustNothing"
 *    - xml layout - android:imeOptions="actionDone" + inputType="text"
 */


class SearchFragment : Fragment() {

    private var _binding: FragmSearchBinding? = null
    private val binding
        get() = _binding

    val navigator = Navigator()
    private val viewModel by viewModels<SearchViewModel>()

    // Debouncing
    val handler = Handler(Looper.getMainLooper())
    var searchRunnable: Runnable? = null
    val DEBOUNCE_DELAY: Long = 300L

    private val adapter = SearchAdapter(onClickCallback = { movieId ->
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
        viewModel.searchList.observe(viewLifecycleOwner, Observer { searchList ->
            adapter.submitList(searchList)
        })


        with(binding!!) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            recyclerView.adapter = adapter

            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged( // non usare
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?, start: Int, before: Int, count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                    // 1. annulla il runnable precedente per implementare il debouncing
                    searchRunnable?.let {
                        handler.removeCallbacks(it)
                    }
                    // 2. definisce un nuovo runnable che eseguirà la ricerca
                    searchRunnable = Runnable {
                        viewModel.loadNetworkResult(s.toString())
                    }
                    // 3. programma il nuovo runnable con un ritardo specificato per realizzare il debouncing
                    searchRunnable?.let {
                        handler.postDelayed(it, DEBOUNCE_DELAY)
                    }
                }
            })
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