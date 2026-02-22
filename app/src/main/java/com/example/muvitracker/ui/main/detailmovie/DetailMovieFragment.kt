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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.MyApp
import com.example.muvitracker.R
import com.example.muvitracker.data.TmdbApi
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.DialogMyNotesBinding
import com.example.muvitracker.databinding.FragmentDetailMovieBinding
import com.example.muvitracker.domain.model.Movie
import com.example.muvitracker.domain.model.Provider
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import androidx.core.net.toUri
import kotlinx.coroutines.async

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

    // TODO: 1.1.4 adapter
    private val providersAdapter = ProvidersAdapter(
        onClickVH = {
            openLinkOnClick(it, currentMovieIds.slug.replace(oldChar = '-', newChar = ' '))
//            openLinkOnClick(it, currentMovieIds.slug)
        }
    )

    /**
     * Scritto cosi, Il frammento riceverà api dopo il onAttach(), quindi prima puoi usarlo solo in
     * lifecycle methods come onViewCreated.
     */
    @Inject
    lateinit var api: TmdbApi

    // 1.1.4
    //by lazy:  inizializza solo la prima volta che viene usata.
    // client HTTP per richieste GET, POST, HEAD,
    private val okHttpClient by lazy { OkHttpClient() }

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
            println("W")
        }

        // data loadings
        loadDetailMovieSetup()
        loadRelatedSetup()
        loadCastSetup()
        myNotesDialogSetup()
        // 1.1.4 - OK
        loadProvidersSetup()


        // -------------------- BUTTONS CLICK ------------------------------------------------------
        b.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        b.extendedFloatingLikedButton.setOnClickListener {
            viewModel.toggleLikedMovie(currentMovieIds.trakt)
        }

    }

    private fun loadDetailMovieSetup() {
        viewModel.loadMovieDetailFlow(currentMovieIds)
        // 1.1.3 NEW loading, data + no internet, no data no internet OK
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
        // Accesso al sub-binding
        val ratingsB = b.ratingsLayout

        // show ratings ------------------------------
        ratingsB.traktRatingNumber.text = movie.traktRating
        ratingsB.imdbRatingNumber.text = movie.imdbRating
        ratingsB.tomatoesRatingNumber.text = "${movie.rottenTomatoesRating}%"

        // rating number/icon ---------------------
        val invalidRatings = setOf(null, "", "0.0", "N/A", "n/a", "-") // TODO  EGDE CASES

        val hasTraktRating = movie.traktRating !in invalidRatings
        val hasImdbRating = movie.imdbRating !in invalidRatings
        val hasTomatoesRating = movie.rottenTomatoesRating !in invalidRatings

        // visibility ------------------------
        ratingsB.traktRatingNumber.visibility = if (hasTraktRating) View.VISIBLE else View.GONE
        ratingsB.traktLinkIcon.visibility = if (hasTraktRating) View.GONE else View.VISIBLE

        ratingsB.imdbRatingNumber.visibility = if (hasImdbRating) View.VISIBLE else View.GONE
        ratingsB.imdbLinkIcon.visibility = if (hasImdbRating) View.GONE else View.VISIBLE

        ratingsB.tomatoesRatingNumber.visibility =
            if (hasTomatoesRating) View.VISIBLE else View.GONE
        ratingsB.tomatoesLinkIcon.visibility = if (hasTomatoesRating) View.GONE else View.VISIBLE

        // click on ratings ------------------------------------------------------
        // trakt
        ratingsB.traktLayout.setOnClickListener {
            val type = "movies"
            val traktSlug = movie.ids.slug
            val traktUrl = "https://trakt.tv/$type/$traktSlug"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(traktUrl))
            startActivity(intent)
        }

        // tmdb
