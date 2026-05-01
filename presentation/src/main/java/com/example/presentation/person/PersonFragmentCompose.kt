package com.example.presentation.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
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

    // TODO OK
    val viewmodel by viewModels<PersonViewmodel>()

    // TODO OK
    private var currentPersonIds: Ids = Ids()


    // TODO OK
    //  1. creo compose view e la passo come return a 'onCreateView()';
    //  - 'onViewCreated()' non piu utilizzato
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
//            TestCompose()
            PersonScreen(
                state = state,
                onBack = {
                    // TODO: ok
                    requireActivity().onBackPressed()
                })
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

@Composable
fun PersonScreen(
    state: StateContainerTwo<Person>,
    onBack: () -> Unit
) {
    when {
        state.data != null -> {
            val person = state.data

            Column(modifier = Modifier.padding(top = 40.dp)) {
                Button(onClick = onBack) {
                    Text("Back")
                }


                PersonGlideImage(person?.ids?.tmdb ?: -1)
//                PersonImage1(person?.ids?.tmdb ?: -1)
                Text(text = person?.name.orDefaultText("Unknown"))
                Spacer(modifier = Modifier.height(12.dp))

                Text(text = person?.biography.orDefaultText("Not available"))
            }
        }

        state.isError -> {
            Text("Errore")
        }

//        else -> {
//            CircularProgressIndicator(modifier = Modifier.size(0.1.dp))
//        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PersonGlideImage(tmdbId: Int) {
    GlideImage(
        model = ImageTmdbRequest.Person(tmdbId),
        contentDescription = "Person Image",
        modifier = Modifier
            .height(250.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        contentScale = ContentScale.Crop
    ) {
        it.placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
    }
}
