//package com.example.muvitracker.inkotlin.ui.main.search.old
//
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.GridLayoutManager
//import com.example.muvitracker.databinding.FragmSearchBinding
//import com.example.muvitracker.inkotlin.data.dto.search.SearDto
//import com.example.muvitracker.inkotlin.ui.main.Navigator
//import com.example.muvitracker.inkotlin.ui.main.search.SearAdapter
//
//
//
//
//
//class SearFragment : Fragment(), SearContract.View {
//
//    // ATTRIBUTI
//    private val adapter = SearAdapter()
//    private val presenter = SearPresenter(this)
//    val navigator = Navigator()
//
//
//    private var bindingBase: FragmSearchBinding? = null
//
//
//    private val binding
//        get() = bindingBase
//
//    // Debouncing OK
//    val handler = Handler(Looper.getMainLooper()) // creazione Handler, classe android
//    var searchRunnable: Runnable? = null // con interfaccia stile java
//    val DEBOUNCE_DELAY: Long = 300L // Milliseconds
//
//
//    // METODI FRAGMENT
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // bindingBase.root == view
//        bindingBase = FragmSearchBinding.inflate(inflater, container, false)
//        return bindingBase?.root
//    }
//
//
//    override fun onViewCreated(
//        view: View,
//        savedInstanceState: Bundle?
//    ) {
//        with(binding!!) {
//            recycleView.layoutManager = GridLayoutManager(requireContext(), 3)
//            recycleView.adapter = adapter
//
//            /* Debouncing OK
//             * implemento callback al cambiamento stato stringa testo
//             * possibilità: prima, durante, dopo cambiamento */
//            searchEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged( // non usare
//                    s: CharSequence?, start: Int, count: Int, after: Int
//                ) {
//                }
//
//                override fun onTextChanged( // non usare
//                    s: CharSequence?, start: Int, before: Int, count: Int
//                ) {
//                }
//
//                // !!!! FUNZIONE DEBOUNCING OK
//                override fun afterTextChanged(s: Editable?) {
//                    // 1. annulla il runnable precedente per implementare il debouncing
//                    /* (java style)
//                    if (searchRunnable != null) {
//                        handler.removeCallbacks(searchRunnable!!)
//                    }
//                     */
//                    // (kotlin style)
//                    // permette di azionarlo solo se non nullo
//                    searchRunnable?.let {
//                        handler.removeCallbacks(it)
//                    }
//
//                    // 2. definisce un nuovo runnable che eseguirà la ricerca
//                    searchRunnable = Runnable {
//                        presenter.loadNetworkResult(s.toString()) // scrive
//                    }
//
//                    // 3. programma il nuovo runnable con un ritardo specificato per realizzare il debouncing
////                    handler.postDelayed(searchRunnable!!, DEBOUNCE_DELAY)
//                    searchRunnable?.let {
//                        handler.postDelayed(it, DEBOUNCE_DELAY)
//                    }
//                }
//
//            })
//
//        }
//
//        adapter.setCallbackVH { movieId ->
//            presenter.onVHolderClick(movieId)
//        }
//
//    }
//
//
//    // binding !!! - serve per evitare error memory leak
//    // se qualche attivita a vale rimane attiva, tipo chiamata internet
//    override fun onDestroyView() {
//        super.onDestroyView()
//        bindingBase = null
//    }
//
//
//    // CONTRACT METHODS
//    override fun updateUi(list: List<SearDto>) {
//        adapter.updateList(list)
//    }
//
//    override fun startDetailsFragment(traktMovieId: Int) {
//        navigator
//            .startDetailsFragment(
//                requireActivity(),
//                traktMovieId
//            )
//    }
//
//
//}