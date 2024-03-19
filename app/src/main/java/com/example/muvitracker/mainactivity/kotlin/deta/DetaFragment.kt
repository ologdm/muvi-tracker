package com.example.muvitracker.mainactivity.kotlin.deta

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.repo.kotlin.dto.DetaDto
import com.example.muvitracker.utils.kotlin.EmptyStatesEnum
import com.example.muvitracker.utils.kotlin.EmptyStatesManagement


class DetaFragment : Fragment(), DetaContract.View {


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

    // TODO: empty states + swipeRefresh


    // altro
    private val presenter = DetaPresenter(this) // vuole una classe che implementa view, quindi this


    // COSTRUTTORE - default privato
    companion object {
        // implemento  costruttore factory -
        // costruttore deve poter essere chiamato da CLassBlueprint
        fun create(): DetaFragment {
            val fragment = DetaFragment

            TODO("finire mettendo i bundle")

        }

    }


    // creazione OK
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }


    // logica
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // inizializzazione OK
        title = view.findViewById(R.id.titoloTitle) //string
        image = view.findViewById(R.id.imageFilm) // glide

        released = view.findViewById(R.id.uscitaReleased) // string
        runtimeFilm = view.findViewById(R.id.durataRuntime) // int
        country = view.findViewById(R.id.paeseCountry) // string
        rating = view.findViewById(R.id.rating) // float
        overview = view.findViewById(R.id.descrizioneOverview) // string

        buttonBack = view.findViewById(R.id.buttonBack)
        likedButton = view.findViewById(R.id.likedButton)
        watchedCkBox = view.findViewById(R.id.watchedCheckbox)


        // TODO: empty states + swipeRefresh - view binding

        // TODO : get bundle

        // TODO: icone


        // LOGICA GET
        presenter.getMovie(traktMovieId, forceRefresh = false) // inputId creazione


        // LOGICA SET
        buttonBack.setOnClickListener {  // OK
            requireActivity().onBackPressed()
        }

        // OK
        likedButton.setOnClickListener {
            presenter.toggleFavorite()
            // toggle -> update -> set icona view
        }

        // OK
        watchedCkBox.setOnCheckedChangeListener { buttonView,
                                                  isChecked ->

            presenter.updateWatched(isChecked)
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

        updateFavoriteIcon(detaDto.liked) // isliked
        updateWatchedCheckbox(detaDto.watched) // isWatched

    }


    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled =context?.getDrawable(R.drawable.baseline_favorite_24)
        val iconEmpty =context?.getDrawable(R.drawable.baseline_favorite_border_24)

        if (isFavorite)
            likedButton.setImageDrawable(iconFilled)
        else
            likedButton.setImageDrawable(iconEmpty)


        // da dto presenter
    }

    private fun updateWatchedCheckbox(isWatched: Boolean) {
        watchedCkBox.isChecked = isWatched

        // da dto presenter
    }


    override fun emptyStatesFlow(emptyStates: EmptyStatesEnum) {
        /*
        EmptyStatesManagement.emptyStatesFlow(
            emptyStates,
        )

         */
    }


}

