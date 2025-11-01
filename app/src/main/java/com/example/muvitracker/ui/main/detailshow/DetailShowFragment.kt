package com.example.muvitracker.ui.main.detailshow

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
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.FragmentDetailShowBinding
import com.example.muvitracker.domain.model.Show
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.detailmovie.adapter.DetailSeasonsAdapter
import com.example.muvitracker.ui.main.detailshow.adapters.RelatedShowsAdapter
import com.example.muvitracker.ui.main.person.adapters.CastAdapter
import com.example.muvitracker.utils.statesFlow
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO
// 1. DetailShow
// 2. Seasons
// 3. RelatedShows
// 4. CastPerson


@AndroidEntryPoint
class DetailShowFragment : Fragment(R.layout.fragment_detail_show) {

    private var currentShowTitle: String = ""
    private var currentShowIds: Ids = Ids() // ids has default value
    private var totSeasonsNumber: Int = 0


    private val b by viewBinding(FragmentDetailShowBinding::bind)
    private val viewModel by viewModels<DetailShowViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val detailSeasonsAdapter = DetailSeasonsAdapter(
        onClickVH = { seasonNumber ->
            navigator.startSeasonsViewpagerFragment( // go to viewpager
                currentShowTitle,
                currentShowIds,
                seasonNumber,
                totSeasonsNumber
            )
        },
        onClickWatchedAllCheckbox = { seasonNr, adapterCallback ->
            viewModel.toggleWatchedAllSingleSeasonEp(currentShowIds, seasonNr, onComplete = {
                adapterCallback()
            })
        }
    )

    private val relatedShowsAdapter = RelatedShowsAdapter(onClickVH = { ids ->
        navigator.startShowDetailFragment(ids)
    })

    private val castMovieAdapter = CastAdapter(onClickVH = { ids, character ->
        navigator.startPersonFragmentFromCast(ids, character)
    })


    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
        }

        expandOverview()
        loadTMDBImagesWithCustomGlide()

        // SHOW DETAIL ##########################################################
        viewModel.loadShowDetail(currentShowIds.trakt)

        viewModel.detailState.observe(viewLifecycleOwner) { stateContainer ->
            // 1
            stateContainer.data?.let { detailShow ->
                updateShowDetailPartOfUi(detailShow)
                updateLikedIcon(detailShow.liked)
                updateWatchedCheckboxAndCounters(detailShow)
            }
            // 2
            stateContainer.statesFlow(
                b.errorTextView,
                b.progressBar
            )
        }


        // BUTTONS CLICK
        b.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        b.floatingLikedButton.setOnClickListener {
            viewModel.toggleLikedShow(currentShowIds.trakt)
        }


        // SEASONS ###############################################################
        b.seasonsRV.adapter = detailSeasonsAdapter
        b.seasonsRV.layoutManager = LinearLayoutManager(requireContext())
