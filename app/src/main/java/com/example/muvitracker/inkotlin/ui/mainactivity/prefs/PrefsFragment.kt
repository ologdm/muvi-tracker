package com.example.muvitracker.inkotlin.ui.mainactivity.prefs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.data.dto.DetaDto
import com.example.muvitracker.inkotlin.ui.mainactivity.MainNavigator


class PrefsFragment() : Fragment(), PrefsContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var presenter: PrefsContract.Presenter
    private val adapter = PrefsAdapter()
    private val navigator = MainNavigator()


    // FRAGMENT METHODS
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragm_prefs, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        presenter = PrefsPresenter(this, requireContext())

        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.setCallbackVH { movieId ->
            presenter.onVHolderClick(movieId)
            // > poi startDetails()
        }

        adapter.setCallbackLiked { dtoToToggle ->
            presenter.toggleFovoriteItem(dtoToToggle)
            presenter.loadPrefsListAndUpdateUi()
        }

        adapter.setCallbackWatched { updatedDto ->
            presenter.updateWatchedItem(updatedDto)
            presenter.loadPrefsListAndUpdateUi()
        }

        // GET default OK
        presenter.loadPrefsListAndUpdateUi()
    }


    // CONTRACT
    override fun updateUi(list: List<DetaDto>) {
        adapter.updateList(list)
    }

    override fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragmentAndAddToBackstack(
            requireActivity(),
            movieId
        )
    }

}