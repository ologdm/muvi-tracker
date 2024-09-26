package com.example.muvitracker.ui.main.detailshow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.databinding.FragmDetailShowBinding
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.detailmovie.adapter.DetailSeasonsAdapter
import com.example.muvitracker.ui.main.detailshow.adapters.RelatedShowsAdapter
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.example.muvitracker.utils.statesFlow
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO
// 1. detailDto OK
// 2. seasonsDto OK
// 3. relatedDto
// 4. castDto


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
        onClickWatchedAllCheckbox = { seasonNr, callback ->
            viewModel.toggleSingleSeasonWatchedAll(currentShowIds.trakt, seasonNr, onComplete = {
                callback() // Chiama la callback passando il risultato
            })
        }
    )


    private val relatedShowsAdapter = RelatedShowsAdapter(onClickVH = { ids ->
        navigator.startShowDetailFragment(ids)
    })


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
        }

        // DETAIL
        viewModel.loadShowDetailFlow(currentShowIds.trakt)

        viewModel.detailState.observe(viewLifecycleOwner) { stateContainer ->
            stateContainer.data?.let { detailShow ->
                updateDetailUi(detailShow)

                binding.watchedCounterTextview.text =
                    "${stateContainer.data!!.watchedCount}/${stateContainer.data!!.airedEpisodes}"
                binding.watchedCounterProgressBar.max = detailShow.airedEpisodes
                binding.watchedCounterProgressBar.progress = detailShow.watchedCount

                binding.watchedAllCheckbox.isChecked = detailShow.watchedAll
            }

            stateContainer.statesFlow(
                binding.errorTextView,
                binding.progressBar
            )
        }

        // overview expanded
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





        // SEASONS
        binding.seasonsRV.adapter = detailSeasonsAdapter
        binding.seasonsRV.layoutManager = LinearLayoutManager(requireContext())

        viewModel.loadAllSeasons(currentShowIds.trakt)

        viewModel.allSeasonsState.observe(viewLifecycleOwner) { stateContainer ->
            // 1 ok
            totSeasonsNumber = stateContainer.data?.size ?: 0
            binding.airedSeasons.text = "${totSeasonsNumber} seasons"
            // 2 ok
            detailSeasonsAdapter.submitList(stateContainer.data)
        }


        // TMDB test
//        viewModel.loadTmdbImageLinksFlow(currentShowIds.tmdb) // for glide
        viewModel.loadImageShowTest(currentShowIds.tmdb)

        // IMAGES TMDB
        // horizontal - backdrop
        viewModel.backdropImageUrl.observe(viewLifecycleOwner) { backdropUrl ->
            Glide.with(requireContext())
                .load(backdropUrl) // 1399 game-of-thrones
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(binding.imageHorizontal)
        }
        // vertical - poster
        viewModel.posterImageUrl.observe(viewLifecycleOwner) { posterUrl ->
            Glide.with(requireContext())
                .load(posterUrl)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(binding.imageVertical)
        }


        // BUTTONS CLICK
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.floatingLikedButton.setOnClickListener {
            viewModel.toggleLikedShow(currentShowIds.trakt)
        }

//        updateWatchedCheckbox()
        binding.watchedAllCheckbox.setOnCheckedChangeListener(null)
//        binding.watchedAllCheckbox.isChecked = isWatched todo eliminare, non serve
        binding.watchedAllCheckbox.setOnCheckedChangeListener { _, _ ->
            viewModel.toggleWatchedAll(currentShowIds.trakt)
            // todo - add checkboxLoadingBar
            println("XXX watchedAllCheckbox fragm ")
        }


        binding.watchedAllCheckbox.setOnCheckedChangeListener{_,isCheched->
            viewModel.toggleWatchedAll(currentShowIds.trakt)
        }



        // RELATED SHOWS OK
        binding.relatedShowsRV.adapter = relatedShowsAdapter
        binding.relatedShowsRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.loadRelatedShows(showId = currentShowIds.trakt)
        viewModel.relatedShowsStatus.observe(viewLifecycleOwner) { response ->
            // related adapter
            relatedShowsAdapter.submitList(response)
            println("YYY related  shows observer:$response")
        }



        // TODO crew
    }


    // show detail
    private fun updateDetailUi(detailShow: DetailShow) {
        with(binding) {
            currentShowTitle = detailShow.title
            title.text = detailShow.title
            status.text = detailShow.status
            networkYearCountry.text =
                "${detailShow.network} ${detailShow.year.toString()} (${detailShow.country.toUpperCase()})"
            runtime.text =
                getString(R.string.runtime_description, detailShow.runtime.toString())  // string
            airedEpisodes.text = "${detailShow.airedEpisodes} episodes"
            rating.text = detailShow.rating.firstDecimalApproxToString() // conversion + string
            overview.text = detailShow.overview


            // genres
            genresChipGroup.removeAllViews() // clean old
            detailShow.genres.forEach { genre ->
                val chip = Chip(context).apply { text = genre }
//                chip.isEnabled = false todo
                genresChipGroup.addView(chip)
            }

            // open link on youtube OK
            var trailerUrl = detailShow.trailer
            trailerLink.setOnClickListener {
                if (!trailerUrl.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                    startActivity(intent)
                }
            }
        }

        updateLikedIcon(detailShow.liked)
//        updateWatched - todo


    }


    private fun updateLikedIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.baseline_liked)
        val iconEmpty = context?.getDrawable(R.drawable.baseline_liked_border)

        binding.floatingLikedButton.setImageDrawable(if (isFavorite) iconFilled else iconEmpty)
    }


    private fun updateWatchedCheckbox() {

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