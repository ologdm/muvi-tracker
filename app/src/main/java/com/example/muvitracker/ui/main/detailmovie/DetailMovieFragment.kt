package com.example.muvitracker.ui.main.detailmovie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.utilsdto.Ids
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


        expandOverview()
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
//            title.text = "${detailMovie.title} (${detailMovie.originalTitle})"
            title.text = detailMovie.title
            val yearString = detailMovie.releaseDate?.formatToReadableDate() ?: "Release date: N/A"
            releasedYearAndCountry.text =
//                "${yearString} (${detailMovie.country.uppercase()})"
                "${yearString} (${detailMovie.countries.joinToString(", ")})" ?: "Country: N/A"
            status.text =
                detailMovie.status?.replaceFirstChar { it.uppercaseChar() } ?: "Status: N/A" // es released
            runtime.text =
                getString(R.string.runtime_description, detailMovie.runtime.toString()) ?: "Runtime N/A"  // string

            traktRating.text = detailMovie.traktRating ?: "-"
            tmdbRating.text = detailMovie.tmdbRating ?: "-"
//            tagline1.text = detailMovie.tagline
            tagline.text = detailMovie.tagline ?: ""

            originalTitle.apply {
                if (detailMovie.title != detailMovie.originalTitle) {
                    visibility = View.VISIBLE
                    text = "(${detailMovie.originalTitle})"
                } else
                    visibility = View.GONE
            }


            overview.text = detailMovie.overview ?: "-"

            // genres
            genresChipGroup.removeAllViews() // clean old
            detailMovie.genres.forEach { genre ->
                val chip = Chip(context).apply { text = genre }
                genresChipGroup.addView(chip)
            }

            // open link on youtube
            val trailerUrl = detailMovie.youtubeTrailer
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

        val isDisabled = movie.releaseDate != null && movie.releaseDate > getNowFormattedDateTime()
        binding.watchedCheckbox.isEnabled = !isDisabled
        binding.watchedTextview.isEnabled = !isDisabled

        binding.watchedCheckbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            viewModel.updateWatched(currentMovieIds.trakt, isChecked)
        }
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


    private fun expandOverview() {
        var isTextExpanded = false // initial state, fragment opening
        binding.overview.setOnClickListener {
            if (isTextExpanded) { // expanded==true -> contract
                binding.overview.maxLines = 4
                binding.overview.ellipsize = TextUtils.TruncateAt.END
            } else { // expanded==false -> expand
                binding.overview.maxLines = Int.MAX_VALUE
                binding.overview.ellipsize = null
            }
            isTextExpanded = !isTextExpanded // toggle state
        }
    }


    // TODO 1.1.3 - se manca connessione e non ho dati cachati, non mostro niente
    // mostro "-" solo se i dati da data mancano
//    private fun initDefaultUi() {
//        with(binding) {
//            title.text = "-"
//            releasedYearAndCountry.text = "-"
//            status.text = "-"
//            runtime.text = getString(R.string.runtime_description, "-")
//            traktRating.text = "-"
//            tmdbRating.text = "-"
//            overview.text = "-"
//            genresChipGroup.removeAllViews()
//        }
//    }


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