//        ratingsBlock.tmdbLayout.setOnClickListener {
//            val type = "movie"
//            val tmdbId = movie.ids.tmdb
//            val tmdbUrl = "https://www.themoviedb.org/$type/$tmdbId"
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tmdbUrl))
//            startActivity(intent)
//        }


        // 1.1.4 imdb OK
        ratingsB.imdbLayout.setOnClickListener {
            // NOTES: automatic language from the browser settings
            val imdbId = movie.ids.imdb
            val imdbUrl = "https://www.imdb.com/title/$imdbId"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
            startActivity(intent)
        }

        //  1.1.4 tomatoes OK
        ratingsB.tomatoesLayout.setOnClickListener {
            // PSEUDOCODE: direct 'nome_film_anno', fallback direct 'nome_film', fallback search 'nome film anno'
            // NOTE: certi film remakes hanno anno, es superman_2025, altrimenti trova superman vecchio

            // caso1 - underscore con anno
            val directSlug1 = movie.ids.slug.replace(oldChar = '-', newChar = '_') // con anno
            // caso2 - underscore senza anno
            val directSlug2 = movie.ids.slug.replace(oldChar = '-', newChar = '_')
                .replace(
                    Regex("(_\\d{4})$"),
                    ""
                ) // '_ + 4 numeri' solo se alla fine della stringa ($ → fine della stringa)
            // caso3 - con spazio
            val searchSlug = movie.ids.slug.replace(oldChar = '-', newChar = ' ')

            val directUrl1 = "https://www.rottentomatoes.com/m/$directSlug1"
            val directUrl2 = "https://www.rottentomatoes.com/m/$directSlug2"
            val searchUrl = "https://www.rottentomatoes.com/search?search=$searchSlug"

            // scegli a cascata tra 3 casi diversi
            viewLifecycleOwner.lifecycleScope.launch {
                // chiamate in parallelo per velocizzare checks
                val diff1 = async { urlExists(okHttpClient, directUrl1) }
                val diff2 = async { urlExists(okHttpClient, directUrl2) }
                val exist1 = diff1.await()
                val exist2 = diff2.await()

                val urlToOpen = when { // check sequenziale !!
                    exist1 -> directUrl1// test 1 (con anno)
                    exist2 -> directUrl2 // test 2 (_) fallback
                    else -> searchUrl // fallback finale (" ")
                }

                val intent = Intent(Intent.ACTION_VIEW, urlToOpen.toUri())
                startActivity(intent)
            }
        }
    }


    private suspend fun urlExists(
        client: OkHttpClient,
        url: String,
        useHead: Boolean = false
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Request
                val requestBuilder = Request.Builder().url(url)
                val request = if (useHead) {
                    requestBuilder.head().build()// solo head()
                } else {
                    requestBuilder.get().build() // alternativa get(), accesso standard al sito
                }

                // 2. Response
                client.newCall(request).execute().use {
                    it.isSuccessful // true se 200..299
                }

            } catch (e: Exception) {
                false // per ogni eccezione ritorna false
            }
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


    // ---------------- 1.1.4 PROVIDERS ------------------------------------------------------------

    private fun loadProvidersSetup() {
        b.providersRecyclerView.adapter = providersAdapter
        b.providersRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        viewModel.loadProviders(currentMovieIds.tmdb)
        viewLifecycleOwner.lifecycleScope.launch {
            // cancella automaticamente la coroutine quando il Fragment va in STOPPED e la rilancia se torna STARTED.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.providersState.collect {
                    providersAdapter.submitList(it)
                }
            }
        }


        b.providersCountry.text = viewModel.countryEnum.displayName

        b.providersCountry.setOnClickListener {
            // 1. open country list on dialog
            // 2. dialog send new value to detailMovieViewModel

        }


    }


    // 1.1.4 - APRI APP AL CLICK -----------------------
    // funziona solo ricerca su netflix, per le altre servono i deeplink
    // quasi tutte le piattaforme usano dei link specifici non ottenibili con la ricerca (ne tmdb, ne just watch, ne google )
    // TODO: 1.1.5 deeplink da justwatch API
    private fun openLinkOnClick(provider: Provider, movieTitle: String) {
        when (provider.name) {
            // netflix - apri ricerca in app - OK
            NETFLIX, NETFLIX_FREE -> openWebUrl("https://www.netflix.com/search/${movieTitle}")
            // prime - non apre correttamente, link ricerca funziona solo da browser
            AMAZON_PRIME_VIDEO, AMAZON_VIDEO, AMAZON__PRIME_VIDEO_WITH_ADS,
            HBO_MAX_AMAZON_CHANNEL,
            APPLE_TV_AMAZON_CHANNEL,
            PARAMOUNT_PLUS_AMAZON_CHANNEL -> openWebUrl("https://www.primevideo.com/search?phrase=${movieTitle}")

            DISNEY_PLUS -> openWebUrl("https://www.disneyplus.com/")
            HBO_MAX -> openWebUrl("https://play.hbomax.com/")
            RAKUTEN_TV -> openWebUrl("https://www.rakuten.tv/")

            PARAMOUNT_PLUS -> openWebUrl("https://www.paramountplus.com/")
            APPLE_TV_STORE, APPLE_TV -> openWebUrl("https://tv.apple.com/")
            CHILI -> openWebUrl("https://chili.com/")
            SKY_GO -> openWebUrl("https://skygo.sky.it/")
            NOW_TV -> openWebUrl("https://www.nowtv.com/")
            TIMVISION -> openWebUrl("https://timvisiontv.tim.it/")

            YOU_TUBE -> openWebUrl("https://www.youtube.com/results?search_query=${movieTitle} movie")
//            GOOGLE_PLAY_MOVIES -> openWebUrl("https://tv.google.com/") // non gestire -> else
            // test google play - non funziona
            // https://tv.google.com/share/title/The%20Hateful%20Eight?m=CgsvbS8 -> link funzionante da smartphone -> apre apre 'Google TV'
            // ?m=CgsvbS8 generata automaticamente, quindi impossibile ricreare link

            else -> openWebUrl(provider.allProvidersLink)
        }
    }


    private fun openWebUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context?.startActivity(intent)
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

        // ------------- 1.1.4 Providers OK ------------------------------------------------------------
        // apro app
        private const val NETFLIX = "Netflix" // TODO test
        private const val NETFLIX_FREE = "Netflix Free"
        private const val AMAZON_PRIME_VIDEO = "Amazon Prime Video" // https://www.primevideo.com/
        private const val AMAZON_VIDEO = "Amazon Video" // non andava
        private const val AMAZON__PRIME_VIDEO_WITH_ADS = "Amazon Prime Video with Ads" // non andava
        private const val HBO_MAX_AMAZON_CHANNEL = "HBO Max Amazon Channel"
        private const val APPLE_TV_AMAZON_CHANNEL = "Apple TV Amazon Channel"
        private const val PARAMOUNT_PLUS_AMAZON_CHANNEL = "Paramount+ Amazon Channel"
        private const val DISNEY_PLUS = "Disney Plus"

        // TODO, ora mancano i link per direttamente al film
        private const val YOU_TUBE = "YouTube"

        //        private const val GOOGLE_PLAY_MOVIES = "Google Play Movies"
        private const val HBO_MAX = "HBO Max"
        private const val RAKUTEN_TV = "Rakuten TV"
        private const val PARAMOUNT_PLUS = "Paramount Plus"
        private const val APPLE_TV_STORE = "Apple TV Store"
        private const val APPLE_TV = "Apple TV"
        private const val CHILI = "CHILI"
        private const val SKY_GO = "Sky Go"
        private const val NOW_TV = "Now TV"
        private const val TIMVISION = "Timvision"
    }


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



