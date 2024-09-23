package com.example.muvitracker.ui.main.prefs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmPrefsBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.prefs.adapter.PrefsMovieAdapter
import com.example.muvitracker.ui.main.prefs.adapter.PrefsShowAdapter
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PrefsShowFragmentTODO : Fragment(R.layout.fragm_prefs) {

    private val viewModel by viewModels<PrefsShowViewModelTODO>()
    private val binding by viewBinding (FragmPrefsBinding::bind)

    @Inject
    lateinit var navigator: Navigator

    private val adapter = PrefsShowAdapter(
        onClickVH = { showIds ->
            navigator.startShowDetailFragment(showIds)
        },
        onLongClickVH = { movieId ->
            startDeleteAlertDialog(movieId)
        },
        onCLickLiked = { movieId ->
            viewModel.togglelikedShow(movieId)
        },
        onClickWatched = { item, watched ->
//            viewModel.updateWatchedItem(updatedItem = item, watched)
        }
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.prefsList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }



    // #######################################################################################
    private fun startDeleteAlertDialog(movieId: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Discard movie")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteItem(movieId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}

