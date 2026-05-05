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

// NOTES: per search

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

