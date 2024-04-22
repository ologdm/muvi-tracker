package com.example.muvitracker.inkotlin.mainactivity.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmDetailsBinding
import com.example.muvitracker.inkotlin.model.dto.DetaDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum
import com.example.muvitracker.myappunti.kotlin.EmptyStatesManagement
import com.google.android.material.chip.Chip
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/* Consegna:
 * - chips lista genere OK
 * - immagine sopra zoom, OK
 * - back button tondo, background OK
 * - floating like button OK
 * - date correzione OK
 * - rating correzione OK
 *
 */


class DetaFragment : Fragment(), DetaContract.View {

    //ATTRIBUTI OK

    private var traktMovieId: Int = 0

    private var bindingBase: FragmDetailsBinding? = null
    private val binding
        get() = bindingBase


    // METODI
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingBase = FragmDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val presenter = DetaPresenter(this, requireContext())

        val bundle = arguments
        if (bundle != null) {
            traktMovieId = bundle.getInt(TRAKT_ID_KEY)
            println("XXX_D_FRAGM_GET_BUNDLE")
        }

        with(binding!!) {

            swipeToRefresh.setOnRefreshListener {
                presenter.getMovie(traktMovieId, forceRefresh = true)
            }

            buttonBack.setOnClickListener {
                requireActivity().onBackPressed()
                println("XXX_D_FRAGMBACK_CLICK")
            }

            /*
            likedButton.setOnClickListener {
                    presenter.toggleFavorite()
                    // toggle -> update -> set icona view
                    println("XXX_D_FRAGM_LIKED_CLICK")
                }

             */

            floatingLikedButton.setOnClickListener {
                presenter.toggleFavorite()
                // toggle -> update -> set icona view
                println("XXX_D_FRAGM_LIKED_CLICK")
            }

            watchedCkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                presenter.updateWatched(isChecked)
                println("XXX_D_FRAGM_WATCHED CLICK")
            }
        }

        // Default - salva elemento poi mostra
        presenter.getMovie(traktMovieId, forceRefresh = false) // inputId creazione


        // TODO chipGroup


    }


    // per binding
    override fun onDestroyView() {
        super.onDestroyView()
        bindingBase = null
    }


    // CONTRACT METHODS
    // GET - OK
    override fun updateUi(detaDto: DetaDto) {
        val ratingApross = approssimaDecimale(detaDto.rating)

        with(binding!!) {
            title.text = detaDto.title

            released.text = dateFormatter(detaDto.released?:"")
            //runtime.text = getString()
            runtime.text = "${detaDto.runtime.toString()} min"
            country.text = detaDto.country
            rating.text = "${ratingApross.toString()} stars"
            overview.text = detaDto.overview

            // stessa vertical e horizontal
            Glide.with(requireContext())
                .load(detaDto.getImageUrl())
                .into(imageVertical)

            Glide.with(requireContext())
                .load(detaDto.getImageUrl())
                .into(imageHorizontal)


            // TODO chip con binding OK
            chipGroup.removeAllViews() // pulire quelli precedenti
            detaDto.genres.forEach {
                val chip = Chip(context).apply {
                    text = it
                }
                chipGroup.addView(chip)
            }


        }

        println("XXX_D_FRAGM_UPDATEUI")
        updateFavoriteIcon(detaDto.liked) // isliked
        updateWatchedCheckbox(detaDto.watched) // isWatched


    }


    // OK
    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.baseline_liked)
        val iconEmpty = context?.getDrawable(R.drawable.baseline_liked_border)

        if (isFavorite) {
            binding
                ?.floatingLikedButton?.setImageDrawable(iconFilled)  // da presenterDto
        } else {
            binding
                ?.floatingLikedButton?.setImageDrawable(iconEmpty)  // da presenterDto
        }
        println("XXX_D_FRAGM_LIKED_BTN_SET")
    }


    // OK
    private fun updateWatchedCheckbox(isWatched: Boolean) {
        binding?.watchedCkbox?.isChecked = isWatched    // da presenterDto
        println("XXX_D_FRAGM_WATCHED_ CKBOX")
    }


    // TODO  - nuovo enum
    override fun emptyStatesFlow(emptyStatesEnum: EmptyStatesEnum) {
        with(binding!!) {

            EmptyStatesManagement.emptyStatesFlow(
                emptyStatesEnum,
                insideScrollView,
                emptyStates.progressBar,
                emptyStates.errorMsgTextview
            )
            when (emptyStatesEnum) { // TODO OK
                EmptyStatesEnum.ON_SUCCESS,
                EmptyStatesEnum.ON_ERROR_IO,
                EmptyStatesEnum.ON_ERROR_OTHER
                -> binding?.swipeToRefresh?.isRefreshing = false

                else -> {}
            }
        }
    }


    companion object {
        fun create(traktId: Int): DetaFragment {
            val detaFragment = DetaFragment()
            val bundle = Bundle()
            bundle.putInt(TRAKT_ID_KEY, traktId)
            detaFragment.arguments =
                bundle  // arguments - quando voglio passare elementi alla creazione

            return detaFragment
        }

        const val TRAKT_ID_KEY = "traktId_key"
    }


    private fun dateFormatter(data: String): String {
        // style input
        val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // style output
        val formatterOutput = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)
        // save input
        val dataLocale = LocalDate.parse(data, formatterInput)
        // modify to output
        return dataLocale.format(formatterOutput)
    }

    fun approssimaDecimale(numero: Float, cifreDecimali: Int = 1): Float {
        return BigDecimal(numero.toString()).setScale(cifreDecimali, RoundingMode.HALF_UP).toFloat()
    }


}

















