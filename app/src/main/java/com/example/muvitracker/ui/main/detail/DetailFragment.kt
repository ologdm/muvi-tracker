package com.example.muvitracker.ui.main.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmDetailBinding
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.dateFormatterInMMMyyy
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.example.muvitracker.utils.statesFlow
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var currentMovieId: Int = 0
    private var _binding: FragmDetailBinding? = null
    private val binding
        get() = _binding

    private val viewModel by viewModels<DetailViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmDetailBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        if (bundle != null) {
            currentMovieId = bundle.getInt(TRAKT_ID_KEY)
        }

        viewModel.getStateContainer(movieId = currentMovieId)
            .observe(viewLifecycleOwner) { stateContainer ->
                stateContainer.data?.let { detailMovie ->
                    updateUi(detailMovie)
                }
                stateContainer.statesFlow(
                    binding!!.progressBar,
                    binding!!.errorTextView,
                )
            }

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
    private fun updateUi(detailmovie: DetailMovie) {
        with(binding!!) {
            val ratingApproximation = detailmovie.rating.firstDecimalApproxToString()

            title.text = detailmovie.title
            released.text = detailmovie.released.dateFormatterInMMMyyy() // conversion
            runtime.text =
                getString(R.string.runtime_description, detailmovie.runtime.toString())  // string
            country.text = detailmovie.country
            rating.text =
                getString(R.string.rating_description, ratingApproximation) // conversion + string
            overview.text = detailmovie.overview

            Glide.with(requireContext())
                .load(detailmovie.imageUrl())
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(imageVertical)
            Glide.with(requireContext())
                .load(detailmovie.imageUrl())
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(imageHorizontal)

            chipGroup.removeAllViews() // clean old
            detailmovie.genres.forEach {
                val chip = Chip(context).apply {
                    text = it
                }
                chipGroup.addView(chip)
            }
        }

        updateFavoriteIcon(detailmovie.liked)
        updateWatchedCheckbox(detailmovie.watched)
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
        const val TRAKT_ID_KEY = "traktId_key"

        fun create(traktId: Int): DetailFragment {
            val detailFragment = DetailFragment()
            val bundle = Bundle()
            bundle.putInt(TRAKT_ID_KEY, traktId)
            detailFragment.arguments = bundle
            return detailFragment
        }
    }

}














