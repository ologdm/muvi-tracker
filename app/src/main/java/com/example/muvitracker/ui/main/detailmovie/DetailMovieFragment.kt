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
import com.example.muvitracker.App
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.data.utils.orIfBlank
import com.example.muvitracker.databinding.FragmDetailMovieBinding
import com.example.muvitracker.domain.model.DetailMovie
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.person.adapters.CastAdapter
import com.example.muvitracker.ui.main.detailmovie.adapters.RelatedMovieAdapter
import com.example.muvitracker.utils.formatToReadableDate
import com.example.muvitracker.utils.getNowFormattedDateTime
import com.example.muvitracker.utils.statesFlow
import com.example.muvitracker.utils.statesFlowDetailTest
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/** Linee
 *   —        (Shift + Option + -)
 *   –        (Option + -)
 *   -        (-)
 */


@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragm_detail_movie) {

    private var currentMovieIds: Ids = Ids()

    private val viewModel by viewModels<DetailMovieViewmodel>()
    private val b by viewBinding(FragmDetailMovieBinding::bind)

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

        // TODO: 1.1.3 NEW loading, data + no internet, no data no internet OK
        viewModel.detailState.observe(viewLifecycleOwner) { stateContainer ->
            // main_layout_detail.xml  -> default GONE

            stateContainer.statesFlowDetailTest(
                b.errorTextView,
                b.progressBar,
                b.mainLayoutDetail,
                bindData = { detailMovie ->
                    updateDetailUi(detailMovie)
                    updateFavoriteIcon(detailMovie.liked)
                    updateWatchedCheckbox(detailMovie)
                }
            )
        }


        // BUTTONS CLICK ------------------------------------------------------------------------------
        b.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        b.floatingLikedButton.setOnClickListener {
            viewModel.toggleLikedMovie(currentMovieIds.trakt)
        }

        b.watchedCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateWatched(currentMovieIds.trakt, isChecked)
        }

        expandOverview()
        loadTMDBImagesWithCustomGlide()


        // RELATED MOVIES -----------------------------------------------------------------------------
        b.relatedRecyclerview.adapter = relatedMovieAdapter
        b.relatedRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.loadRelatedMovies(currentMovieIds.trakt)

        viewModel.relatedMoviesState.observe(viewLifecycleOwner) { stateContainer ->
            stateContainer.statesFlow(
                progressBar = b.relatedProgressBar,
                errorTextview = b.relatedErrorMessage,
                errorMsg = getString(R.string.not_available),
                bindData = { data ->
                    if (data.isNullOrEmpty()) {
                        b.relatedRecyclerview.visibility = View.GONE
                        b.relatedErrorMessage.apply {
                            text = getString(R.string.not_available)
                            visibility = View.VISIBLE
                        }
                    } else {
                        b.relatedRecyclerview.visibility = View.VISIBLE
                        b.relatedErrorMessage.visibility = View.GONE
                        relatedMovieAdapter.submitList(data)
                    }
                }
            )
        }


        // CAST --------------------------------------------------------------------------------------
        b.castRecyclerView.adapter = castMovieAdapter
        b.castRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.loadCast(currentMovieIds.trakt)
        // TODO 1.1.3 OK
        viewModel.castState.observe(viewLifecycleOwner) { stateContainer ->

            // cast_progress_bar.xml  -> default GONE
            stateContainer.statesFlow(
                progressBar = b.castProgressBar,
                errorTextview = b.castErrorMessage,
                errorMsg = getString(R.string.actors_are_not_available),

                // gestione specifica per quando ho i dati
                bindData = { data ->
                    val castList = stateContainer.data?.castMembers
                    if (castList.isNullOrEmpty()) {
                        b.castRecyclerView.visibility = View.GONE
                        b.castErrorMessage.apply {
                            text = getString(R.string.actors_are_not_available)
                            visibility = View.VISIBLE
                        }
                    } else {
                        b.castRecyclerView.visibility = View.VISIBLE
                        b.castErrorMessage.visibility = View.GONE
                        castMovieAdapter.submitList(castList)
                    }
                }
            )
        }
    }


    // PRIVATE FUNCTIONS ###############################################
    // movie detail
    // TODO DEFAULT CASES - OK
    private fun updateDetailUi(movie: DetailMovie) {
        with(b) {
            //
            title.text = movie.title.orIfBlank(MovieDefaults.TITLE)
            //
            val releaseData = movie.releaseDate?.formatToReadableDate()
            val releaseDataText = releaseData.takeUnless { it.isNullOrBlank() } ?: MovieDefaults.RELEASE
            val countryList = movie.countries.filter { it.isNotBlank() }
            val countryText = countryList.joinToString(", ")
            releasedYearAndCountry.text = "${releaseDataText} (${countryText})"
            //
            status.text = movie.status?.replaceFirstChar { it.uppercaseChar() }
                .orIfBlank(MovieDefaults.STATUS)  // es released
            //
            runtime.text = if (movie.runtime != null && movie.runtime > 0)
                getString(R.string.runtime_description, movie.runtime.toString())
            else
                MovieDefaults.RUNTIME
            //
            traktRating.text = movie.traktRating ?: MovieDefaults.RATING
            tmdbRating.text = movie.tmdbRating ?: MovieDefaults.RATING
            //
            tagline.text = movie.tagline.orIfBlank(MovieDefaults.TAGLINE)
            //
            // TODO TODO !!!! 27 ott
            originalTitle.apply {
                if (movie.title != movie.originalTitle) {
                    visibility = View.VISIBLE
                    text = "(${movie.originalTitle})"
                } else
                    visibility = View.GONE
            }
            //
            overview.text = movie.overview.orIfBlank(MovieDefaults.OVERVIEW)
            //
            // genres
            genresChipGroup.removeAllViews() // clean old
            movie.genres.forEach { genre ->
                val chip = Chip(context).apply { text = genre }
                genresChipGroup.addView(chip)
            }


            // TODO - sbiadire icona
            // open link on youtube
            val trailerUrl = movie.youtubeTrailer
            trailerImageButton.setOnClickListener {
                if (!trailerUrl.isNullOrBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_trailer_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.liked_icon_filled)
        val iconEmpty = context?.getDrawable(R.drawable.liked_icon_empty)
        b.floatingLikedButton.setImageDrawable(if (isFavorite) iconFilled else iconEmpty)
    }

    //    private fun updateWatchedCheckbox(isWatched: Boolean) {
    private fun updateWatchedCheckbox(movie: DetailMovie) {
        b.watchedCheckbox.setOnCheckedChangeListener(null)
        b.watchedCheckbox.isChecked = movie.watched

        val isDisabled = movie.releaseDate != null && movie.releaseDate > getNowFormattedDateTime()
        b.watchedCheckbox.isEnabled = !isDisabled
        b.watchedTextview.isEnabled = !isDisabled

        b.watchedCheckbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            viewModel.updateWatched(currentMovieIds.trakt, isChecked)
        }
    }


    private fun loadTMDBImagesWithCustomGlide() {
        Glide.with(requireContext())
            .load(ImageTmdbRequest.MovieHorizontal(currentMovieIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(b.imageHorizontal)


        Glide.with(requireContext())
            .load(ImageTmdbRequest.MovieVertical(currentMovieIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(b.imageVertical)
    }


    private fun expandOverview() {
        var isTextExpanded = false // initial state, fragment opening
        b.overview.setOnClickListener {
            if (isTextExpanded) { // expanded==true -> contract
                b.overview.maxLines = 4
                b.overview.ellipsize = TextUtils.TruncateAt.END
            } else { // expanded==false -> expand
                b.overview.maxLines = Int.MAX_VALUE
                b.overview.ellipsize = null
            }
            isTextExpanded = !isTextExpanded // toggle state
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


    // TODO: fix class
    object MovieDefaults {
        const val TITLE = "Untitled"
        const val RELEASE = "Year: —"
        const val RUNTIME = "Runtime: N/A"
        const val STATUS = "Status: Unknown"
        const val LANGUAGE = "Language: Unknown"
        var OVERVIEW = App.appContext.getString(R.string.not_available)
        const val TAGLINE = "Tagline are not available"
        const val RATING = " — "
//    const val GENRES = "—"
    }

}



