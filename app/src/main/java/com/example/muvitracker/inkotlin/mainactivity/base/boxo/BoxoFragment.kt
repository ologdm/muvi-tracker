package com.example.muvitracker.inkotlin.mainactivity.base.boxo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmBaseCategoryBinding
import com.example.muvitracker.inkotlin.mainactivity.MainNavigator
import com.example.muvitracker.inkotlin.mainactivity.base.BaseAdapter
import com.example.muvitracker.inkotlin.mainactivity.base.BaseContract
import com.example.muvitracker.inkotlin.model.MovieModel
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum
import com.example.muvitracker.myappunti.kotlin.EmptyStatesManagement

class BoxoFragment : Fragment(), BaseContract.View {

    private lateinit var presenter: BaseContract.Presenter
    private val adapter = BaseAdapter()
    private val navigator = MainNavigator()
    private var binding: FragmBaseCategoryBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmBaseCategoryBinding.inflate(inflater,container,false)
        return binding?.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        presenter = BoxoPresenter(this, requireContext())

        with(binding!!) {

            toolbar.text = getString(R.string.box_office)

            recycleView.adapter = adapter
            recycleView.layoutManager = GridLayoutManager(
                requireContext(),
                2
            )

            swipeToRefresh.setOnRefreshListener {
                presenter.getMovieAndUpdateUi(forceRefresh = true)
            }
        }

        adapter.setCallbackVH { movieId ->
            presenter.onVHolderClick(movieId)
        }

        presenter.getMovieAndUpdateUi(forceRefresh = false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    // CONTRACTS METHODS OK
    override fun updateUi(list: List<MovieModel>) {
        adapter.updateList(list)
    }


    override fun emptyStatesFlow(emptyStates: EmptyStatesEnum) {
        EmptyStatesManagement.emptyStatesFlow(
            emptyStates,
            binding?.emptyStates!!.progressBar,
            binding?.emptyStates!!.errorMsgTextview
        )
        when (emptyStates) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
            -> binding?.swipeToRefresh?.isRefreshing = false
            else -> {}
        }
    }


    // ALTRO
    override fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
        println("XXX_ START FRAGMENT FROM BOXO, movieId: $movieId")
    }

}