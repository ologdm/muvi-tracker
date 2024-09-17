package com.example.muvitracker.ui.main.detailshow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
class DetailShowFragment private constructor() : Fragment(R.layout.fragm_detail_show) {

    private var currentShowTitle: String = ""
    private var currentShowIds: Ids = Ids() // ids has default value
    private var totSeasonsNumber: Int = 0

    private val binding by viewBinding(FragmDetailShowBinding::bind)
    private val viewModel by viewModels<DetailShowViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val detailSeasonsAdapter = DetailSeasonsAdapter(onClickVH = { seasonNumber ->
        navigator.startSeasonsViewpagerFragment( // go to viewpager
            currentShowTitle,
            currentShowIds,
            seasonNumber,
            totSeasonsNumber
        )
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
        viewModel.detailState.observe(viewLifecycleOwner) { stateContainer ->
            stateContainer.data?.let { detailShow ->
                updateDetailUi(detailShow)
            }
            stateContainer.statesFlow(
                binding.errorTextView,
                binding.progressBar
            )
        }
        viewModel.loadShowDetailFlow(currentShowIds.trakt)

        // SEASONS
        binding.seasonsRV.adapter = detailSeasonsAdapter
        binding.seasonsRV.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allSeasonsState.observe(viewLifecycleOwner) { stateContainer ->
            detailSeasonsAdapter.submitList(stateContainer.data)
            totSeasonsNumber = stateContainer.data?.size ?: 0
            binding.airedSeasons.text = "${totSeasonsNumber} seasons"
        }
        viewModel.loadAllSeasons(currentShowIds.trakt)


        // TMDB
//        viewModel.getTmdbImageLinks(currentShowIds.tmdb) // for glide
        viewModel.getTmdbImageLinksFlow(currentShowIds.tmdb) // for glide

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

        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.floatingLikedButton.setOnClickListener {
            viewModel.toggleLikedItem(currentShowIds.trakt)
        }


        // TODO aggiornamento
//        binding.watchedProgressionBar
        // max = airedEpisedes
        // count = TOTWatchedCount

//        binding.watchedCheckbox.isChecked
        // se totSeasonsWatchedAll = totAiredSeasons


        // TODO crew
        // TODO related Shows
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
            chipGroup.removeAllViews() // clean old
            detailShow.genres.forEach { genre ->
                val chip = Chip(context).apply { text = genre }
                chipGroup.addView(chip)
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

        updateFavoriteIcon(detailShow.liked)
//        updateWatched - non fare

        // test
        binding.watchedCount.text = detailShow.watchedCount.toString()
        binding.watchedCheckbox.isChecked = detailShow.watchedAll
    }


    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.baseline_liked)
        val iconEmpty = context?.getDrawable(R.drawable.baseline_liked_border)

        binding.floatingLikedButton.setImageDrawable(if (isFavorite) iconFilled else iconEmpty)
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