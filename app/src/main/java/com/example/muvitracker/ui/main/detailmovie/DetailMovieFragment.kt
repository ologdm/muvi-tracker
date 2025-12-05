package com.example.muvitracker.ui.main.detailmovie

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.MyApp
import com.example.muvitracker.R
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.DialogMyNotesBinding
import com.example.muvitracker.databinding.FragmentDetailMovieBinding
import com.example.muvitracker.domain.model.Movie
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.person.adapters.CastAdapter
import com.example.muvitracker.ui.main.detailmovie.adapters.RelatedMoviesAdapter
import com.example.muvitracker.utils.formatToReadableDate
import com.example.muvitracker.utils.getNowFormattedDateTime
import com.example.muvitracker.utils.orDefaultText
import com.example.muvitracker.utils.statesFlowDetail
import com.example.muvitracker.utils.twoStatesFlow
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/** Linee
 *   —        (Shift + Option + -)
 *   –        (Option + -)
 *   -        (-)
 */


// 1.1.3 grafica edge toedge
// status bar dinamiche ok  TODO fix
// trailer sbiadito ok
// back dinamico ok TODO fix background
// floating like dinamico ok  TODO fix layout
// TODO - fix listeners, ottimizzare lettura valori default sistema Material3.DynamicColors
//  torna a valori di default onDestroy {1.listener null, 2. status bar default }


