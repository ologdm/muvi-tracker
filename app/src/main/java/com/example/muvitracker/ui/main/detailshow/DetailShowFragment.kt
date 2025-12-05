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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.MyApp
import com.example.muvitracker.R
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.DialogMyNotesBinding
import com.example.muvitracker.databinding.FragmentDetailShowBinding
import com.example.muvitracker.domain.model.Show
import com.example.muvitracker.ui.main.Navigator
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
import java.time.LocalDate
import javax.inject.Inject

// TODO 1.1.3 ok all
// 1. DetailShow
// 2. Seasons
// 3. RelatedShows
// 4. CastPerson


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


        // SHOW DETAIL -----------------------------------------------------------------------------
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

        loadSeasonsSetup()
        loadRelatedSetup()
        loadCastSetup()
        myNotesDialogSetup()


        // BUTTONS CLICK ---------------------------------------------------------------------------
        b.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        b.extendedFloatingLikedButton.setOnClickListener {
            viewModel.toggleLikedShow(currentShowIds.trakt)
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
        b.totalSeasons.text = getString(R.string.total_seasons,show.seasonsCount.toString())



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

        // TODO: - sistemare funzione ricerca link migliore
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
        b.traktRating.text = show.traktRating
        b.tmdbRating.text = show.tmdbRating

        if (show.traktRating.isNullOrEmpty() || show.traktRating == "0.0") {
            b.traktRating.visibility = View.GONE
            b.linkImageTrakt.visibility = View.VISIBLE
        } else {
            b.traktRating.visibility = View.VISIBLE
            b.linkImageTrakt.visibility = View.GONE
        }


        if (show.tmdbRating.isNullOrEmpty() || show.traktRating == "0.0") {
            b.tmdbRating.visibility = View.GONE
            b.linkImageTmdb.visibility = View.VISIBLE
        } else {
            b.tmdbRating.visibility = View.VISIBLE
            b.linkImageTmdb.visibility = View.GONE
        }


        b.traktLayout.setOnClickListener {
            // apri link
            val type = "shows"
            val traktSlug = show.ids.slug
            val traktUrl = "https://trakt.tv/$type/$traktSlug"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(traktUrl))
            startActivity(intent)
        }

        b.tmdbLayout.setOnClickListener {
            val type = "tv"
            val tmdbId = show.ids.tmdb
            val tmdbUrl = "https://www.themoviedb.org/$type/$tmdbId"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tmdbUrl))
            startActivity(intent)
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


    object ShowDefaults {
        var TITLE = MyApp.appContext.getString(R.string.untitled)

        var STATUS = MyApp.appContext.getString(R.string.status_unknown)
        var YEAR = MyApp.appContext.getString(R.string.year_n_a)
        var NETWORK = MyApp.appContext.getString(R.string.network_n_a)
        var COUNTRY = MyApp.appContext.getString(R.string.country_n_a)

        var OVERVIEW = MyApp.appContext.getString(R.string.not_available)
    }
}