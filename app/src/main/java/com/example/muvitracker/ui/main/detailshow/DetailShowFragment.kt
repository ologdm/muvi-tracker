package com.example.muvitracker.ui.main.detailmovie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.DetailShowDto
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

    private var currentShowId: Int = 0

    val binding by viewBinding(FragmDetailShowBinding::bind)
    private val viewModel by viewModels<DetailShowViewmodel>()
    @Inject
    lateinit var navigator: Navigator

    val detailSeasonsAdapter = DetailSeasonsAdapter(onClickVH = { seasonNumber ->
        startSeasonFragment(currentShowId, seasonNumber)
    })


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val bundle = arguments // getArguments , cioè del fragment esistente
        if (bundle != null) {
            currentShowId = bundle.getInt(TRAKT_ID_KEY)
        }

        var trailerUrl: String? = null

        viewModel.state.observe(viewLifecycleOwner) { stateContainer ->
            stateContainer.data?.let { detailShowDto ->
                updateDetailUi(detailShowDto)
                trailerUrl = detailShowDto.trailer
            }
            stateContainer.statesFlow(
                binding.errorTextView,
                binding.progressBar
            )
        }

        viewModel.loadDetail(currentShowId)

        // apri link youtube OK
        binding.trailerLink.setOnClickListener {
            if (!trailerUrl.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                startActivity(intent)
            }
            // action view  - cerca un corrispondente nel manifest di tutte le app installata
            // e cerca di gestire l'intent filter

        }


        // indietro OK
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


        // TODO recyclerView
//        binding.seasonsRV.adapter =
//        binding.seasonsRV.layoutManager =


    }


    // da movie details
    private fun updateDetailUi(detailShow: DetailShowDto) {
        with(binding) {
            // OK
            title.text = detailShow.title
            status.text = detailShow.status
//            trailerLink.text = detailShow.trailer
            networkYearCountry.text =
                "${detailShow.network} ${detailShow.year.toString()} (${detailShow.country.toUpperCase()})"
            runtime.text =
                getString(R.string.runtime_description, detailShow.runtime.toString())  // string
            airedEpisodes.text = "${detailShow.airedEpisodes} episodes"
            rating.text = detailShow.rating.firstDecimalApproxToString() // conversion + string
            overview.text = detailShow.overview

            Glide.with(requireContext())
                .load(detailShow.imageUrl())
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(imageVertical)
            Glide.with(requireContext())
                .load(detailShow.imageUrl())
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .placeholder(R.drawable.glide_placeholder_base)
                .into(imageHorizontal)

            chipGroup.removeAllViews() // clean old
            detailShow.genres.forEach {
                val chip = Chip(context).apply {
                    text = it
                }
                chipGroup.addView(chip)
            }
        }

//        updateFavoriteIcon(detailShow.liked)
//        updateWatchedCheckbox(detailShow.watched)
    }


    private fun startSeasonFragment(traktShowId: Int, seasonNumber: Int) {
        // TODO on navigator
        navigator.startSeasonFragment(traktShowId,seasonNumber)
    }


    // ok
    companion object {
        private const val TRAKT_ID_KEY = "traktIdKey" // key dell'intero che invio

        fun create(traktId: Int): DetailShowFragment {
            val detailShowFragment = DetailShowFragment() // creo fragment
            val bundle = Bundle()
            bundle.putInt(TRAKT_ID_KEY, traktId)
            detailShowFragment.arguments = bundle // carico il bundle dentro il fragment
            return detailShowFragment
        }
    }


}