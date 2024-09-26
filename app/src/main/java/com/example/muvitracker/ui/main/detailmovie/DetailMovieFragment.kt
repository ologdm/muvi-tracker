package com.example.muvitracker.ui.main.detailmovie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.databinding.FragmDetailMovieBinding
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.utils.dateFormatterInMMMyyy
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.example.muvitracker.utils.statesFlow
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragm_detail_movie) {

    private var currentMovieIds: Ids = Ids()

    private val viewModel by viewModels<DetailMovieViewmodel>()
    private val binding by viewBinding(FragmDetailMovieBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        if (bundle != null) {
            currentMovieIds = bundle.getParcelable(MOVIE_IDS_KEY) ?: Ids()
        }

        // MOVIE
        viewModel.getStateContainer(currentMovieIds.trakt)
        viewModel.detailState.observe(viewLifecycleOwner) { stateContainer ->
            stateContainer.data?.let { detailMovie ->
                updateUi(detailMovie)
            }
            stateContainer.statesFlow(
                binding.errorTextView,
                binding.progressBar
            )
        }


        // BUTTONS
        with(binding) {
            buttonBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            floatingLikedButton.setOnClickListener {
                viewModel.toggleFavorite(currentMovieIds.trakt)
            }
            watchedCkbox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateWatched(currentMovieIds.trakt, isChecked)
            }
        }

        // IMAGES - TODO test
        viewModel.loadImageMovieTest(currentMovieIds.tmdb)

        viewModel.backdropImageUrl.observe(viewLifecycleOwner) { backdropUrl ->
            Glide.with(requireContext())
                .load(backdropUrl) // 1399 game-of-thrones
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(binding.imageHorizontal)
            println("XXX detail test backdrop: $backdropUrl")
        }

        // vertical - poster
        viewModel.posterImageUrl.observe(viewLifecycleOwner) { posterUrl ->
            Glide.with(requireContext())
                .load(posterUrl)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(binding.imageVertical)
            println("XXX detail test poster: $posterUrl")
        }



        // overview expand - non fare





    }


    // ###################################################################
    private fun updateUi(detailMovie: DetailMovie) {
        with(binding) {
            val ratingApproximation = detailMovie.rating.firstDecimalApproxToString()

            title.text = detailMovie.title
            released.text = detailMovie.released.dateFormatterInMMMyyy() // conversion
            runtime.text =
                getString(R.string.runtime_description, detailMovie.runtime.toString())  // string
            country.text = detailMovie.country
            rating.text =
                getString(R.string.rating_description, ratingApproximation) // conversion + string
            overview.text = detailMovie.overview

            // old
//            Glide.with(requireContext())
//                .load(detailMovie.imageUrl())
//                .transition(DrawableTransitionOptions.withCrossFade(500))
//                .placeholder(R.drawable.glide_placeholder_base)
//                .error(R.drawable.glide_placeholder_base)
//                .into(imageVertical)
//            Glide.with(requireContext())
//                .load(detailMovie.imageUrl())
//                .transition(DrawableTransitionOptions.withCrossFade(500))
//                .placeholder(R.drawable.glide_placeholder_base)
//                .into(imageHorizontal)

            genresChipGroup.removeAllViews() // clean old
            detailMovie.genres.forEach {
                val chip = Chip(context).apply {
                    text = it
                }
                genresChipGroup.addView(chip)
            }
        }

        updateFavoriteIcon(detailMovie.liked)
        updateWatchedCheckbox(detailMovie.watched)
    }


    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.baseline_liked)
        val iconEmpty = context?.getDrawable(R.drawable.baseline_liked_border)

        binding.floatingLikedButton.setImageDrawable(if (isFavorite) iconFilled else iconEmpty)
    }

    private fun updateWatchedCheckbox(isWatched: Boolean) {
        binding.watchedCkbox.setOnCheckedChangeListener(null) // ok
        binding.watchedCkbox.isChecked = isWatched // ok
        binding.watchedCkbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateWatched(currentMovieIds.trakt, isChecked)
        }
    }

    companion object {
        fun create(movieIds: Ids): DetailMovieFragment {
            val detailMovieFragment = DetailMovieFragment()
            val bundle = Bundle()
            bundle.putParcelable(MOVIE_IDS_KEY,movieIds)
            detailMovieFragment.arguments = bundle
            return detailMovieFragment
        }

        private const val MOVIE_IDS_KEY = "movieIdsKey"
    }

}