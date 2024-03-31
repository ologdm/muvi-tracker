package com.example.muvitracker.inkotlin.mainactivity.deta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.repo.dto.DetaDto
import com.example.muvitracker.myappunti.kotlin.EmptyStatesEnum
import com.example.muvitracker.myappunti.kotlin.EmptyStatesManagement


class DetaFragmentOld : Fragment(), DetaContract.View {


    //ATTRIBUTI OK

    // id
    private var traktMovieId: Int = 0

    // views
    private lateinit var title: TextView
    private lateinit var image: ImageView

    private lateinit var released: TextView
    private lateinit var runtimeFilm: TextView
    private lateinit var country: TextView
    private lateinit var rating: TextView
    private lateinit var overview: TextView


    private lateinit var buttonBack: ImageButton

    private lateinit var likedButton: ImageButton
    private lateinit var watchedCkBox: CheckBox

    // empty states + swipeRefresh OK
    private lateinit var frameLayout: View
    private lateinit var progressBar: ProgressBar
    private lateinit var retryButton: Button
    private lateinit var errorMsgTextview: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout



    // COSTRUTTORE - default privato


    // creazione OK
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragm_deta, container, false)
    }


    // logica
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // altro
        val presenter = DetaPresenter(this, requireContext()) // vuole una classe che implementa view, quindi this


        // inizializzazione OK
        title = view.findViewById(R.id.title) //string
        image = view.findViewById(R.id.image) // glide

        released = view.findViewById(R.id.released) // string
        runtimeFilm = view.findViewById(R.id.runtime) // int
        country = view.findViewById(R.id.country) // string
        rating = view.findViewById(R.id.rating) // float
        overview = view.findViewById(R.id.overview) // string

        buttonBack = view.findViewById(R.id.buttonBack)

        likedButton = view.findViewById(R.id.likedButton)
        watchedCkBox = view.findViewById(R.id.watchedCkbox)

        frameLayout = view.findViewById(R.id.frameLayout)
        progressBar = view.findViewById(R.id.progressBar)
        retryButton = view.findViewById(R.id.retryButton)
        errorMsgTextview = view.findViewById(R.id.errorMsgTextview)

        // swipe OK
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)

        swipeRefreshLayout.setOnRefreshListener {
            presenter.getMovie(traktMovieId, forceRefresh = true)

            //println("XXX_D_FRAGM_START_SWIPE_REFRESH") ridondante
        }

        // TODO: empty states + swipeRefresh - view binding

        //  OK
        val bundle = arguments
        if (bundle != null) {
            traktMovieId = bundle.getInt(TRAKT_ID_KEY)

            println("XXX_D_FRAGM_GET_BUNDLE")
        }

        buttonBack.setOnClickListener {  // OK
            requireActivity().onBackPressed()

            println("XXX_D_FRAGMBACK_CLICK")
        }

        // LOGICA GET
        // OK
        presenter.getMovie(traktMovieId, forceRefresh = false) // inputId creazione


        // LOGICA SET

        // OK
        likedButton.setOnClickListener {
            presenter.toggleFavorite()
            // toggle -> update -> set icona view

            println("XXX_D_FRAGM_LIKED_CLICK")
        }

        // OK
        watchedCkBox.setOnCheckedChangeListener { buttonView,
                                                  isChecked ->
            presenter.updateWatched(isChecked)
            println("XXX_D_FRAGM_WATCHED CLICK")
        }


    }

    // CONTRACT METHODS

    // GET - OK
    override fun updateUi(detaDto: DetaDto) {

        val stringaRuntime = "${detaDto.runtime.toString()} min"
        val stringaRating = "${detaDto.rating.toString()} stars"


        // setto tutto
        title.text = detaDto.title

        released.text = detaDto.released
        runtimeFilm.text = stringaRuntime
        country.text = detaDto.country
        rating.text = stringaRating
        overview.text = detaDto.overview

        Glide.with(requireContext())
            .load(detaDto.getImageUrl())
            .into(image)

        println("XXX_D_FRAGM_UPDATEUI")
        updateFavoriteIcon(detaDto.liked) // isliked
        updateWatchedCheckbox(detaDto.watched) // isWatched


    }

    // OK
    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = context?.getDrawable(R.drawable.baseline_favorite_24)
        val iconEmpty = context?.getDrawable(R.drawable.baseline_favorite_border_24)

        if (isFavorite)
            likedButton.setImageDrawable(iconFilled)
        else
            likedButton.setImageDrawable(iconEmpty)

        println("XXX_D_FRAGM_LIKED_BTN_SET")

        // da presenterDto
    }

    // OK
    private fun updateWatchedCheckbox(isWatched: Boolean) {
        watchedCkBox.isChecked = isWatched

        println("XXX_D_FRAGM_WATCHED_ CKBOX")
        // da presenterDto
    }


    //OK
    override fun emptyStatesFlow(emptyStates: EmptyStatesEnum) {

        EmptyStatesManagement.emptyStatesFlow(
            emptyStates,
            frameLayout,
            progressBar,
            retryButton,
            errorMsgTextview
        )

        //println("XXX_D_FRAGM_E_STATES")

        when (emptyStates) {
            EmptyStatesEnum.ON_SUCCESS,
            EmptyStatesEnum.ON_ERROR_IO,
            EmptyStatesEnum.ON_ERROR_OTHER
            -> swipeRefreshLayout.isRefreshing = false
            else -> {}
        }
    }




    // OK
    companion object {
        // Details Factory OK

        const val TRAKT_ID_KEY = "traktId_key"

        // implemento  costruttore factory -
        // costruttore deve poter essere chiamato da CLassBlueprint


        fun create(traktId: Int): DetaFragmentOld {
            val detaFragment = DetaFragmentOld()

            val bundle = Bundle()
            bundle.putInt(TRAKT_ID_KEY, traktId)
            detaFragment.arguments =
                bundle  // arguments - quando voglio passare elementi alla creazione

            return detaFragment
        }

    }


}

