package com.example.muvitracker.inkotlin.mainactivity.base.boxo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.mainactivity.MainNavigator
import com.example.muvitracker.inkotlin.repo.dto.base.BoxoDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum
import com.example.muvitracker.myappunti.kotlin.EmptyStatesManagement



class BoxoFragment : Fragment(), BoxoContract.View {

    // dichiaro OK
    private lateinit var recyclerView: RecyclerView

    private lateinit var progressBar: ProgressBar
    private lateinit var errorMsgTextview: TextView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var presenter: BoxoContract.Presenter // OK

    // inizializzo
    private val adapter = BoxoAdapter()
    private val navigator = MainNavigator()



    // crea OK
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragm_boxo, container, false)
    }


    // logica
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState) // i paramentri gli passo al costruttore

        val presenter = BoxoPresenter(this, requireContext())

        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        progressBar = view.findViewById(R.id.progressBar)
        errorMsgTextview = view.findViewById(R.id.errorMsgTextview)
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)

        swipeRefreshLayout.setOnRefreshListener { // ()->Unit, interfaccia parametro vuoto
            // implementazione OnRefreshListener
            presenter.getMovieAndUpdateUi(forceRefresh = true)

        }

        adapter.setcallbackVH { movieId ->
            presenter.onVHolderClick(movieId)
        }

        presenter.getMovieAndUpdateUi(forceRefresh = false)
    }


    // CONTRACTS METHODS OK nuovo tutto

    override fun updateUi(list: List<BoxoDto>) {
        adapter.updateList(list)
    }


    override fun emptyStatesFlow(emptyStates: EmptyStatesEnum) {

        EmptyStatesManagement.emptyStatesFlow(
            emptyStates,
            progressBar,
            errorMsgTextview
        )

        // stop refreshing solo in questi 3 stati
        when (emptyStates) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
            -> swipeRefreshLayout.isRefreshing = false

            else -> {}  // non fare nulla
        }
    }


    // OK nuovo
    override fun startDetailsFragment(movieId: Int) {

        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
        println("XXX_ START FRAGMENT FROM BOXO, movieId: $movieId")
    }

}