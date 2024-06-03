package com.example.muvitracker.ui.main.prefs

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
import com.example.muvitracker.databinding.FragmDetailBinding
import com.example.muvitracker.databinding.FragmPrefsBinding
import com.example.muvitracker.ui.main.Navigator


class PrefsFragment() : Fragment() {

    private var bindingBase: FragmPrefsBinding? = null
    private val binding
        get() = bindingBase

    private val viewModel by viewModels<PrefsViewModel>()
    private val navigator = Navigator()


    //    private val adapter = PrefsAdapter()
    private val adapter = PrefsListAdapter(
        onClickVH = { movieId ->
            startDetailsFragment(movieId)
        },
        onCLickLiked = { dto -> // check funziona
            viewModel.toggleFovoriteItem(dtoToToggle = dto)
            viewModel.updatePrefList()
        },
        onClickWatched = { dto -> // check funziona
            viewModel.updateWatchedItem(updatedDto = dto)
            viewModel.updatePrefList()
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragm_prefs, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.run {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            viewModel.preftList.observe(viewLifecycleOwner, Observer { list ->
                adapter.submitList(list)
            })
        }

    }


    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragment(
            requireActivity(),
            movieId
        )
    }

}


//            adapter.setCallbackVH { movieId ->
//                startDetailsFragment(movieId)
//            }

//            adapter.setCallbackLiked { dtoToToggle ->
//                viewModel.toggleFovoriteItem(dtoToToggle)
//                viewModel.updatePrefList()
//            }
//
//            adapter.setCallbackWatched { updatedDto ->
//                viewModel.updateWatchedItem(updatedDto)
//                viewModel.updatePrefList()
//            }
//        }
