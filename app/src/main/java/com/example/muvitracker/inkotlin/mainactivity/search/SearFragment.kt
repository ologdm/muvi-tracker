package com.example.muvitracker.inkotlin.mainactivity.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.databinding.FragmSearchBinding
import com.example.muvitracker.inkotlin.mainactivity.MainNavigator
import com.example.muvitracker.inkotlin.model.dto.search.SearDto


/**
 * funzioni generali:
 *  1. mostro solo movie ( hanno stessi dati), TODO show type ecc
 *  2. apro details da adapter
 *  3. db NO, mostro ogni volta solo la chiamata
 *  4. swipe to refresh NO
 *  5. view binding su adapter
 *  6. empty states NO
 *
 *  7. chiamo funzione ricerca debouncing   OK
 *  8. ordinare risultato in base allo score OK
 *
 *  9. gestione visibilità tastiera:
 *        - da Manifest - "adjustNothing"
 *        - xml layout - android:imeOptions="actionDone" + inputType="text"
 *
 */


class SearFragment : Fragment(), SearContract.View {

    // ATTRIBUTI
    private val adapter = SearAdapter()
    private val presenter = SearPresenter(this)
    val navigator = MainNavigator()


    /* !!!!! binding OK
     * modo furbo per incapsulare una var in una val , ma che la seconda rifletta sempre la variazione della prima
     * ( private val binding = _binding )  ==> cosi invece il val una volta fissato non cambia al cambiare di var
     * custom getter , fa riferimento al var
     */

    private var bindingBase: FragmSearchBinding? = null

    // binding non cambia mai, quindi non c'e il rischio che varierà
    private val binding
        get() = bindingBase // custom getter per bindingBase


    // Debouncing OK
    val handler = Handler(Looper.getMainLooper()) // creazione Handler, classe android
    var searchRunnable: Runnable? = null // con interfaccia stile java
    val DEBOUNCE_DELAY: Long = 300L // Milliseconds


    // METODI
    // crea
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // bindingBase.root == view
        bindingBase = FragmSearchBinding.inflate(inflater, container, false)
        return bindingBase?.root
    }

    // logica
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // (!!) va bene ???
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
                    // 1. annulla il runnable precedente per implementare il debouncing
                    /* (java style)
                    if (searchRunnable != null) {
                        handler.removeCallbacks(searchRunnable!!)
                    }
                     */

                    // (kotlin style)
                    // permette di azionarlo solo se non nullo
                    searchRunnable?.let {
                        handler.removeCallbacks(it)
                    }

                    // 2. definisce un nuovo runnable che eseguirà la ricerca
                    searchRunnable = Runnable {
                        presenter.getNetworkResult(s.toString()) // scrive
                    }

                    // 3. programma il nuovo runnable con un ritardo specificato per realizzare il debouncing
                    handler.postDelayed(searchRunnable!!, DEBOUNCE_DELAY)
                }
            })

        }

        adapter.setCallbackVH { movieId ->
            presenter.onVHolderClick(movieId)
        }

    }


    // binding !!! - serve per evitare error memory leak
    // se qualche attivita a vale rimane attiva, tipo chiamata internet
    override fun onDestroyView() {
        super.onDestroyView()
        bindingBase = null
    }


    // CONTRACT METHODS
    override fun updateUi(list: List<SearDto>) {
        adapter.updateList(list)
    }

    override fun startDetailsFragment(traktMovieId: Int) {
        navigator
            .startDetailsFragmentAndAddToBackstack(
                requireActivity(),
                traktMovieId
            )
    }


}