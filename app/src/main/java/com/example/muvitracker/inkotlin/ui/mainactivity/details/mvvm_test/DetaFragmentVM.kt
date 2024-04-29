package com.example.muvitracker.inkotlin.ui.mainactivity.details.mvvm_test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.databinding.FragmDetailsBinding
import com.example.muvitracker.inkotlin.data.dto.DetaDto
import com.example.muvitracker.inkotlin.utils.EmptyStatesEnum
import com.example.muvitracker.inkotlin.utils.EmptyStatesManagement
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.Locale


class DetaFragmentVM : Fragment() {

    private var traktMovieId: Int = 0

    private var bindingBase: FragmDetailsBinding? = null
    private val binding
        get() = bindingBase

    //    lateinit var viewModel:DetaViewModel // java style
    // by - creare l'istanza lazy, cioe solo quando verrà usata la prima volta
    private val viewModel by viewModels<DetaViewModel>()


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

        println("TEST MVVM YYYYYYY")
        // viewModel inizializzazione  - java style,
        // con kotlin è automatico, non serve
//        viewModel = ViewModelProvider(this, DetaVMFactory(requireContext())).get(DetaViewModel::class.java)


        viewModel.viewModelDto.observe(viewLifecycleOwner, Observer {
            updateUi(it) // aggiorno tutta la ui
        })

        viewModel.emptyState.observe(viewLifecycleOwner, Observer {
            handleEmptyStates(it) // aggiorna stato ES
        })

        val bundle = arguments
        if (bundle != null) {
            traktMovieId = bundle.getInt(TRAKT_ID_KEY)
            println("XXX_D_FRAGM_GET_BUNDLE")
        }

        with(binding!!) {

            swipeToRefresh.setOnRefreshListener {
                viewModel.getMovie(traktMovieId, forceRefresh = true)
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

        // Default - salva elemento poi mostra
        viewModel.getMovie(traktMovieId, forceRefresh = false)
    }


    // per binding
    override fun onDestroyView() {
        super.onDestroyView()
        bindingBase = null
    }


    // CONTRACT METHODS -> PRIVATE
    // GET - OK
    private fun updateUi(detaDto: DetaDto) {
        val ratingApross = approssimaDecimale(detaDto.rating)

        with(binding!!) {
            title.text = detaDto.title
            released.text = dateFormatter(detaDto.released ?: "")
//            runtime.text = "${detaDto.runtime.toString()} min"
            runtime.text = getString(R.string.runtime_description, detaDto.runtime.toString())
            country.text = detaDto.country
//            rating.text = "${ratingApross.toString()} stars"
            rating.text = getString(R.string.rating_description, ratingApross.toString())
            overview.text = detaDto.overview

            // stessa vertical e horizontal
            Glide.with(requireContext())
                .load(detaDto.getImageUrl())
                .into(imageVertical)

            Glide.with(requireContext())
                .load(detaDto.getImageUrl())
                .into(imageHorizontal)

            // chip con binding OK
            chipGroup.removeAllViews() // pulire quelli precedenti
            detaDto.genres.forEach {
                val chip = Chip(context).apply {
                    text = it
                }
                chipGroup.addView(chip)
            }
        }
        updateFavoriteIcon(detaDto.liked) // isliked
        updateWatchedCheckbox(detaDto.watched) // isWatched

        println("XXX_D_FRAGM_UPDATEUI")
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


    // nuovo enum = OK
    private fun handleEmptyStates(emptyStatesEnum: EmptyStatesEnum) {
        with(binding!!) {

            EmptyStatesManagement.emptyStatesFlow(
                emptyStatesEnum,
                insideScrollView,
                progressBar,
                errorMsgTextview
            )
            when (emptyStatesEnum) {
                EmptyStatesEnum.ON_SUCCESS,
                EmptyStatesEnum.ON_ERROR_IO,
                EmptyStatesEnum.ON_ERROR_OTHER
                -> binding?.swipeToRefresh?.isRefreshing = false

                else -> {}
            }
        }
    }


    companion object {
        fun create(traktId: Int): DetaFragmentVM {
            val detaFragment = DetaFragmentVM()
            val bundle = Bundle()
            bundle.putInt(TRAKT_ID_KEY, traktId)
            detaFragment.arguments =
                bundle  // arguments - quando voglio passare elementi alla creazione

            return detaFragment
        }

        const val TRAKT_ID_KEY = "traktId_key"
    }


    // dateFormatter - sdk 26
//    private fun dateFormatter(data: String): String {
//        // style input
//        val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        // style output
//        val formatterOutput = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)
//        // save input
//        val dataLocale = LocalDate.parse(data, formatterInput)
//        // modify to output
//        return dataLocale.format(formatterOutput)
//    }


    // dateFormatter - sdk 24 compatibile
    private fun dateFormatter(data: String): String {
        // Input date format
        val formatterInput = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        // Output date format
        val formatterOutput = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)

        // Parse the input date string
        val date = formatterInput.parse(data)
        // Format to output string
        return formatterOutput.format(date)
    }


    fun approssimaDecimale(numero: Float): String {
        return String.format("%.1f",numero)
        // % - numero
        // .1f - numero decimali
    }




}

















