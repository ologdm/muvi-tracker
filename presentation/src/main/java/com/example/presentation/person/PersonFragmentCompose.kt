package com.example.presentation.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.domain.model.Ids
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
        viewmodel.loadPersonCastCredits(currentPersonIds)

        val composeView = ComposeView(requireContext())

        // TODO: check!! , funzionalità aggiuntiva
        composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        composeView.setContent {
            val personState = viewmodel.personState.collectAsState().value
            val creditsState = viewmodel.personCastCredits.collectAsState().value
            Scaffold {
                PersonScreen(
                    modifier = Modifier.padding(it),
                    personState = personState,
                    creditsState = creditsState,
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

