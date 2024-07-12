package com.example.muvitracker.ui.main.prefs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muvitracker.databinding.FragmPrefsBinding
import com.example.muvitracker.ui.main.Navigator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PrefsFragment : Fragment() {

    private var _binding: FragmPrefsBinding? = null
    private val binding
        get() = _binding

    private val viewModel by viewModels<PrefsViewModel>()

    @Inject
    lateinit var navigator: Navigator

    private val adapter = PrefsAdapter(
        onClickVH = { movieId ->
            startDetailsFragment(movieId)
        },
        onLongClickVH = { movieId ->
            startDeleteAlertDialog(movieId)
        },
        onCLickLiked = { item ->
            viewModel.toggleFavoriteItem(itemToToggle = item)
        },
        onClickWatched = { item, watched ->
            viewModel.updateWatchedItem(updatedItem = item, watched)
        }
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmPrefsBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.adapter = adapter
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        viewModel.prefsList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // #######################################################################################

    private fun startDetailsFragment(movieId: Int) {
        navigator.startDetailsFragment(
            movieId
        )
    }


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

