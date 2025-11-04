package com.example.muvitracker.ui.main.detailshow

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
import com.example.muvitracker.data.utils.orIfBlank
import com.example.muvitracker.databinding.FragmentDetailShowBinding
import com.example.muvitracker.domain.model.Show
import com.example.muvitracker.ui.main.Navigator
import com.example.muvitracker.ui.main.detailmovie.DetailMovieFragment.MovieDefaults
import com.example.muvitracker.ui.main.detailmovie.adapter.DetailSeasonsAdapter
import com.example.muvitracker.ui.main.detailshow.adapters.RelatedShowsAdapter
import com.example.muvitracker.ui.main.person.adapters.CastAdapter
import com.example.muvitracker.utils.statesFlow
import com.example.muvitracker.utils.statesFlowDetailTest
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO
// 1. DetailShow
// 2. Seasons
// 3. RelatedShows
// 4. CastPerson


@AndroidEntryPoint
class DetailShowFragment : Fragment(R.layout.fragment_detail_show) {

    private var currentShowTitle: String = ""
    private var currentShowIds: Ids = Ids() // ids has default value
    private var totSeasonsNumber: Int = 0


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
                totSeasonsNumber
            )
        },
        onClickWatchedAllCheckbox = { seasonNr, adapterCallback ->
            viewModel.toggleWatchedAllSingleSeasonEp(currentShowIds, seasonNr, onComplete = {
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

        setupTopEdgeToEdgeAutoPaddingToLayoutElements()
        setupStatusBarEdgeToEdgeScrollEffect()
        setupButtonsScrollEffects()

        val bundle = arguments
        if (bundle != null) {
            currentShowIds = bundle.getParcelable(SHOW_IDS_KEY) ?: Ids()
        }

        // SHOW DETAIL -----------------------------------------------------------------------------
        viewModel.loadShowDetail(currentShowIds.trakt)

        // TODO: 1.1.3 NEW loading, data + no internet, no data no internet OK
        viewModel.detailState.observe(viewLifecycleOwner) { stateContainer ->

            stateContainer.statesFlowDetailTest(
                b.errorTextView,
                b.progressBar,
                b.mainLayoutToDisplayDetail,
                bindData = { show ->
                    updateShowDetailPartOfUi(show)
                    updateFavoriteIcon(show.liked)
                    updateWatchedCheckboxAndCounters(show)
                }
            )
        }


        // BUTTONS CLICK ---------------------------------------------------------------------------
        b.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        b.extendedFloatingLikedButton.setOnClickListener {
            viewModel.toggleLikedShow(currentShowIds.trakt)
        }


        // SEASONS ---------------------------------------------------------------------------------
        b.seasonsRecyclerview.adapter = seasonsAdapter
        b.seasonsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
//        viewModel.loadAllSeasons(currentShowIds.trakt)
        viewModel.loadAllSeasons(currentShowIds)
        viewModel.allSeasonsState.observe(viewLifecycleOwner) { stateContainer ->

            stateContainer.statesFlow(
                b.seasonsProgressBar,
                b.seasonsErrorMessage,
                errorMsg = getString(R.string.seasons_not_available),
                bindData = { data ->
                    // gestione specifica per quando ho i dati, ma ma risposta null o empty da API
                    if (data.isNullOrEmpty()) {
                        b.seasonsRecyclerview.visibility = View.GONE
                        b.seasonsErrorMessage.apply {
                            text = getString(R.string.seasons_not_available)
                            visibility = View.VISIBLE
                        }
                    } else {
                        b.seasonsRecyclerview.visibility = View.VISIBLE
                        b.seasonsErrorMessage.visibility = View.GONE
                        seasonsAdapter.submitList(data)
                    }
                }
            )
        }


        // RELATED SHOWS ---------------------------------------------------------------------------
        b.relatedRecyclerview.adapter = relatedShowsAdapter
        b.relatedRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.loadRelatedShows(showId = currentShowIds.trakt)


        // TODO 1.1.3 OK
        viewModel.relatedShowsState.observe(viewLifecycleOwner) { stateContainer ->
            stateContainer.statesFlow(
                progressBar = b.relatedProgressBar,
                errorTextview = b.relatedErrorMessage,
                errorMsg = getString(R.string.not_available),
                bindData = { data ->
                    if (data.isNullOrEmpty()) {
                        b.relatedRecyclerview.visibility = View.GONE
                        b.relatedErrorMessage.apply {
                            text = getString(R.string.not_available)
                            visibility = View.VISIBLE
                        }
                    } else {
                        b.relatedRecyclerview.visibility = View.VISIBLE
                        b.relatedErrorMessage.visibility = View.GONE
                        relatedShowsAdapter.submitList(data)
                    }
                }
            )
        }


        // CAST SHOWS --------------------------------------------------------------------------
        b.castRecyclerView.adapter = castMovieAdapter
        b.castRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.loadCast(currentShowIds.trakt)
        // TODO 1.1.3 OK
//        viewModel.castState.observe(viewLifecycleOwner) {
//            castMovieAdapter.submitList(it.castMembers)
//        }
        viewModel.castState.observe(viewLifecycleOwner) { stateContainer ->

            // cast_progress_bar.xml  -> default GONE
            stateContainer.statesFlow(
                progressBar = b.castProgressBar,
                errorTextview = b.castErrorMessage,
                errorMsg = getString(R.string.actors_are_not_available),

                // gestione specifica per quando ho i dati
                bindData = { data ->
                    val castList = stateContainer.data?.castMembers
                    if (castList.isNullOrEmpty()) {
                        b.castRecyclerView.visibility = View.GONE
                        b.castErrorMessage.apply {
                            text = getString(R.string.actors_are_not_available)
                            visibility = View.VISIBLE
                        }
                    } else {
                        b.castRecyclerView.visibility = View.VISIBLE
                        b.castErrorMessage.visibility = View.GONE
                        castMovieAdapter.submitList(castList)
                    }
                }
            )
        }

        expandOverview()
        loadTMDBImagesWithCustomGlide()
    }


    // PRIVATE FUNCTIONS ###################################################
    // show detail TODO: check
    private fun updateShowDetailPartOfUi(show: Show) {
        currentShowTitle = show.title.orIfBlank(ShowDefaults.TITLE) ?: ""

        with(b) {
            // Titolo
            title.text = show.title.orIfBlank(ShowDefaults.TITLE)
            // 1.1.3 OK
            tagline.apply {
                if (show.tagline.isNullOrBlank()) {
                    visibility = View.GONE
                } else {
                    text = show.tagline
                    visibility = View.VISIBLE
                }
            }

            // Info section
            status.text = show.status?.replaceFirstChar { it.uppercaseChar() }
                .orIfBlank(MovieDefaults.STATUS)  // es released


            val networkList = show.networks.filter { it.isNotBlank() }
            val networkText = networkList.joinToString(", ")
            val countryList = show.countries.filter { it.isNotBlank() }
            val countryText = countryList.joinToString(", ")
            networkAndCountry.text = "${networkText} (${countryText})"

            // TODO:  -  inizio fine, - estrarre to year
            releaseAndEndYear.text = if (show.lastAirDate.isNullOrBlank()) {
                "dal ${show.firstAirDate?.substring(0, 4)}"
            } else {
                "dal ${show.firstAirDate?.substring(0, 4)} al ${show.lastAirDate.substring(0, 4)}"
            }

            airedEpisodes.text =
                getString(R.string.aired_episodes, show.airedEpisodes.toString())

            traktRating.text = show.traktRating
            tmdbRating.text = show.tmdbRating
            //
            // TODO 1.1.3  - ok in inglese
            englishTitle.apply {
                if (show.title != show.englishTitle) {
                    visibility = View.VISIBLE
                    text = "${show.englishTitle}"
                } else {
                    visibility = View.GONE
                }

            }

            overviewContent.text = show.overview

            // TODO: upgrade grafica
            // genres
            genresChipGroup.removeAllViews()
            show.genres.forEach { genre ->
                val chip = Chip(context).apply { text = genre }
//                chip.isEnabled = false todo
                genresChipGroup.addView(chip)
            }


            // TODO: - sistemare funzione ricerca link migliore
            // open link on youtube
            val trailerUrl = show.youtubeTrailer
            val trailerAvailable = !trailerUrl.isNullOrBlank()
            // sbiadimento senza trailer
            trailerLayout.alpha = if (trailerAvailable) 1f else 0.4f
            //
            trailerLayout.setOnClickListener {
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
            val isEnabled = show.airedEpisodes != 0
            b.watchedAllCheckbox.isEnabled = isEnabled
            b.watchedAllTextview.isEnabled = isEnabled
        }
    }


    // TODO: 1.1.3 OK
    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconFilled = R.drawable.liked_icon_filled
        val iconEmpty = R.drawable.liked_icon_empty
        b.extendedFloatingLikedButton.setIconResource(if (isFavorite) iconFilled else iconEmpty)
    }


    // TODO: check
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
            viewModel.toggleWatchedAllShowEpisodes(currentShowIds, onComplete = {
                // spegni caricamento
                b.watchedAllCheckboxLoadingBar.visibility = View.GONE
                b.watchedAllCheckbox.isEnabled = true
            })
        }
    }


    // TODO: OK
    private fun loadTMDBImagesWithCustomGlide() {
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
    private fun expandOverview() {
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


    // TODO 1.1.3 - ui animata

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

    // TODO: fix class
    object ShowDefaults {
        const val TITLE = "Untitled"
        const val RELEASE = "Year: —"
        const val RUNTIME = "Runtime: N/A"
        const val STATUS = "Status: Unknown"
        const val LANGUAGE = "Language: Unknown"
        var OVERVIEW = MyApp.appContext.getString(R.string.not_available)
        const val TAGLINE = "Tagline are not available"
        const val RATING = " — "
//    const val GENRES = "—"
    }
}