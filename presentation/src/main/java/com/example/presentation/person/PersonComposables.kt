package com.example.presentation.person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.core.orDefaultText
import com.example.domain.glide.ImageTmdbRequest
import com.example.domain.model.Ids
import com.example.domain.model.Person
import com.example.presentation.R
import com.example.presentation.utils.StateContainerTwo

// NOTE: ------------------------------------------------------------------------------------
//  - usare per full compose, non mischiare xml con compose api
//  - utilizzare api comunicazione xml (fragment/fragment) e all'interno costruire compose
//  - in compose isVisible è usato per nascondere/mostrare ModalBottomSheet
// BOTTOM_SHEET --------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonBottomSheetHost(
    state: StateContainerTwo<Person>,
    character: String,
    onDismissCallback: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true
    )


    var isVisible by remember { mutableStateOf(true) }


    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                isVisible = false
                onDismissCallback()
            },
            sheetState = sheetState,
            dragHandle = null
        ) {
            // Sheet content
            PersonScreen(
                state = state,
                character = character,
                isBottomSheet = true
            )
        }
    }
}


// ---------- COMPOSABLES -----------------------------------------------------------------------

@Composable
fun PersonScreen(
    modifier: Modifier = Modifier,
    state: StateContainerTwo<Person>,
    character: String? = null, // NOTE: if null, clean person, hide character,
    isBottomSheet: Boolean = false,
    onBack: () -> Unit = {}  // NOTE: per tasto specifico back
) {
    Box(
        modifier = modifier
            .padding(12.dp)
    ) {

        when {
            state.data != null -> {
                PersonDetailLayout(
                    person = state.data!!,
                    character = character,
                    isBottomSheet = isBottomSheet,
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
    character: String? = null,
    isBottomSheet: Boolean = false,
    onBack: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        if (!isBottomSheet) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.back_button_arrow),
                    contentDescription = null
                )
            }

            HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 16.dp))
        }

        // --- imageAndInfoLayout equivalent ---
        Row(modifier = Modifier.fillMaxWidth()) {
            // verticalImage
            PersonGlideImage(
                tmdbId = person.ids.tmdb,
                modifier = Modifier
                    .weight(0.4f)
                    .aspectRatio(2f / 3f)
            )

            Spacer(modifier = Modifier.width(20.dp))

            // infoLayout
            Column(modifier = Modifier.weight(0.6f)) {
                Text(
                    text = person.name.orDefaultText("No Name Available"), // TODO OK
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                // character
                character?.let {
                    Text(
                        text = character.orDefaultText("No Character Available"),
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }


                Spacer(modifier = Modifier.height(8.dp))

                // GridLayout equivalent - NOTE: rispetta tutto OK
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
//        Text(
//            text = stringResource(R.string.biography),
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(top = 12.dp)
//        )

        // --- biographyContent equivalent ---
        Text(
            text = person.biography.orDefaultText("Not available"),
            maxLines = if (isExpanded) Int.MAX_VALUE else 5,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(top = 12.dp)
                .clickable(
                ) { isExpanded = !isExpanded }
        )

        // TODO:  TEST CON LISTA LUNGA
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = "FILM/ SERIE TV",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )



        LazyColumn() {
            items(30) { index ->
                Text(text = "Elemento #$index")
            }
        }

    }


}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PersonGlideImage(tmdbId: Int, modifier: Modifier = Modifier) {
    GlideImage(
        model = ImageTmdbRequest.Person(tmdbId),
        contentDescription = null,
        // <style name="ImageLargeRoundedShape" parent="ShapeAppearance.Material3.Corner.Large" />
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
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
    Row(
        modifier = Modifier.padding(
            vertical = 2.dp
        )
    ) {
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


// -------- PREVIEWS ------------------------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun PersonPreview() {
    val mockPerson = Person(
        ids = Ids(tmdb = 123),
        name = "Brad Pitt",
        knownForDepartment = "Acting",
        birthday = "1963-12-18",
        birthplace = "Shawnee, Oklahoma, USA",
        age = 60,
        twitter = null,
        facebook = null,
        instagram = null,
        wikipedia = null,
        biography = "William Bradley Pitt is an American actor and film producer. He is the recipient of various accolades, including two Academy Awards, a British Academy Film Award, two Golden Globe Awards, and a Primetime Emmy Award. William Bradley Pitt is an American actor and film producer. He is the recipient of various accolades, including two Academy Awards, a British Academy Film Award, two Golden Globe Awards, and a Primetime Emmy Award.",
        death = "Shawnee, Oklahoma, USA"
    )

    MaterialTheme {
        PersonScreen(
            state = StateContainerTwo(data = mockPerson),
            isBottomSheet = true,
            character = "Superman"
        )
    }
}
