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
import com.example.muvitracker.data.dto.utilsdto.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.FragmDetailShowBinding
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.detailmovie.adapter.DetailSeasonsAdapter
import com.example.muvitracker.ui.main.detailshow.adapters.RelatedShowsAdapter
import com.example.muvitracker.ui.main.person.adapters.CastAdapter
import com.example.muvitracker.utils.statesFlow1
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


// 1. DetailShow
// 2. Seasons
// 3. RelatedShows
// 4. CastPerson


@AndroidEntryPoint
class DetailShowFragment : Fragment(R.layout.fragm_detail_show) {

    private var currentShowTitle: String = ""
    private var currentShowIds: Ids = Ids() // ids has default value
    private var totSeasonsNumber: Int = 0


    private val binding by viewBinding(FragmDetailShowBinding::bind)
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
            viewModel.toggleLikedShow(currentShowIds.trakt)
        }


        // SEASONS ###############################################################
        binding.seasonsRV.adapter = detailSeasonsAdapter
        binding.seasonsRV.layoutManager = LinearLayoutManager(requireContext())
//        viewModel.loadAllSeasons(currentShowIds.trakt)
        viewModel.loadAllSeasons(currentShowIds)
        viewModel.allSeasonsState.observe(viewLifecycleOwner) { stateContainer ->
            // 1
            totSeasonsNumber = stateContainer.data?.size ?: 0
            binding.airedSeasons.text = "${totSeasonsNumber} seasons"
            // 2
            detailSeasonsAdapter.submitList(stateContainer.data)
        }


        // RELATED SHOWS ###############################################################
        binding.relatedShowsRV.adapter = relatedShowsAdapter
        binding.relatedShowsRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.loadRelatedShows(showId = currentShowIds.trakt)
        viewModel.relatedShowsStatus.observe(viewLifecycleOwner) { response ->
            relatedShowsAdapter.submitList(response)
        }


        // CAST SHOWS ###############################################################
        binding.castRV.adapter = castMovieAdapter
        binding.castRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.loadCast(currentShowIds.trakt)
        viewModel.castState.observe(viewLifecycleOwner) {
            castMovieAdapter.submitList(it.castMembers)
        }
    }


    // PRIVATE FUNCTIONS ###################################################
    // show detail
    private fun updateShowDetailPartOfUi(detailShow: DetailShow) {
        currentShowTitle = detailShow.title ?: "N/A"

        with(binding) {
            title.text = detailShow.title
            status.text = detailShow.status // es: ended
            networkYearCountry.text =
                "${detailShow.networks.joinToString (", ")} ${detailShow.year} (${detailShow.countries.joinToString (", ").toUpperCase()})"
            airedEpisodes.text =
                getString(R.string.aired_episodes, detailShow.airedEpisodes.toString())
            runtime.text =
                getString(R.string.runtime_description, detailShow.runtime.toString() ?: "-")

            traktRating.text = detailShow.traktRating // (string, already converted)
            overview.text = detailShow.overview

            // genres
            genresChipGroup.removeAllViews() // clean old
            detailShow.genres.forEach { genre ->
                val chip = Chip(context).apply { text = genre }
//                chip.isEnabled = false todo
                genresChipGroup.addView(chip)
            }

            // open link on youtube
            val trailerUrl = detailShow.youtubeTrailer
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
            val isEnabled = detailShow.airedEpisodes != 0
            binding.watchedAllCheckbox.isEnabled = isEnabled
            binding.watchedAllTextview.isEnabled = isEnabled
        }
    }


    private fun updateLikedIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.liked_icon_filled)
        val iconEmpty = context?.getDrawable(R.drawable.liked_icon_empty)
        binding.floatingLikedButton.setImageDrawable(if (isFavorite) iconFilled else iconEmpty)
    }


    private fun updateWatchedCheckboxAndCounters(detailShow: DetailShow) {
        binding.watchedCounterTextview.text =
            "${detailShow.watchedCount}/${detailShow.airedEpisodes}"
        binding.watchedCounterProgressBar.max = detailShow.airedEpisodes
        binding.watchedCounterProgressBar.progress = detailShow.watchedCount

        // vanno sempre insieme
        // 1. togli listener - per non farlo scattare, aggiorna dato, rimetti listener
        binding.watchedAllCheckbox.setOnCheckedChangeListener(null)
        // 2. leggi
        binding.watchedAllCheckbox.isChecked = detailShow.watchedAll
        // 3. rimetti listener, triggera il cambiamento di stato a db
        binding.watchedAllCheckbox.setOnCheckedChangeListener { _, _ ->
            // 3.1 stato iniziale
            binding.watchedAllCheckboxLoadingBar.visibility = View.VISIBLE
            binding.watchedAllCheckbox.isEnabled = false
            // 3.2 stato concluso
            viewModel.toggleWatchedAllShowEpisodes(currentShowIds, onComplete = {
                // spegni caricamento
                binding.watchedAllCheckboxLoadingBar.visibility = View.GONE
                binding.watchedAllCheckbox.isEnabled = true
            })
        }
    }


    private fun expandOverview() {
        var isTextExpanded = false // initial state, fragment opening
        binding.overview.setOnClickListener {
            if (isTextExpanded) { // expanded==true -> contract
                binding.overview.maxLines = 3
                binding.overview.ellipsize = TextUtils.TruncateAt.END
            } else { // expanded==false -> expand
                binding.overview.maxLines = Int.MAX_VALUE
                binding.overview.ellipsize = null
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
            .into(binding.imageHorizontal)

        Glide.with(requireContext())
            .load(ImageTmdbRequest.ShowVertical(currentShowIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.imageVertical)
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