package com.example.muvitracker.inkotlin.mainactivity.prefs.mvvm_test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.mainactivity.MainNavigator
import com.example.muvitracker.inkotlin.mainactivity.details.mvvm_test.DetaViewModel
import com.example.muvitracker.inkotlin.mainactivity.prefs.PrefsAdapter
import com.example.muvitracker.inkotlin.model.dto.DetaDto
import com.example.muvitracker.inkotlin.utils.DetaVMFactory
import com.example.muvitracker.inkotlin.utils.PrefsVMFactory


class PrefsFragmentVM() : Fragment() {


    // ATTRIBUTI

    private lateinit var recyclerView: RecyclerView
    private val adapter = PrefsAdapter()
    private val navigator = MainNavigator()

//    private lateinit var presenter: PrefsContract.Presenter
    private lateinit var viewModel: PrefsViewModel


    // FRAGMENT METHODS

    // creazione
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragm_prefs, container, false)
    }


    // logica
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // ok


//        val presenter = PrefsPresenter(this, requireContext())
        viewModel = ViewModelProvider(
            this,
            PrefsVMFactory(requireContext())
        ).get(PrefsViewModel::class.java)


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
            viewModel.getPrefsList()
        }



        adapter.setCallbackWatched { updatedDto ->
//            presenter.updateWatchedItem(updatedDto)
//            presenter.getPrefsListAndUpdateUi()
            viewModel.updateWatchedItem(updatedDto)
            viewModel.getPrefsList()
        }


        // GET default OK
//        presenter.getPrefsListAndUpdateUi()
        viewModel.getPrefsList()


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