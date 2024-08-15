package com.example.muvitracker.ui.main.detailmovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmDetailMovieBinding
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.dateFormatterInMMMyyy
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.example.muvitracker.utils.statesFlow
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMovieFragment private constructor() : Fragment() {

    private var currentMovieId: Int = 0
    private var _binding: FragmDetailMovieBinding? = null
    private val binding
        get() = _binding

    private val viewModel by viewModels<DetailMovieViewmodel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmDetailMovieBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        if (bundle != null) {
            currentMovieId = bundle.getInt(TRAKT_ID_KEY)
        }

        viewModel.state.observe(viewLifecycleOwner) { stateContainer ->
            stateContainer.data?.let { detailMovie ->
                updateUi(detailMovie)
            }
            stateContainer.statesFlow(
                binding!!.errorTextView,
                binding!!.progressBar
            )
        }

        viewModel.getStateContainer(currentMovieId)

        with(binding!!) {
            buttonBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            floatingLikedButton.setOnClickListener {
                viewModel.toggleFavorite(currentMovieId)
            }
            watchedCkbox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateWatched(currentMovieId, isChecked)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // ###################################################################
    private fun updateUi(detailMovie: DetailMovie) {
        with(binding!!) {
            val ratingApproximation = detailMovie.rating.firstDecimalApproxToString()

            title.text = detailMovie.title
            released.text = detailMovie.released.dateFormatterInMMMyyy() // conversion
            runtime.text =
                getString(R.string.runtime_description, detailMovie.runtime.toString())  // string
            country.text = detailMovie.country
            rating.text =
                getString(R.string.rating_description, ratingApproximation) // conversion + string
            overview.text = detailMovie.overview

            Glide.with(requireContext())
                .load(detailMovie.imageUrl())
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(imageVertical)
            Glide.with(requireContext())
                .load(detailMovie.imageUrl())
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .placeholder(R.drawable.glide_placeholder_base)
                .into(imageHorizontal)

            chipGroup.removeAllViews() // clean old
            detailMovie.genres.forEach {
                val chip = Chip(context).apply {
                    text = it
                }
                chipGroup.addView(chip)
            }
        }

        updateFavoriteIcon(detailMovie.liked)
        updateWatchedCheckbox(detailMovie.watched)
    }


    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.baseline_liked)
        val iconEmpty = context?.getDrawable(R.drawable.baseline_liked_border)

        binding?.floatingLikedButton?.setImageDrawable(if (isFavorite) iconFilled else iconEmpty)
    }

    private fun updateWatchedCheckbox(isWatched: Boolean) {
        binding?.watchedCkbox?.setOnCheckedChangeListener(null) // ok
        binding?.watchedCkbox?.isChecked = isWatched // ok
        binding?.watchedCkbox?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateWatched(currentMovieId, isChecked)
        }
    }


    companion object {
        private const val TRAKT_ID_KEY = "traktIdKey"

        fun create(traktId: Int): DetailMovieFragment {
            val detailMovieFragment = DetailMovieFragment()
            val bundle = Bundle()
            bundle.putInt(TRAKT_ID_KEY, traktId)
            detailMovieFragment.arguments = bundle
            return detailMovieFragment
        }
    }

}














