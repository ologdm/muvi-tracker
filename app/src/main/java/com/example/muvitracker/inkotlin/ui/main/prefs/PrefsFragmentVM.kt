package com.example.muvitracker.inkotlin.ui.main.prefs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.ui.main.Navigator


class PrefsFragmentVM() : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = PrefsAdapter()
    private val navigator = Navigator()

//    private lateinit var presenter: PrefsContract.Presenter
    private val viewModel by viewModels<PrefsViewModel>()


    // FRAGMENT METHODS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragm_prefs, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // ok


//        val presenter = PrefsPresenter(this, requireContext())


        viewModel.preftList.observe(viewLifecycleOwner, Observer {
            adapter.updateList(it)
        })


        adapter.setCallbackVH { movieId ->
//            presenter.onVHolderClick(movieId)
            startDetailsFragment(movieId)
        }



        adapter.setCallbackLiked { dtoToToggle ->
//            presenter.toggleFovoriteItem(dtoToToggle)
//            presenter.getPrefsListAndUpdateUi()
            viewModel.toggleFovoriteItem(dtoToToggle)
            viewModel.updatePrefList()
        }



        adapter.setCallbackWatched { updatedDto ->
//            presenter.updateWatchedItem(updatedDto)
//            presenter.getPrefsListAndUpdateUi()
            viewModel.updateWatchedItem(updatedDto)
            viewModel.updatePrefList()
        }


        // GET default OK
//        presenter.getPrefsListAndUpdateUi()
        viewModel.updatePrefList()


    }


    // CONTRACT -> PRIVATE

//    // eliminare
//    private fun updateUi(list: List<DetaDto>) {
//        adapter.updateList(list)
//    }


    // OK
    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
    }


}