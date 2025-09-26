package com.example.muvitracker.ui.main.detailmovie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.FragmDetailMovieBinding
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.person.adapters.CastAdapter
import com.example.muvitracker.ui.main.detailmovie.adapters.RelatedMovieAdapter
import com.example.muvitracker.utils.formatToReadableDate
import com.example.muvitracker.utils.getNowFormattedDateTime
import com.example.muvitracker.utils.statesFlow1
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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


    private val castMovieAdapter = CastAdapter(onClickVH = { ids, character ->
        navigator.startPersonFragmentFromCast(ids, character)
    })


    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        val bundle = arguments
        if (bundle != null) {
            currentMovieIds = bundle.getParcelable(MOVIE_IDS_KEY) ?: Ids()
        }

        // MOVIE DETAIL
        viewModel.loadMovieDetailFlow(currentMovieIds.trakt)

        viewModel.detailState.observe(viewLifecycleOwner) { stateContainer ->
            // 1
            stateContainer.data?.let { detailMovie ->
                updateDetailUi(detailMovie)
                updateFavoriteIcon(detailMovie.liked)
                updateWatchedCheckbox(detailMovie)
            }
            // 2
            stateContainer.statesFlow1(
                binding.errorTextView,
                binding.progressBar
            )
        }


        // BUTTONS CLICK
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.floatingLikedButton.setOnClickListener {
            viewModel.toggleLikedMovie(currentMovieIds.trakt)
        }

        binding.watchedCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateWatched(currentMovieIds.trakt, isChecked)
        }


        loadTMDBImagesWithCustomGlide()


        // RELATED MOVIES
        binding.relatedMoviesRV.adapter = relatedMovieAdapter
        binding.relatedMoviesRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.loadRelatedMovies(currentMovieIds.trakt)
        viewModel.relatedMoviesStatus.observe(viewLifecycleOwner) { relatedMovies ->
            relatedMovieAdapter.submitList(relatedMovies)
        }


        // CAST
        binding.castRV.adapter = castMovieAdapter
        binding.castRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.loadCast(currentMovieIds.trakt)
        viewModel.castState.observe(viewLifecycleOwner) {
            castMovieAdapter.submitList(it.castMembers)
            println("XXX ${it.castMembers}")
        }
    }


    // PRIVATE FUNCTIONS ###############################################
    // movie detail
    private fun updateDetailUi(detailMovie: DetailMovie) {
        with(binding) {
            title.text = detailMovie.title
            val yearString = detailMovie.released?.formatToReadableDate() ?: "N/A"
            releasedYearAndCountry.text =
                "${yearString} (${detailMovie.country.uppercase()})"
            status.text = detailMovie.status.replaceFirstChar { it.uppercaseChar() } // es released
            runtime.text =
                getString(R.string.runtime_description, detailMovie.runtime.toString())  // string

            traktRating.text = detailMovie.rating // (string, already converted)
            tagline.text = detailMovie.tagline
            overview.text = detailMovie.overview

            // genres
            genresChipGroup.removeAllViews() // clean old
            detailMovie.genres.forEach { genre ->
                val chip = Chip(context).apply { text = genre }
                genresChipGroup.addView(chip)
            }

            // open link on youtube
            val trailerUrl = detailMovie.trailer
            trailerImageButton.setOnClickListener {
                if (!trailerUrl.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Nessun trailer disponibile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.liked_icon_filled)
        val iconEmpty = context?.getDrawable(R.drawable.liked_icon_empty)
        binding.floatingLikedButton.setImageDrawable(if (isFavorite) iconFilled else iconEmpty)
    }

    //    private fun updateWatchedCheckbox(isWatched: Boolean) {
    private fun updateWatchedCheckbox(movie: DetailMovie) {
        binding.watchedCheckbox.setOnCheckedChangeListener(null)
        binding.watchedCheckbox.isChecked = movie.watched

        val isDisabled = movie.released != null && movie.released > getNowFormattedDateTime()
        binding.watchedCheckbox.isEnabled = !isDisabled
        binding.watchedTextview.alpha = if (isDisabled)  0.38f else 1f

        binding.watchedCheckbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            viewModel.updateWatched(currentMovieIds.trakt, isChecked)
        }
    }


    private fun setAlphaForDrawable(floatAlpha: Float): Int {
        return (floatAlpha * 255).toInt()
    }


    private fun loadTMDBImagesWithCustomGlide() {
        Glide.with(requireContext())
            .load(ImageTmdbRequest.MovieHorizontal(currentMovieIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.imageHorizontal)


        Glide.with(requireContext())
            .load(ImageTmdbRequest.MovieVertical(currentMovieIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.imageVertical)
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