//        viewModel.loadAllSeasons(currentShowIds.trakt)
        viewModel.loadAllSeasons(currentShowIds)
        viewModel.allSeasonsState.observe(viewLifecycleOwner) { stateContainer ->
            // 1
            totSeasonsNumber = stateContainer.data?.size ?: 0
            b.totalSeasons.text = getString(R.string.total_seasons, totSeasonsNumber.toString())
            // 2
            detailSeasonsAdapter.submitList(stateContainer.data)
        }


        // RELATED SHOWS ###############################################################
        b.relatedShowsRV.adapter = relatedShowsAdapter
        b.relatedShowsRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.loadRelatedShows(showId = currentShowIds.trakt)
        viewModel.relatedShowsStatus.observe(viewLifecycleOwner) { response ->
            relatedShowsAdapter.submitList(response)
        }


        // CAST SHOWS ###############################################################
        b.castRecyclerView.adapter = castMovieAdapter
        b.castRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.loadCast(currentShowIds.trakt)
        viewModel.castState.observe(viewLifecycleOwner) {
            castMovieAdapter.submitList(it.castMembers)
        }
    }


    // PRIVATE FUNCTIONS ###################################################
    // show detail
    private fun updateShowDetailPartOfUi(show: Show) {
        currentShowTitle = show.title ?: "N/A"

        with(b) {
            title.text = show.title
            status.text = show.status // es: ended
            networkYearCountry.text =
                "${show.networks.joinToString (", ")} ${show.year} (${show.countries.joinToString (", ").toUpperCase()})"
            airedEpisodes.text =
                getString(R.string.aired_episodes, show.airedEpisodes.toString())
            runtime.text =
                getString(R.string.runtime_description, show.runtime.toString() ?: "-")

            traktRating.text = show.traktRating // (string, already converted)
            overviewContent.text = show.overview

            // genres
            genresChipGroup.removeAllViews() // clean old
            show.genres.forEach { genre ->
                val chip = Chip(context).apply { text = genre }
//                chip.isEnabled = false todo
                genresChipGroup.addView(chip)
            }

            // open link on youtube
            val trailerUrl = show.youtubeTrailer
            trailerImageButton.setOnClickListener {
                if (!trailerUrl.isNullOrEmpty()) {
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

            // upgrade  – disable checkbox click if no episodes have been aired
            val isEnabled = show.airedEpisodes != 0
            b.watchedAllCheckbox.isEnabled = isEnabled
            b.watchedAllTextview.isEnabled = isEnabled
        }
    }


    private fun updateLikedIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.liked_icon_filled)
        val iconEmpty = context?.getDrawable(R.drawable.liked_icon_empty)
        b.floatingLikedButton.setImageDrawable(if (isFavorite) iconFilled else iconEmpty)
    }


    private fun updateWatchedCheckboxAndCounters(show: Show) {
        b.watchedCounterTextview.text =
            "${show.watchedCount}/${show.airedEpisodes}"
        b.watchedCounterProgressBar.max = show.airedEpisodes
        b.watchedCounterProgressBar.progress = show.watchedCount

        // vanno sempre insieme
        // 1. togli listener - per non farlo scattare, aggiorna dato, rimetti listener
        b.watchedAllCheckbox.setOnCheckedChangeListener(null)
        // 2. leggi
        b.watchedAllCheckbox.isChecked = show.watchedAll
        // 3. rimetti listener, triggera il cambiamento di stato a db
        b.watchedAllCheckbox.setOnCheckedChangeListener { _, _ ->
            // 3.1 stato iniziale
            b.watchedAllCheckboxLoadingBar.visibility = View.VISIBLE
            b.watchedAllCheckbox.isEnabled = false
            // 3.2 stato concluso
            viewModel.toggleWatchedAllShowEpisodes(currentShowIds, onComplete = {
                // spegni caricamento
                b.watchedAllCheckboxLoadingBar.visibility = View.GONE
                b.watchedAllCheckbox.isEnabled = true
            })
        }
    }


    private fun expandOverview() {
        var isTextExpanded = false // initial state, fragment opening
        b.overviewContent.setOnClickListener {
            if (isTextExpanded) { // expanded==true -> contract
                b.overviewContent.maxLines = 4
                b.overviewContent.ellipsize = TextUtils.TruncateAt.END
            } else { // expanded==false -> expand
                b.overviewContent.maxLines = Int.MAX_VALUE
                b.overviewContent.ellipsize = null
            }
            isTextExpanded = !isTextExpanded // toggle state
        }
    }


    private fun loadTMDBImagesWithCustomGlide() {
        Glide.with(requireContext())
            .load(ImageTmdbRequest.ShowHorizontal(currentShowIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(b.horizontalImage)

        Glide.with(requireContext())
            .load(ImageTmdbRequest.ShowVertical(currentShowIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(b.verticalImage)
    }


    companion object {
        fun create(showIds: Ids): DetailShowFragment {
            val detailShowFragment = DetailShowFragment()
            val bundle = Bundle()
            bundle.putParcelable(SHOW_IDS_KEY, showIds)
            detailShowFragment.arguments = bundle
            return detailShowFragment
        }

        private const val SHOW_IDS_KEY = "showIdsKey"
    }
}