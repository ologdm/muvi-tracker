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
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.data.imagetmdb.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.FragmDetailShowBinding
import com.example.muvitracker.domain.model.DetailShow
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.detailmovie.adapter.DetailSeasonsAdapter
import com.example.muvitracker.ui.main.detailshow.adapters.RelatedShowsAdapter
import com.example.muvitracker.ui.main.person.adapters.CastAdapter
import com.example.muvitracker.utils.statesFlow
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


// 1. DetailShow
// 2. Seasons OK
// 3. RelatedShows OK
// 4. CastPerson TODO


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
            viewModel.toggleSingleSeasonWatchedAll(currentShowIds.trakt, seasonNr, onComplete = {
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

        // SHOW DETAIL
        viewModel.loadShowDetailFlow(currentShowIds.trakt)

        viewModel.detailState.observe(viewLifecycleOwner) { stateContainer ->
            // 1
            stateContainer.data?.let { detailShow ->
                updateDetailUi(detailShow)
                updateLikedIcon(detailShow.liked)
                updateWatchedCheckboxAndCounters(detailShow)
            }
            // 2
            stateContainer.statesFlow(
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

        binding.watchedAllCheckbox.setOnCheckedChangeListener { _, _ ->
            //avvia caricamento
            binding.watchedAllCheckboxLoadingBar.visibility = View.VISIBLE
            binding.watchedAllCheckbox.isEnabled = false

            viewModel.toggleWatchedAll(currentShowIds.trakt, onComplete = {
                // spegni caricamento
                binding.watchedAllCheckboxLoadingBar.visibility = View.GONE
                binding.watchedAllCheckbox.isEnabled = true
            })
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


        // TMDB IMAGES - with custom glide
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


        // RELATED SHOWS
        binding.relatedShowsRV.adapter = relatedShowsAdapter
        binding.relatedShowsRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.loadRelatedShows(showId = currentShowIds.trakt)
        viewModel.relatedShowsStatus.observe(viewLifecycleOwner) { response ->
            // related adapter
            relatedShowsAdapter.submitList(response)
        }


        // CAST TODO
        binding.castRV.adapter = castMovieAdapter
        binding.castRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.loadCast(currentShowIds.trakt)
        viewModel.castState.observe(viewLifecycleOwner) {
            castMovieAdapter.submitList(it.cast)
            println("XXX SHOW CASH ${it.cast.toString()}")
        }
    }


    // show detail
    private fun updateDetailUi(detailShow: DetailShow) {
        currentShowTitle = detailShow.title

        with(binding) {
            title.text = detailShow.title
            status.text = detailShow.status // es: ended
            networkYearCountry.text =
                "${detailShow.network} ${detailShow.year} (${detailShow.country.toUpperCase()})"
            airedEpisodes.text =
                getString(R.string.aired_episodes, detailShow.airedEpisodes.toString())
            runtime.text =
                getString(R.string.runtime_description, detailShow.runtime.toString())  // string

            traktRating.text = detailShow.rating // (string, already converted)
            overview.text = detailShow.overview

            // genres
            genresChipGroup.removeAllViews() // clean old
            detailShow.genres.forEach { genre ->
                val chip = Chip(context).apply { text = genre }
//                chip.isEnabled = false todo
                genresChipGroup.addView(chip)
            }

            // open link on youtube
            val trailerUrl = detailShow.trailer
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

        // sempre insieme (togli listener, leggi, rimetti listener)
        binding.watchedAllCheckbox.setOnCheckedChangeListener(null)
        binding.watchedAllCheckbox.isChecked = detailShow.watchedAll

        binding.watchedAllCheckbox.setOnCheckedChangeListener { _, _ ->
            binding.watchedAllCheckboxLoadingBar.visibility = View.VISIBLE
            binding.watchedAllCheckbox.isEnabled = false

            viewModel.toggleWatchedAll(currentShowIds.trakt, onComplete = {
                // spegni caricamento
                binding.watchedAllCheckboxLoadingBar.visibility = View.GONE
                binding.watchedAllCheckbox.isEnabled = true
            })
        }
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