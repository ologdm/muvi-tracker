package com.example.presentation.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.core.orDefaultText
import com.example.domain.glide.ImageTmdbRequest
import com.example.domain.model.Ids
import com.example.domain.model.Person
import com.example.presentation.R
import com.example.presentation.utils.StateContainerTwo
import dagger.hilt.android.AndroidEntryPoint

// !!! similar to PersonBottomSheetFragment

@AndroidEntryPoint
class PersonFragmentCompose : Fragment() {

    private var currentPersonIds: Ids = Ids()

    val viewmodel by viewModels<PersonViewmodel>()

    // NOTE:
    //  1. creo compose view e la passo come return a 'onCreateView()';
    //  2. 'onViewCreated()' non piu utilizzato
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val bundle = arguments
        bundle?.let {
            currentPersonIds = bundle.getParcelable<Ids>(PERSON_IDS_KEY) ?: Ids()
        }

        viewmodel.loadPersonDetail(currentPersonIds)

        val composeView = ComposeView(requireContext())

        // TODO: check!! , funzionalità aggiuntiva
        composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        composeView.setContent {
            val state = viewmodel.personState.collectAsState().value
            Scaffold {
                PersonScreen(
                    modifier = Modifier.padding(it),
                    state = state,
                    onBack = {
                        requireActivity().onBackPressed()
                    })
            }
        }

        return composeView
    }


    // NOTE: 'onViewCreated' si può ommettere
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    }


    companion object {
        // from search (personDto -> personExtended)
        fun create(personIds: Ids): PersonFragmentCompose {
            val personFragment = PersonFragmentCompose()
            val bundle = Bundle()
            bundle.putParcelable(PERSON_IDS_KEY, personIds)
            personFragment.arguments = bundle
            return personFragment
        }

        private const val PERSON_IDS_KEY = "person_ids_key"
    }
}


// ---------- COMPOSABLES -----------------------------------------------------------------------

// PersonScreen
// PersonContent
// PersonImage

// ---------- TEST COMPOSABLE FROM XML ---------------------------------------------------------
@Composable
fun PersonScreen(
    modifier: Modifier = Modifier,
    state: StateContainerTwo<Person>,
    onBack: () -> Unit
) {
    Box(
        modifier = modifier
//            .background(Color.Red)
            .padding(horizontal = 8.dp)
    ) {

        when {
            state.data != null -> {
                PersonDetailLayout(
                    person = state.data!!,
                    onBack = onBack
                )
            }

            state.isError -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Errore")
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Composable
fun PersonDetailLayout(
    person: Person,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .verticalScroll(rememberScrollState()) // TOD:check
//            .padding(12.dp)
    ) {
        // --- buttonBackLayout equivalent ---
        IconButton(onClick = onBack) {
            Icon(
                painter = painterResource(id = R.drawable.back_button_arrow),
                contentDescription = null
            )
        }

        HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 16.dp))

        // --- imageAndInfoLayout equivalent ---
        Row(modifier = Modifier.fillMaxWidth()) {
            // verticalImage
            PersonGlideImage(
                tmdbId = person.ids.tmdb ?: -1,
                modifier = Modifier
                    .weight(0.4f)
                    .aspectRatio(2f / 3f)
            )

            Spacer(modifier = Modifier.width(20.dp))

            // infoLayout
            Column(modifier = Modifier.weight(0.6f)) {
                Text(
                    text = person.name.orDefaultText("Kit Harington"),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                // character (TextView id/character) - placeholder for now
                Text(
                    text = "Character Name", // TODO - if character null or empty -> bottom sheet, else
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // GridLayout equivalent - TODO: rispetta tutto OK
                PersonInfoRows(
                    label = stringResource(R.string.known_for),
                    value = person.knownForDepartment.orDefaultText("-")
                )
                PersonInfoRows(
                    label = stringResource(R.string.age),
                    value = person.age?.toString().orDefaultText("-")
                )

                val bornInfo = buildString {
                    append(person.birthday.orDefaultText(""))
                    if (!person.birthplace.isNullOrEmpty()) {
                        if (isNotEmpty()) append("\n")
                        append(person.birthplace)
                    }
                }.ifEmpty { "-" }
                PersonInfoRows(label = stringResource(R.string.born), value = bornInfo)

                if (!person.death.isNullOrEmpty()) {
                    PersonInfoRows(label = stringResource(R.string.died), value = person.death!!)
                }
            }
        }

        // --- toolbarDivider equivalent ---
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

        // --- biographyTitle equivalent ---
        Text(
            text = stringResource(R.string.biography),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // --- biographyContent equivalent ---
        Text(
            text = person.biography.orDefaultText("Not available"),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 40.dp)
        )
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PersonGlideImage(tmdbId: Int, modifier: Modifier = Modifier) {
    GlideImage(
        model = ImageTmdbRequest.Person(tmdbId),
        contentDescription = null,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
//            .background(Color.LightGray)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentScale = ContentScale.Crop
    ) {
        it.placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
    }
}


@Composable
fun PersonInfoRows(
    label: String,
    value: String
) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(85.dp), // NOTE: alignment as grid - limita dimens massima OK
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}



