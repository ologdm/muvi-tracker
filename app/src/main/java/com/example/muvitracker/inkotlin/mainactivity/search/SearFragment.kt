package com.example.muvitracker.inkotlin.mainactivity.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.databinding.FragmSearBinding
import com.example.muvitracker.inkotlin.mainactivity.MainNavigator
import com.example.muvitracker.inkotlin.repo.dto.search.SearDto

/** funzioni generali:
 * 1. mostro movie o show type ( hanno stessi dati)
 * 2. apro details da adapter
 * 3. no db, mostro ogni volta solo la chiamata
 * 4. swipe to refresh
 * 5. view binding su adapter
 * 6. no empty states
 *
 * 8. chiamo funzione ricerca debouncing (o solo onclicklistener o)  OK
 * 9. ordinare risultato in base allo score OK, fare check
 *
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
    private var bindingBase: FragmSearBinding? = null

    // binding non cambia mai, quindi non c'e il rischio che varierraa
    private val binding
        get() = bindingBase // custom getter per bindingBase


    // Debouncing OK
    val handler = Handler(Looper.getMainLooper()) // creazione Handler, classe android
    var searchRunnable: Runnable? = null
    val DEBOUNCE_DELAY: Long = 300L // Milliseconds


    // METODI
    // crea
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // bindingBase.root == view
        bindingBase = FragmSearBinding.inflate(inflater, container, false)
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

            // Debouncing OK
            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) { // non usare
                }

                override fun onTextChanged(
                    s: CharSequence?, start: Int, before: Int, count: Int
                ) { // non usare
                }

                // fun per debouncing OK
                override fun afterTextChanged(s: Editable?) {
                    // 1 annulla il runnable precedente per implementare il debouncing
                    if (searchRunnable != null) {
                        handler.removeCallbacks(searchRunnable!!)
                    }
                    // 2 permette di azionarlo solo se non nullo
                    searchRunnable?.let {
                        handler.removeCallbacks(it)
                    }

                    // 3 definisce un nuovo runnable che eseguirà la ricercaù
                    searchRunnable = Runnable {
                        presenter.getNetworkResult(s.toString()) // scrive
                    }

                    // 4 programma il nuovo runnable con un ritardo specificato per realizzare il debouncing
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