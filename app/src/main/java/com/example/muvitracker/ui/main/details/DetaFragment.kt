package com.example.muvitracker.ui.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.databinding.FragmDetailBinding
import com.example.muvitracker.utils.dateFormatterInMMMyyy
import com.example.muvitracker.utils.firstDecimalApproxToString
import com.google.android.material.chip.Chip


class DetaFragment : Fragment() {

    private var traktMovieId: Int = 0

    private var bindingBase: FragmDetailBinding? = null
    private val binding
        get() = bindingBase
    private val viewModel by viewModels<DetaViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingBase = FragmDetailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.stateContainer.observe(viewLifecycleOwner, Observer { state ->
            state.data?.let {
                updateUi(it)
            }

        })


        // ARGUMENTS
        val bundle = arguments
        if (bundle != null) {
            traktMovieId = bundle.getInt(TRAKT_ID_KEY)
        }

        with(binding!!) {

            swipeToRefresh.setOnRefreshListener {
                viewModel.loadDetail(traktMovieId, forceRefresh = true)
            }

            buttonBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            floatingLikedButton.setOnClickListener {
                viewModel.toggleFavorite()
            }

            watchedCkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.updateWatched(isChecked)
            }
        }

        // Default - saves item then shows
        viewModel.loadDetail(traktMovieId, forceRefresh = false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        bindingBase = null
    }


    private fun updateUi(detailDto: DetailDto) {
        val ratingApproximation = detailDto.rating.firstDecimalApproxToString()

        with(binding!!) {
            title.text = detailDto.title
            released.text = detailDto.released.dateFormatterInMMMyyy() // conversion

            runtime.text =
                getString(R.string.runtime_description, detailDto.runtime.toString())  // string
            country.text = detailDto.country

            rating.text =
                getString(R.string.rating_description, ratingApproximation) // conversion + string
            overview.text = detailDto.overview


            // same image vertical & horizontal
            Glide.with(requireContext())
                .load(detailDto.imageUrl())
                .into(imageVertical)

            Glide.with(requireContext())
                .load(detailDto.imageUrl())
                .into(imageHorizontal)


            chipGroup.removeAllViews() // pulire quelli precedenti
            detailDto.genres.forEach {
                val chip = Chip(context).apply {
                    text = it
                }
                chipGroup.addView(chip)
            }
        }

        updateFavoriteIcon(detailDto.liked)
        updateWatchedCheckbox(detailDto.watched)

    }


    // used on fun updateUi()
    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.baseline_liked)
        val iconEmpty = context?.getDrawable(R.drawable.baseline_liked_border)

        // get state from viewmodel
        if (isFavorite) {
            binding
                ?.floatingLikedButton?.setImageDrawable(iconFilled)
        } else {
            binding
                ?.floatingLikedButton?.setImageDrawable(iconEmpty)
        }

    }

    // used on fun updateUi()
    private fun updateWatchedCheckbox(isWatched: Boolean) {
        binding?.watchedCkbox?.isChecked = isWatched
    }


//    private fun handleEmptyStates(emptyStatesEnum: EmptyStatesEnum) {
//        with(binding!!) {
//
//            EmptyStatesManagement.emptyStatesFlow(
//                emptyStatesEnum,
//                insideScrollView,
//                progressBar,
//                errorMsgTextview
//            )
//            when (emptyStatesEnum) {
//                EmptyStatesEnum.ON_SUCCESS,
//                EmptyStatesEnum.ON_ERROR_IO,
//                EmptyStatesEnum.ON_ERROR_OTHER
//                -> binding?.swipeToRefresh?.isRefreshing = false
//
//                else -> {}
//            }
//        }
//    }


    companion object {
        fun create(traktId: Int): DetaFragment {
            val detaFragment = DetaFragment()
            val bundle = Bundle()
            bundle.putInt(TRAKT_ID_KEY, traktId)
            detaFragment.arguments =
                bundle

            return detaFragment
        }

        const val TRAKT_ID_KEY = "traktId_key"
    }


}

















