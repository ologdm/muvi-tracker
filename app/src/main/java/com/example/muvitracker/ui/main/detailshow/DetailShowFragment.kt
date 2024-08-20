package com.example.muvitracker.ui.main.detailmovie

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
import com.example.muvitracker.data.dto.DetailShowDto
import com.example.muvitracker.data.dto.basedto.Ids
import com.example.muvitracker.databinding.FragmDetailShowBinding
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.detailmovie.adapter.DetailSeasonsAdapter
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.example.muvitracker.utils.statesFlow
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// STEPS:
// 1. detailDto
// 2. seasonsDto
// 3. relatedDto
// 4. castDto


@AndroidEntryPoint
class DetailShowFragment private constructor() : Fragment(R.layout.fragm_detail_show) {

    private var currentShowIds: Ids = Ids() // ids ha valori default
    private val binding by viewBinding(FragmDetailShowBinding::bind)
    private val viewModel by viewModels<DetailShowViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val detailSeasonsAdapter = DetailSeasonsAdapter(onClickVH = { seasonNumber ->
        navigator.startSeasonFragment(currentShowIds, seasonNumber)
    })


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // getArguments
        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
        }

        viewModel.detailState.observe(viewLifecycleOwner) { stateContainer ->
            stateContainer.data?.let { detailShowDto ->
                updateDetailUi(detailShowDto)
            }
            stateContainer.statesFlow(
                binding.errorTextView,
                binding.progressBar
            )
        }

        binding.seasonsRV.adapter = detailSeasonsAdapter
        binding.seasonsRV.layoutManager = LinearLayoutManager(requireContext())
        viewModel.allSeasonsState.observe(viewLifecycleOwner) { stateContainer ->
            detailSeasonsAdapter.submitList(stateContainer.data)
        }

        // data call
        viewModel.loadShowDetail(currentShowIds.trakt)
        viewModel.loadAllSeasons(currentShowIds.trakt)
        viewModel.getTmdbImageLinks(currentShowIds.tmdb) // for glide

        // IMAGES TMDB
        // horizontal - backdrop  TODO -ridurre dimensione
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
        // TODO crew
        // TODO related Shows
    }


    // show detail
    private fun updateDetailUi(detailShowDto: DetailShowDto) {
        with(binding) {
            title.text = detailShowDto.title
            status.text = detailShowDto.status
            networkYearCountry.text =
                "${detailShowDto.network} ${detailShowDto.year.toString()} (${detailShowDto.country.toUpperCase()})"
            runtime.text =
                getString(R.string.runtime_description, detailShowDto.runtime.toString())  // string
            // TODO seasons, calcolo
            airedEpisodes.text = "${detailShowDto.airedEpisodes} episodes"
            rating.text = detailShowDto.rating.firstDecimalApproxToString() // conversion + string
            overview.text = detailShowDto.overview


            // genres
            chipGroup.removeAllViews() // clean old
            detailShowDto.genres.forEach { genre ->
                val chip = Chip(context).apply { text = genre }
                chipGroup.addView(chip)
            }

            // open link on youtube OK
            var trailerUrl = detailShowDto.trailer
            trailerLink.setOnClickListener {
                if (!trailerUrl.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                    startActivity(intent)
                }
            }
        }

//        updateFavoriteIcon(detailShow.liked) TODO
//        updateWatchedCheckbox(detailShow.watched) TODO
    }


    companion object {
        private const val SHOW_IDS_KEY = "showIdsKey"

        fun create(showIds: Ids): DetailShowFragment {
            val detailShowFragment = DetailShowFragment()
            val bundle = Bundle()
            bundle.putParcelable(SHOW_IDS_KEY, showIds)
            detailShowFragment.arguments = bundle
            return detailShowFragment
        }
    }
}