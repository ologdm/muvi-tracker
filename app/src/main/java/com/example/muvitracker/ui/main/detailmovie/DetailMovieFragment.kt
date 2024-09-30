package com.example.muvitracker.ui.main.detailmovie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.databinding.FragmDetailMovieBinding
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.detailshow.adapters.RelatedShowsAdapter
import com.example.muvitracker.utils.statesFlow
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragm_detail_movie) {

    private var currentMovieIds: Ids = Ids()

    private val viewModel by viewModels<DetailMovieViewmodel>()
    private val binding by viewBinding(FragmDetailMovieBinding::bind)

    @Inject
    lateinit var navigator: Navigator

    private val relatedMovieAdapter = RelatedMovieAdapter(onClickVH = { ids ->
        navigator.startMovieDetailFragment(ids)
    })


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
            watchedCheckbox.setOnCheckedChangeListener { _, isChecked ->
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


        // RELATED MOVIES
        binding.relatedMoviesRV.adapter = relatedMovieAdapter
        binding.relatedMoviesRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.loadRelatedMovies(currentMovieIds.trakt)
        viewModel.relatedMoviesStatus.observe(viewLifecycleOwner) { relatedMovies ->
            relatedMovieAdapter.submitList(relatedMovies)
        }
    }


    // ###################################################################
    private fun updateUi(detailMovie: DetailMovie) {
        with(binding) {
            title.text = detailMovie.title
            releasedYearAndCountry.text =
                "${detailMovie.released} (${detailMovie.country.uppercase()})"
            status.text = detailMovie.status.replaceFirstChar { it.uppercaseChar() } // es released
            runtime.text =
                getString(R.string.runtime_description, detailMovie.runtime.toString())  // string
            traktRating.text = detailMovie.rating // (string, already converted)
            tagline.text = detailMovie.tagline
            overview.text = detailMovie.overview

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
        binding.watchedCheckbox.setOnCheckedChangeListener(null) // ok
        binding.watchedCheckbox.isChecked = isWatched
        binding.watchedCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateWatched(currentMovieIds.trakt, isChecked)
        }
    }

    companion object {
        fun create(movieIds: Ids): DetailMovieFragment {
            val detailMovieFragment = DetailMovieFragment()
            val bundle = Bundle()
            bundle.putParcelable(MOVIE_IDS_KEY, movieIds)
            detailMovieFragment.arguments = bundle
            return detailMovieFragment
        }

        private const val MOVIE_IDS_KEY = "movieIdsKey"
    }

}