@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragment_detail_movie) {

    private var currentMovieIds: Ids = Ids()

    private val b by viewBinding(FragmentDetailMovieBinding::bind)
    private val viewModel by viewModels<DetailMovieViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val relatedMoviesAdapter = RelatedMoviesAdapter(onClickVH = { ids ->
        navigator.startMovieDetailFragment(ids)
    })


    private val castMovieAdapter = CastAdapter(onClickVH = { ids, character ->
        navigator.startPersonFragmentFromCast(ids, character)
    })


    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupTopEdgeToEdgeAutoPaddingToLayoutElements()
        setupStatusBarEdgeToEdgeScrollEffect()
        setupButtonsScrollEffects()


        val bundle = arguments
        if (bundle != null) {
            currentMovieIds = bundle.getParcelable(MOVIE_IDS_KEY) ?: Ids()
        }

        // MOVIE DETAIL ---------------------------------------------------------------------------------
        viewModel.loadMovieDetailFlow(currentMovieIds)
        // TODO: 1.1.3 NEW loading, data + no internet, no data no internet OK
        viewModel.detailState.observe(viewLifecycleOwner) { stateContainer ->
            // main_layout_detail.xml  -> default GONE

            stateContainer.statesFlowDetail(
                b.errorTextView,
                b.progressBar,
                b.mainLayoutToDisplayDetail,
                bindData = { movie ->
                    setupDetailMovieUiSection(movie) // base movie UI
                    updateFavoriteIcon(movie.liked)
                    updateWatchedCheckbox(movie)
                }
            )
        }


        loadRelatedSetup()
        loadCastSetup()
        myNotesDialogSetup()


        // BUTTONS CLICK ------------------------------------------------------------------------------
        b.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        b.extendedFloatingLikedButton.setOnClickListener {
            viewModel.toggleLikedMovie(currentMovieIds.trakt)
        }

        b.watchedCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateWatched(currentMovieIds.trakt, isChecked)
        }
    }


    private fun loadRelatedSetup() {
        b.relatedRecyclerview.adapter = relatedMoviesAdapter
        b.relatedRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.loadRelatedMovies(currentMovieIds.trakt)

        viewModel.relatedMoviesState.observe(viewLifecycleOwner) { listStateContainer ->
            listStateContainer.twoStatesFlow(
                progressBar = b.relatedProgressBar,
                errorTextview = b.relatedErrorMessage,
                errorMsg = getString(R.string.not_available),
                recyclerView = b.relatedRecyclerview,
                bindData = { list ->
                    relatedMoviesAdapter.submitList(list)
                }
            )
        }
    }


    private fun loadCastSetup() {
        b.castRecyclerView.adapter = castMovieAdapter
        b.castRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.loadCast(currentMovieIds.trakt)
        // TODO 1.1.3 OK
        viewModel.castState.observe(viewLifecycleOwner) { listStateContainer ->

            listStateContainer.twoStatesFlow(
                progressBar = b.castProgressBar,
                errorTextview = b.castErrorMessage,
                errorMsg = getString(R.string.actors_are_not_available),
                recyclerView = b.castRecyclerView,
                bindData = { data ->
                    castMovieAdapter.submitList(data)
                }
            )
        }
    }




    // PRIVATE FUNCTIONS ###############################################
    // movie detail
    // TODO DEFAULT CASES - OK
    private fun setupDetailMovieUiSection(movie: Movie) {
        //
//        b.title.text = movie.title.orIfBlank(MovieDefaults.TITLE)
        b.title.text = movie.title.orDefaultText(MovieDefaults.TITLE)
        //
        b.tagline.apply {
            if (movie.tagline.isNullOrBlank()) {
                visibility = View.GONE
            } else {
                text = movie.tagline
                visibility = View.VISIBLE
            }
        }

        // info section ----------------------------------------------------------------
        b.status.text = movie.status?.replaceFirstChar { it.uppercaseChar() }
            .orDefaultText(MovieDefaults.STATUS)  // es released

        // date (country)
        // 1° element
        val releaseDate = movie.releaseDate?.formatToReadableDate()
        val releaseDateText = releaseDate.orDefaultText(MovieDefaults.YEAR)

        // 2° element - 2 formats
        val countryList = movie.countries.filter { it.isNotBlank() }
        val countryText = if (countryList.isNullOrEmpty()) {
            ", ${MovieDefaults.COUNTRY}"
        } else {
            " (${countryList.joinToString(", ")})"
        }
        // 1° + 2°
        b.releasedDateAndCountry.text = "$releaseDateText$countryText"

        //
        b.runtime.text =
            if (movie.runtime != null && movie.runtime > 0)
                getString(R.string.runtime_description, movie.runtime.toString())
            else
                MovieDefaults.RUNTIME

        //
        ratingLayoutsSetup(movie)

        // TODO 1.1.3  - ok in inglese
        b.englishTitle.apply {
            if (movie.title != movie.englishTraktTitle) {
                visibility = View.VISIBLE
                text = "${movie.englishTraktTitle}"
            } else
                visibility = View.GONE
        }
        //
        b.overviewContent.text = movie.overview.orDefaultText(MovieDefaults.OVERVIEW)
        expandOverviewSetup()
        //
        // genres
        b.genresChipGroup.removeAllViews()
        movie.genres.forEach { genre ->
            val chip = Chip(context).apply {
                text = genre
                isClickable = false
                // 1.1.3 - fixed padding between rows
                setEnsureMinTouchTargetSize(false) // clickable external padding to 0dp (on xml: app:chipMinTouchTargetSize="0dp" )
            }
            b.genresChipGroup.addView(chip)
        }


        // Open Link On Youtube --------------------------------------------
        val trailerUrl = movie.youtubeTrailer
        val trailerAvailable = !trailerUrl.isNullOrBlank()
        // sbiadimento senza trailer
        b.trailerLayout.alpha = if (trailerAvailable) 1f else 0.4f
        //
        b.trailerLayout.setOnClickListener {
            if (trailerAvailable) {
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

        b.notesTextview.text = movie.notes


        loadTmdbImagesWithCustomGlide()
    }


    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = R.drawable.liked_icon_filled
        val iconEmpty = R.drawable.liked_icon_empty
        b.extendedFloatingLikedButton.setIconResource(if (isFavorite) iconFilled else iconEmpty)
    }

    private fun myNotesDialogSetup() {
        b.notesLayout.setOnClickListener {
            // Inflating del layout con ViewBinding
            val dialogBinding = DialogMyNotesBinding.inflate(layoutInflater)
            // Aggiorna note con valore attuale
            dialogBinding.noteContentText.setText(viewModel.getNotes())

            // Crea il dialog Material
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(dialogBinding.root)
                .setCancelable(true)
                .create()
            // Mostra il dialog prima di modificare il background
            dialog.show()

            // modifica background -> bordi arrotondati dialog
            // dialog.window -> disponibile solo dopo .show()
            dialog.window?.decorView?.background = MaterialShapeDrawable().apply {
                shapeAppearanceModel = ShapeAppearanceModel.builder()
                    .setAllCornerSizes(16f.dpToPx(requireContext())) // arrotondamento 16dp
                    .build()
                fillColor = ColorStateList.valueOf(
                    MaterialColors.getColor(
                        dialogBinding.root,
                        com.google.android.material.R.attr.colorSurface
                    )
                )
            }

            // TODO:
            // Save button -----------------------------------------------------
            dialogBinding.saveNoteButton.setOnClickListener {
                val notes = dialogBinding.noteContentText.text.toString()
                viewModel.setNotes(currentMovieIds.trakt, notes)
                dialog.dismiss()
            }
        }
    }

    // Helper per convertire dp in pixel
    private fun Float.dpToPx(context: Context): Float {
        return this * context.resources.displayMetrics.density
    }


    private fun ratingLayoutsSetup(movie: Movie) {
        b.traktRating.text = movie.traktRating
        b.tmdbRating.text = movie.tmdbRating

        if (movie.traktRating.isNullOrEmpty() || movie.traktRating == "0.0") {
            b.traktRating.visibility = View.GONE
            b.linkImageTrakt.visibility = View.VISIBLE
        } else {
            b.traktRating.visibility = View.VISIBLE
            b.linkImageTrakt.visibility = View.GONE
        }


        if (movie.tmdbRating.isNullOrEmpty() || movie.traktRating == "0.0") {
            b.tmdbRating.visibility = View.GONE
            b.linkImageTmdb.visibility = View.VISIBLE
        } else {
            b.tmdbRating.visibility = View.VISIBLE
            b.linkImageTmdb.visibility = View.GONE
        }


        b.traktLayout.setOnClickListener {
            // apri link
            val type = "movies"
            val traktSlug = movie.ids.slug
            val traktUrl = "https://trakt.tv/$type/$traktSlug"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(traktUrl))
            startActivity(intent)
        }

        b.tmdbLayout.setOnClickListener {
            val type = "movie"
            val tmdbId = movie.ids.tmdb
            val tmdbUrl = "https://www.themoviedb.org/$type/$tmdbId"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tmdbUrl))
            startActivity(intent)
        }
    }


    //    private fun updateWatchedCheckbox(isWatched: Boolean) {
    private fun updateWatchedCheckbox(movie: Movie) {
        b.watchedCheckbox.setOnCheckedChangeListener(null)
        b.watchedCheckbox.isChecked = movie.watched

        val isDisabled = movie.releaseDate != null && movie.releaseDate > getNowFormattedDateTime()
        b.watchedCheckbox.isEnabled = !isDisabled
        b.watchedTextview.alpha = if (isDisabled) 0.4f else 1f

        b.watchedCheckbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            viewModel.updateWatched(currentMovieIds.trakt, isChecked)
        }
    }


    private fun loadTmdbImagesWithCustomGlide() {
        Glide.with(requireContext())
            .load(ImageTmdbRequest.MovieHorizontal(currentMovieIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(b.horizontalImage)


        Glide.with(requireContext())
            .load(ImageTmdbRequest.MovieVertical(currentMovieIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(b.verticalImage)
    }


    private fun expandOverviewSetup() {
        var isTextExpanded = false // initial state, fragment opening
        b.overviewContent.setOnClickListener {
            if (isTextExpanded) { // expanded==true -> contract
                b.overviewContent.maxLines = 4
                b.overviewContent.ellipsize = TextUtils.TruncateAt.END
            } else { // expanded==false -> expand
                b.overviewContent.maxLines = Int.MAX_VALUE
                b.overviewContent.ellipsize = null
            }
            isTextExpanded = !isTextExpanded // toggle state
        }
    }


    // TODO: reset status bar color when Fragment/activity changes
    // 1.1.3 gestione colori status bar colori per edgeToEdge OK
    // - su imageHorizontal -> status bar bianche Ok
    // - restante mainScrollView -> nere OK
    // - solo per tema bianco OK
    private fun setupStatusBarEdgeToEdgeScrollEffect() {
        // Controlla se il tema corrente è chiaro
        val isLightTheme = (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) != Configuration.UI_MODE_NIGHT_YES
        if (!isLightTheme) return //  --> esci se non è tema chiaro

        val window = requireActivity().window
        val controller =
            WindowCompat.getInsetsController(window, window.decorView) // - al posto di require

        // Rendi la status bar trasparente, così l’immagine va "sotto"
//        window.statusBarColor = Color.TRANSPARENT

        // All'inizio: icone chiare (per stare sopra l’immagine)
        controller.isAppearanceLightStatusBars = false

        val scrollView = b.mainScrollView  // il tuo NestedScrollView
        val horizzontalImageView = b.horizontalImage

        // setOnScrollChangeListener - passare a TODO
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = scrollView.scrollY
            val imageBottom = horizzontalImageView.bottom - scrollY

            // threshold = punto in cui cambiare da chiaro a scuro
            val threshold = horizzontalImageView.height / 5

            if (imageBottom > threshold) {
                // status bar sopra immagine -> icone chiare
                controller.isAppearanceLightStatusBars = false
            } else {
                // immagine uscita -> icone scure
                controller.isAppearanceLightStatusBars = true
            }
        }
    }


    private fun setupButtonsScrollEffects() {
        val mainScrollView = b.mainScrollView
        val imageHorizzontal = b.horizontalImage

        val likeFab = b.extendedFloatingLikedButton
        val backButton = b.buttonBack

        // Fab parte compatto
        likeFab.shrink()

        // NUOVO
        // --- BACK BUTTON parte bianco + trasparente ---
        backButton.setBackgroundResource(R.drawable.back_button_shape_trasparent)
        backButton.imageTintList = ColorStateList.valueOf(Color.WHITE)

        mainScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            // --- FAB behavior ---
            val contentHeight = mainScrollView.getChildAt(0)?.height ?: 0
            val visibleHeight = mainScrollView.height
            val diff = contentHeight - (scrollY + visibleHeight)

            if (diff <= 300) {
                if (!likeFab.isExtended) likeFab.extend()
            } else {
                if (likeFab.isExtended) likeFab.shrink()
            }

            // --- BackButton background ---
            val imageBottom = imageHorizzontal.bottom - scrollY
            val threshold = imageHorizzontal.height / 5

            if (imageBottom > threshold) {
                // Sopra immagine → trasparente + icona bianca
                backButton.setBackgroundResource(R.drawable.back_button_shape_trasparent)
                backButton.imageTintList = ColorStateList.valueOf(Color.WHITE)
            } else {
                // Fuori immagine → sfondo tema + icona colore tema
                backButton.setBackgroundResource(R.drawable.back_button_shape_default)

                // trova typevalue
                val typedValue = TypedValue()
                requireContext().theme.resolveAttribute(
                    com.google.android.material.R.attr.colorOnPrimaryContainer,
                    typedValue,
                    true
                )

                backButton.imageTintList = ColorStateList.valueOf(typedValue.data)
            }
        }
    }


    // TODO 1.1.3 OK cosi
    private fun setupTopEdgeToEdgeAutoPaddingToLayoutElements() {
        ViewCompat.setOnApplyWindowInsetsListener(b.buttonBack) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top // padding extra opzionale
            }
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(b.errorTextView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                // distanza dai bordi + margine extra opzionale
                topMargin = systemBars.top
            }
            insets
        }
    }


    companion object {
        fun create(movieIds: Ids): DetailMovieFragment {
            val detailMovieFragment = DetailMovieFragment()
            val bundle = Bundle()
            bundle.putParcelable(MOVIE_IDS_KEY, movieIds)
            detailMovieFragment.arguments = bundle
            return detailMovieFragment
        }

        private const val MOVIE_IDS_KEY = "movieIdsKey"
    }


    // OK
    object MovieDefaults {
        var TITLE = MyApp.appContext.getString(R.string.untitled)

        var STATUS = MyApp.appContext.getString(R.string.status_unknown)
        var YEAR = MyApp.appContext.getString(R.string.year_n_a)
        var COUNTRY = MyApp.appContext.getString(R.string.country_n_a)
        var RUNTIME = MyApp.appContext.getString(R.string.runtime_n_a)

        var OVERVIEW = MyApp.appContext.getString(R.string.not_available)

        var LANGUAGE = MyApp.appContext.getString(R.string.language_unknown)
        var TAGLINE = MyApp.appContext.getString(R.string.tagline_are_not_available)
    }


}



