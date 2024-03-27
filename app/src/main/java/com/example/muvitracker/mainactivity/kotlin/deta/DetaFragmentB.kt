package com.example.muvitracker.mainactivity.kotlin.deta
/*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmentDetailsBinding
import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum
import com.example.muvitracker.utils.kotlin.EmptyStatesManagement


class DetaFragmentB : Fragment(), /* DetaContract.View*/  {


    //ATTRIBUTI OK

    // id
    private var traktMovieId: Int = 0

    // FragmentDetailsBinding - inversione nome layout
    private lateinit var _binding: FragmentDetailsBinding
    private val binding get() = _binding
    // incapsulamento, why???


    private val presenter = DetaPresenter(this) // vuole una classe che implementa view, quindi this


    // METODI

    //OK
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }


    //
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        //  OK
        val bundle = arguments
        if (bundle != null) {
            traktMovieId = bundle.getInt(TRAKT_ID_KEY)
            println("XXX_D_FRAGM_GET_BUNDLE")
        }


        binding
            .swipeToRefresh
            .setOnRefreshListener {

                presenter.getMovie(traktMovieId, forceRefresh = true)
                //println("XXX_D_FRAGM_START_SWIPE_REFRESH") ridondante
            }


        // LOGICA GET OK
        presenter.getMovie(traktMovieId, forceRefresh = false) // inputId creazione


        // LOGICA SET OK

        binding
            .buttonBack
            .setOnClickListener {
                requireActivity().onBackPressed()
                println("XXX_D_FRAGMBACK_CLICK")
            }

        binding
            .likedButton
            .setOnClickListener {
                presenter.toggleFavorite()
                // toggle -> update -> set icona view
                println("XXX_D_FRAGM_LIKED_CLICK")
            }

        binding
            .watchedCkbox
            .setOnCheckedChangeListener { buttonView, isChecked ->
                presenter.updateWatched(isChecked)
                println("XXX_D_FRAGM_WATCHED CLICK")
            }


    }


    // serve??
    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }


// CONTRACT METHODS

    // GET - OK
    override fun updateUi(detaDto: DetaDto) {

        val stringaRuntime = "${detaDto.runtime.toString()} min"
        val stringaRating = "${detaDto.rating.toString()} stars"

        with(binding) {
            title.text = detaDto.title

            released.text = detaDto.released
            runtime.text = stringaRuntime
            country.text = detaDto.country
            rating.text = stringaRating
            overview.text = detaDto.overview

            Glide.with(requireContext())
                .load(detaDto.getImageUrl())
                .into(image)
        }


        println("XXX_D_FRAGM_UPDATEUI")
        //updateFavoriteIcon(detaDto.liked) // isliked
        //updateWatchedCheckbox(detaDto.watched) // isWatched


    }


    override fun emptyStatesFlow(emptyStates: EmptyStatesEnum) {

    }


    // OK
    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.baseline_favorite_24)
        val iconEmpty = context?.getDrawable(R.drawable.baseline_favorite_border_24)

        if (isFavorite)
            binding
                .likedButton.setImageDrawable(iconFilled)
        else
            binding
                .likedButton.setImageDrawable(iconEmpty)

        println("XXX_D_FRAGM_LIKED_BTN_SET")

        // da presenterDto
    }

    // OK
    private fun updateWatchedCheckbox(isWatched: Boolean) {
        binding.watchedCkbox.isChecked = isWatched

        println("XXX_D_FRAGM_WATCHED_ CKBOX")
        // da presenterDto
    }


    // TODO , empty state sdnon gli riconosce

    override fun emptyStatesFlow(emptyStates: EmptyStatesEnum) {

        EmptyStatesManagement.emptyStatesFlow(
            emptyStates,
            binding.frameLayout,
            binding.buttonBack
            //binding.retryButton,
            //binding.errorMsgTextview
        )

        //println("XXX_D_FRAGM_E_STATES")

        when (emptyStates) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
            -> binding.swipeToRefresh.isRefreshing = false

            else -> {}
        }
    }


    // OK
    companion object {
        // Details Factory OK

        const val TRAKT_ID_KEY = "traktId_key"

        // implemento  costruttore factory -
        // costruttore deve poter essere chiamato da CLassBlueprint


        fun create(traktId: Int): DetaFragmentB {
            val detaFragment = DetaFragmentB()

            val bundle = Bundle()
            bundle.putInt(TRAKT_ID_KEY, traktId)
            detaFragment.arguments =
                bundle  // arguments - quando voglio passare elementi alla creazione

            return detaFragment
        }

    }



}


 */














