package com.example.presentation.person

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.core.orDefaultText
import com.example.domain.glide.ImageTmdbRequest
import com.example.domain.model.Ids
import com.example.domain.model.Person
import com.example.presentation.R
import com.example.presentation.databinding.FragmentPersonBinding
import com.example.presentation.utils.StateContainerTwo
import com.example.presentation.utils.fragmentViewLifecycleScope
import com.example.presentation.utils.statesFlow
import com.example.presentation.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// !!! similar to PersonBottomSheetFragment

@AndroidEntryPoint
class PersonFragmentCompose : Fragment() {

    val viewmodel by viewModels<PersonViewmodel>()

    private var currentPersonIds: Ids = Ids()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val composeView = ComposeView(requireContext())

        val bundle = arguments
        bundle?.let {
            currentPersonIds = bundle.getParcelable<Ids>(PERSON_IDS_KEY) ?: Ids()
        }

        viewmodel.getPersonDetail(currentPersonIds)

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
                PersonImage(person?.ids?.tmdb ?: -1)
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


@Composable
fun PersonImage(tmdbId: Int) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),

        factory = { context ->
            ImageView(context).apply {
                Glide.with(context)
                    .load(ImageTmdbRequest.Person(tmdbId))
                    .placeholder(R.drawable.glide_placeholder_base)
                    .error(R.drawable.glide_placeholder_base)
                    .into(this)
            }
        }
    )
}


