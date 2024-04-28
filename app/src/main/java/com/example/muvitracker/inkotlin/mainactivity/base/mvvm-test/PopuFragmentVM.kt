package com.example.muvitracker.inkotlin.mainactivity.base.`mvvm-test`

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmBaseCategoryBinding
import com.example.muvitracker.inkotlin.mainactivity.MainNavigator
import com.example.muvitracker.inkotlin.mainactivity.base.BaseAdapter
import com.example.muvitracker.inkotlin.utils.PopuVMFactory
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum
import com.example.muvitracker.myappunti.kotlin.EmptyStatesManagement


class PopuFragmentVM : Fragment() {

    private val adapter = BaseAdapter()
    private val navigator = MainNavigator()
    private var binding: FragmBaseCategoryBinding? = null

    // Vmodel OK
    lateinit var popuViewModel: PopuViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmBaseCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }


    // 2.2 logica
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        // Vmodel OK
        //popuViewModel = ViewModelProvider(this).get(PopuViewModel::class.java)
        popuViewModel =ViewModelProvider(this, PopuVMFactory(requireContext())).get(PopuViewModel::class.java)

        // Vmodel OK
        popuViewModel.popuList.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.updateList(it) }
        })

        popuViewModel.emptyState.observe(viewLifecycleOwner, Observer {
            it?.let { handleEmptyStates(it) }
        })


        with(binding!!) {

            toolbar.text = getString(R.string.popular)
            recycleView.adapter = adapter
            recycleView.layoutManager = GridLayoutManager(requireContext(), 2)

            // Vmodel OK
            // presenter.getMovieAndUpdateUi(forceRefresh = false)
            popuViewModel.getMovie(isRefresh = false)

            // Vmodel OK
            swipeToRefresh.setOnRefreshListener {
                // presenter.getMovieAndUpdateUi(forceRefresh = true)
                popuViewModel.getMovie(isRefresh = true)
            }
        }


        // Vmodel OK
        adapter.setCallbackVH { movieId ->
            // presenter.onVHolderClick(movieId)
            // senza passaggio intermedio da presenter
            startDetailsFragment(movieId)
        }

    }


    // FUNZIONI CONTRACT -> PRIVATE
    // non sono piu da chiamare da presenter,
    // ma direttamente da view
    // con observer che aggiorna solamente stato loro parametri

    // fun updateUi(popuList) - non piu necessaria


    private fun handleEmptyStates(emptyStates: EmptyStatesEnum) {
        EmptyStatesManagement(requireContext()).emptyStatesFlow(
            emptyStates,
            binding!!.progressBar,
            binding!!.errorMsgTextview
        )

        // stop refreshing solo in questi 3 stati
        when (emptyStates) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
            -> binding?.swipeToRefresh?.isRefreshing = false

            else -> {}  // non fare nulla
        }
    }


    // OK
    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
        println("XXX_ START FRAGMENT FROM POPU, movieId: $movieId")
    }
}



