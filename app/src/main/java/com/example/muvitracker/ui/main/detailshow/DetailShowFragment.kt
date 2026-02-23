package com.example.muvitracker.ui.main.detailshow

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
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.DialogMyNotesBinding
import com.example.muvitracker.databinding.FragmentDetailShowBinding
import com.example.muvitracker.domain.model.Provider
import com.example.muvitracker.domain.model.Show
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.detailmovie.ProvidersAdapter
import com.example.muvitracker.ui.main.detailmovie.adapter.DetailSeasonsAdapter
import com.example.muvitracker.ui.main.detailshow.adapters.RelatedShowsAdapter
import com.example.muvitracker.ui.main.person.adapters.CastAdapter
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
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


@AndroidEntryPoint
class DetailShowFragment : Fragment(R.layout.fragment_detail_show) {

    private var currentShowTitle: String = "" // serve per season fragment
    private var currentShowIds: Ids = Ids() // ids has default value
    private var totSeasonsCount: Int = 0


    private val b by viewBinding(FragmentDetailShowBinding::bind)
    private val viewModel by viewModels<DetailShowViewmodel>()

    @Inject
    lateinit var navigator: Navigator

    private val seasonsAdapter = DetailSeasonsAdapter(
        onClickVH = { seasonNumber ->
            navigator.startSeasonsViewpagerFragment( // go to viewpager
                currentShowTitle,
                currentShowIds,
                seasonNumber,
                totSeasonsCount
            )
        },
        onClickWatchedAllCheckbox = { seasonNr, adapterCallback ->
            viewModel.toggleWatchedAllSeason(currentShowIds, seasonNr, onComplete = {
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

    private val providersAdapter = ProvidersAdapter(
        onClickVH = { provider ->
            openLinkOnClick(provider, currentShowIds.slug.replace(oldChar = '-', newChar = ' '))
        }
    )

    // 1.2.0
    private val okHttpClient by lazy { OkHttpClient() }


    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // servono all'inizio funz
        setupTopEdgeToEdgeAutoPaddingToLayoutElements()
        setupStatusBarEdgeToEdgeScrollEffect()
        setupButtonsScrollEffects()

        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
        }


        // data loadings
        loadDetailShowSetup()
        loadSeasonsSetup()
        loadRelatedSetup()
        loadCastSetup()
        myNotesDialogSetup()
        loadProvidersSetup()

        // BUTTONS CLICK ---------------------------------------------------------------------------
        b.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        b.extendedFloatingLikedButton.setOnClickListener {
            viewModel.toggleLikedShow(currentShowIds.trakt)
        }

    }

    private fun loadDetailShowSetup() {
        viewModel.loadShowDetail(currentShowIds)
        // 1.1.3 NEW loading, data + no internet, no data no internet OK
        viewModel.showState.observe(viewLifecycleOwner) { stateContainer ->
            stateContainer.statesFlowDetail(
                b.errorTextView,
                b.progressBar,
                b.mainLayoutToDisplayDetail,
                bindData = { show ->
                    setupDetailShowUiSection(show) // base show ui
                    updateFavoriteIcon(show.liked)
                    updateWatchedCheckboxAndCounters(show)
                    totSeasonsCount = show.seasonsCount
                }
            )
        }
    }


    // SEASONS ------------------------------------------------------------------------------------
    private fun loadSeasonsSetup() {
        b.seasonsRecyclerview.adapter = seasonsAdapter
        b.seasonsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        viewModel.loadAllSeasons(currentShowIds)
        viewModel.seasonsState.observe(viewLifecycleOwner) { listStateContainer ->
            listStateContainer.twoStatesFlow(
                b.seasonsProgressBar,
                b.seasonsErrorMessage,
                errorMsg = getString(R.string.seasons_not_available),
                recyclerView = b.seasonsRecyclerview,
                bindData = { data ->
                    seasonsAdapter.submitList(data)
                }
            )
        }
    }


    // RELATED SHOWS --------------------------------------------------------------------------------
    private fun loadRelatedSetup() {
        b.relatedRecyclerview.adapter = relatedShowsAdapter
        b.relatedRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.loadRelatedShows(showId = currentShowIds.trakt)
        viewModel.relatedState.observe(viewLifecycleOwner) { listStateContainer ->
            listStateContainer.twoStatesFlow(
                progressBar = b.relatedProgressBar,
                errorTextview = b.relatedErrorMessage,
                errorMsg = getString(R.string.not_available),
                recyclerView = b.relatedRecyclerview,
                bindData = { data ->
                    relatedShowsAdapter.submitList(data)

                }
            )
        }
    }


    // CAST SHOWS ---------------------------------------------------------------------------------
    private fun loadCastSetup() {
        b.castRecyclerView.adapter = castMovieAdapter
        b.castRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.loadCast(currentShowIds.trakt)
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


    // PRIVATE FUNCTIONS ----------------------------------------------------------------------------
    // show detail TODO: check
    private fun setupDetailShowUiSection(show: Show) {
        // per passare a season
        currentShowTitle = show.title
            .orDefaultText(ShowDefaults.TITLE) // 1.1.3 OK

        //
        b.title.text = show.title
            .orDefaultText(ShowDefaults.TITLE) // 1.1.3 OK

        //
        b.tagline.apply { // 1.1.3 OK
            if (show.tagline.isNullOrBlank()) {
                visibility = View.GONE
            } else {
                text = show.tagline
                visibility = View.VISIBLE
            }
        }

        // INFO SECTION ON LAYOUT -----------------------------------------------------------------------------
        b.status.text = show.status?.replaceFirstChar { it.uppercaseChar() } // 1.1.3 OK
            .orDefaultText(ShowDefaults.STATUS)  // es released


        // network (country)
        // 1° element
        val networkList = show.networks.filter { it.isNotBlank() }
        val networkText =
            if (networkList.isNullOrEmpty()) {
                ShowDefaults.NETWORK
            } else {
                networkList.joinToString(", ")
            }
        // 2° element - 2 formats
        val countryList = show.countries.filter { it.isNotBlank() }
        val countryText =
            if (countryList.isNullOrEmpty()) {
                ", ${ShowDefaults.COUNTRY}"
            } else {
                " (${countryList.joinToString(", ")})"
            }
        // 1° + 2°
        b.networkAndCountry.text = "$networkText$countryText"


        // 1.1.3  - YEAR OK
        //
        val currentYear = LocalDate.now().year
        val firstYear: Int? = show.year // 2019
        val lastYear: Int? = show.lastAirDate // 2019-05-19
            ?.takeIf { it.length >= 4 } // if() return this, or null - stringa < 4
            ?.substring(0, 4)
            ?.toIntOrNull()

        b.releaseAndEndYear.text = when {
            firstYear != null &&
                    lastYear != null &&
                    lastYear > firstYear &&
                    lastYear != currentYear -> "$firstYear - $lastYear"

            firstYear != null -> getString(R.string.from_release_year, "$firstYear")

            else -> ShowDefaults.YEAR
        }


        // 1.1.3 - OK
        b.airedEpisodes.text =
            getString(R.string.aired_episodes, show.airedEpisodes.toString())

        // 1.1.3 ok - spostato su repo
        b.totalSeasons.text = getString(R.string.total_seasons, show.seasonsCount.toString())


        // TODO 1.1.3: click su film
        ratingLayoutsSetup(show)

        //
        // 1.1.3 - IN INGLESE OK
        b.englishTitle.apply {
            if (show.title != show.englishTitle) {
                visibility = View.VISIBLE
                text = "${show.englishTitle}"
            } else {
                visibility = View.GONE
            }

        }
        // 1.1.3 - OK
        b.overviewContent.text = show.overview
            .orDefaultText(ShowDefaults.OVERVIEW)
        expandOverviewSetup()

        // TODO: upgrade grafica
        // genres
        b.genresChipGroup.removeAllViews()
        show.genres.forEach { genre ->
            val chip = Chip(context).apply {
                text = genre
                isClickable = false
                // 1.1.3 - fixed padding between rows
                setEnsureMinTouchTargetSize(false) // clickable external padding to 0dp (on xml: app:chipMinTouchTargetSize="0dp" )
            }
            b.genresChipGroup.addView(chip)
        }

        // sistemare funzione ricerca link migliore - OK
        // open link on youtube
        val trailerUrl = show.youtubeTrailer
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

        // upgrade  – disable checkbox click if no episodes have been aired
        val isEnabled = show.airedEpisodes > 0
        b.watchedAllCheckbox.isEnabled = isEnabled
        b.watchedAllTextview.isEnabled = isEnabled
        b.watchedAllTextview.alpha = if (isEnabled) 1f else 0.4f

        b.notesTextview.text = show.notes


        loadTMDBImagesWithCustomGlideSetup()
    }


    // TODO: 1.1.3 OK
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

            // Save button -----------------------------------------------------
            dialogBinding.saveNoteButton.setOnClickListener {
                val notes = dialogBinding.noteContentText.text.toString()
                viewModel.setNotes(currentShowIds.trakt, notes)
                dialog.dismiss()
            }
        }
    }

    // Helper per convertire dp in pixel
    private fun Float.dpToPx(context: Context): Float {
        return this * context.resources.displayMetrics.density
    }


    private fun ratingLayoutsSetup(show: Show) {
        val ratingsB = b.ratingsLayout
        // show ratings ------------------------------
        ratingsB.traktRatingNumber.text = show.traktRating
        ratingsB.imdbRatingNumber.text = show.imdbRating
        ratingsB.tomatoesRatingNumber.text = show.rottenTomatoesRating

        // rating number/icon ---------------------
        val invalidRatings = listOf(null, "0.0", "N/A", "n/a", "-")

        val hasTraktRating = show.traktRating !in invalidRatings
        val hasImdbRating = show.imdbRating !in invalidRatings
        val hasTomatoesRating = show.rottenTomatoesRating !in invalidRatings

        // visibility ------------------------
        ratingsB.traktRatingNumber.visibility = if (hasTraktRating) View.VISIBLE else View.GONE
        ratingsB.traktLinkIcon.visibility = if (hasTraktRating) View.GONE else View.VISIBLE

        ratingsB.imdbRatingNumber.visibility = if (hasImdbRating) View.VISIBLE else View.GONE
        ratingsB.imdbLinkIcon.visibility = if (hasImdbRating) View.GONE else View.VISIBLE

        ratingsB.tomatoesRatingNumber.visibility =
            if (hasTomatoesRating) View.VISIBLE else View.GONE
        ratingsB.tomatoesLinkIcon.visibility = if (hasTomatoesRating) View.GONE else View.VISIBLE


        // click on ratings ------------------------------------------------------
        ratingsB.traktLayout.setOnClickListener {
            // apri link
            val type = "shows"
            val traktSlug = show.ids.slug
            val traktUrl = "https://trakt.tv/$type/$traktSlug"
            val intent = Intent(Intent.ACTION_VIEW, traktUrl.toUri())
            startActivity(intent)
        }

        ratingsB.imdbLayout.setOnClickListener {
            // NOTES: automatic language from the browser settings
            val imdbId = show.ids.imdb
            val imdbUrl = "https://www.imdb.com/title/$imdbId"
            val intent = Intent(Intent.ACTION_VIEW, imdbUrl.toUri())
            startActivity(intent)
        }

        ratingsB.tomatoesLayout.setOnClickListener {
            // stessa logica come show

            // caso1 - underscore con anno
            val directSlug1 = show.ids.slug.replace(oldChar = '-', newChar = '_') // con anno
            // caso2 - underscore senza anno
            val directSlug2 = show.ids.slug.replace(oldChar = '-', newChar = '_')
                .replace(
                    Regex("(_\\d{4})$"),
                    ""
                ) // '_ + 4 numeri' solo se alla fine della stringa ($ → fine della stringa)
            // caso3 - con spazio
            val searchSlug = show.ids.slug.replace(oldChar = '-', newChar = ' ')

            val directUrl1 = "https://www.rottentomatoes.com/tv/$directSlug1"
            val directUrl2 = "https://www.rottentomatoes.com/tv/$directSlug2"
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


    // TODO: Unificare
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


    // TODO: check ----------------------------------------------------------------------------------------
    private fun updateWatchedCheckboxAndCounters(show: Show) {
        b.watchedCounterTextview.text =
            "${show.watchedCount}/${show.airedEpisodes}"
        b.watchedCounterProgressBar.max = show.airedEpisodes
        b.watchedCounterProgressBar.progress = show.watchedCount

        // vanno sempre insieme
        // 1. togli listener - per non farlo scattare, aggiorna dato, rimetti listener
        b.watchedAllCheckbox.setOnCheckedChangeListener(null)
        // 2. leggi
        b.watchedAllCheckbox.isChecked = show.watchedAll
        // 3. rimetti listener, triggera il cambiamento di stato a db
        b.watchedAllCheckbox.setOnCheckedChangeListener { _, _ ->
            // 3.1 stato iniziale
            b.watchedAllCheckboxLoadingBar.visibility = View.VISIBLE
            b.watchedAllCheckbox.isEnabled = false
            // 3.2 stato concluso
            viewModel.toggleWatchedAllShow(currentShowIds, onComplete = {
                // spegni caricamento
                b.watchedAllCheckboxLoadingBar.visibility = View.GONE
                b.watchedAllCheckbox.isEnabled = true
            })
        }
    }


    // TODO: OK
    private fun loadTMDBImagesWithCustomGlideSetup() {
        Glide.with(requireContext())
            .load(ImageTmdbRequest.ShowHorizontal(currentShowIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(b.horizontalImage)

        Glide.with(requireContext())
            .load(ImageTmdbRequest.ShowVertical(currentShowIds.tmdb))
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(b.verticalImage)
    }


    // TODO: OK
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


    // UI ANIMATA -----------------------------------------------------------------------------------
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


    // TODO 1.1.3 - resetta colori status bar alla default
    //      IN TEST
    private fun resetStatusBarColorsOnDefault() {
        val window = requireActivity().window
        val controller = WindowCompat.getInsetsController(window, requireView())

        // ripristina il colore della status bar dal tema
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(android.R.attr.statusBarColor, typedValue, true)
        window.statusBarColor = typedValue.data

        // ripristina icone secondo tema (chiaro/scuro)
        val isLightStatusBarsDefault = resources.getBoolean(
            resources.getIdentifier("light_icons", "bool", requireContext().packageName)
        )
        controller.isAppearanceLightStatusBars = isLightStatusBarsDefault
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

    // ---------------------------------------------------------------------------------------------

    // ---------------- 1.2.0 PROVIDERS ------------------------------------------------------------

    private fun loadProvidersSetup() {
        b.providersRecyclerView.adapter = providersAdapter
        b.providersRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        viewModel.loadProviders(currentShowIds.tmdb)
        viewLifecycleOwner.lifecycleScope.launch {
            // cancella automaticamente la coroutine quando il Fragment va in STOPPED e la rilancia se torna STARTED.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.providersState.collect {
                    providersAdapter.submitList(it)
                }
            }
        }


        b.providersCountry.setOnClickListener {
            // 1. open country list on dialog
            // 2. dialog send new value to detailMovieViewModel
            TODO("same like on movie")
        }


    }


    // 1.2.0 - APRI APP AL CLICK -----------------------
    private fun openLinkOnClick(provider: Provider, movieTitle: String) {
        when (provider.name) {
            NETFLIX, NETFLIX_FREE -> openWebUrl("https://www.netflix.com/search/${movieTitle}")
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

            else -> openWebUrl(provider.allProvidersLink)
        }
    }


    private fun openWebUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context?.startActivity(intent)
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


        // ------------- 1.2.0 Providers OK ------------------------------------------------------------
        private const val NETFLIX = "Netflix"
        private const val NETFLIX_FREE = "Netflix Free"
        private const val AMAZON_PRIME_VIDEO = "Amazon Prime Video" // https://www.primevideo.com/
        private const val AMAZON_VIDEO = "Amazon Video" // non andava
        private const val AMAZON__PRIME_VIDEO_WITH_ADS = "Amazon Prime Video with Ads" // non andava
        private const val HBO_MAX_AMAZON_CHANNEL = "HBO Max Amazon Channel"
        private const val APPLE_TV_AMAZON_CHANNEL = "Apple TV Amazon Channel"
        private const val PARAMOUNT_PLUS_AMAZON_CHANNEL = "Paramount+ Amazon Channel"
        private const val DISNEY_PLUS = "Disney Plus"

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


    object ShowDefaults {
        var TITLE = MyApp.appContext.getString(R.string.untitled)
        var STATUS = MyApp.appContext.getString(R.string.status_unknown)
        var YEAR = MyApp.appContext.getString(R.string.year_n_a)
        var NETWORK = MyApp.appContext.getString(R.string.network_n_a)
        var COUNTRY = MyApp.appContext.getString(R.string.country_n_a)

        var OVERVIEW = MyApp.appContext.getString(R.string.not_available)
    }
}