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
import com.example.muvitracker.inkotlin.utils.BoxoVMFactory
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum
import com.example.muvitracker.myappunti.kotlin.EmptyStatesManagement

class BoxoFragmentVM : Fragment() {

    private val adapter = BaseAdapter()
    private val navigator = MainNavigator()
    private var binding: FragmBaseCategoryBinding? = null

    private lateinit var boxoViewModel: BoxoViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmBaseCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        // factory semplificato
        boxoViewModel = ViewModelProvider(this, BoxoVMFactory(requireContext())).get(BoxoViewModel::class.java)

        // aggiorno ogni volta che cambia stato su VModel
        boxoViewModel.boxoList.observe(viewLifecycleOwner, Observer {
            adapter.updateList(it)
        })

        boxoViewModel.emptyState.observe(viewLifecycleOwner, Observer {
            handleEmptyStates(it)
        })



        with(binding!!) {

            toolbar.text = getString(R.string.box_office)

            recycleView.adapter = adapter
            recycleView.layoutManager = GridLayoutManager(requireContext(), 2)

            swipeToRefresh.setOnRefreshListener {
                boxoViewModel.getMovies(isRefresh = true)
            }
        }

        adapter.setCallbackVH { movieId ->
            startDetailsFragment(movieId)
        }

        boxoViewModel.getMovies(isRefresh = false)
    }


    private fun handleEmptyStates(emptyStates: EmptyStatesEnum) {
        EmptyStatesManagement.emptyStatesFlow(
            emptyStates,
            binding!!.progressBar,
            binding!!.errorMsgTextview
        )
        when (emptyStates) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
            -> binding?.swipeToRefresh?.isRefreshing = false

            else -> {}
        }
    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
        println("XXX_ START FRAGMENT FROM BOXO, movieId: $movieId")
    }

}