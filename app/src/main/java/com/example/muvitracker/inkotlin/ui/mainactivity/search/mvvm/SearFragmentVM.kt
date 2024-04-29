package com.example.muvitracker.inkotlin.ui.mainactivity.search.mvvm

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.databinding.FragmSearchBinding
import com.example.muvitracker.inkotlin.ui.mainactivity.MainNavigator
import com.example.muvitracker.inkotlin.ui.mainactivity.search.SearAdapter


class SearFragmentVM : Fragment() {

    // ATTRIBUTI
    private val adapter = SearAdapter()
    val navigator = MainNavigator()

    private var bindingBase: FragmSearchBinding? = null

    private val binding
        get() = bindingBase

    // Debouncing
    val handler = Handler(Looper.getMainLooper()) // creazione Handler, classe android
    var searchRunnable: Runnable? = null // con interfaccia stile java
    val DEBOUNCE_DELAY: Long = 300L // Milliseconds

    // !!!!! creazione automatica
    private val viewModel by viewModels<SearViewModel>()


    // METODI

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // bindingBase.root == view
        bindingBase = FragmSearchBinding.inflate(inflater, container, false)
        return bindingBase?.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        viewModel.searchList.observe(viewLifecycleOwner, Observer {
            adapter.updateList(it)
        })


        with(binding!!) {
            recycleView.layoutManager = GridLayoutManager(requireContext(), 3)
            recycleView.adapter = adapter

            /* Debouncing OK
             * implemento callback al cambiamento stato stringa testo
             * possibilità: prima, durante, dopo cambiamento */
            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged( // non usare
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged( // non usare
                    s: CharSequence?, start: Int, before: Int, count: Int
                ) {
                }

                // fun per Debouncing OK
                override fun afterTextChanged(s: Editable?) {
                    // 1. permette di azionarlo solo se non nullo
                    searchRunnable?.let {
                        handler.removeCallbacks(it)
                    }

                    // 2. definisce un nuovo runnable che eseguirà la ricerca
                    searchRunnable = Runnable {
//                        presenter.getNetworkResult(s.toString()) // scrive
                        viewModel.loadNetworkResult(s.toString())
                    }

                    // 3. programma il nuovo runnable con un ritardo specificato per realizzare il debouncing
                    searchRunnable?.let {
                        handler.postDelayed(it, DEBOUNCE_DELAY)
                    }
                }
            })
        }

        adapter.setCallbackVH { movieId ->
//            presenter.onVHolderClick(movieId)
            startDetailsFragment(movieId)
        }
    }


    // binding !!! - serve per evitare error memory leak
    // se qualche attivita a valle rimane attiva, tipo chiamata internet
    override fun onDestroyView() {
        super.onDestroyView()
        bindingBase = null
    }


    // PRIVATE METHODS
//    // non serve
//    override fun updateUi(list: List<SearDto>) {
//        adapter.updateList(list)
//    }

    private fun startDetailsFragment(traktMovieId: Int) {
        navigator
            .startDetailsFragmentAndAddToBackstack(
                requireActivity(),
                traktMovieId
            )
    }

}