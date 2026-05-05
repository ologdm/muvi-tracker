//package com.example.presentation.person
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.ComposeView
//import androidx.compose.ui.platform.ViewCompositionStrategy
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.fragment.app.viewModels
//import com.example.domain.model.Ids
//import com.example.domain.model.Person
//import com.example.presentation.R
//import com.example.presentation.databinding.FragmentPersonBinding
//import com.example.presentation.person.xml.PersonBottomSheetFragmentXml
//import com.example.presentation.utils.fragmentViewLifecycleScope
//import com.example.presentation.utils.viewBinding
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.launch
//
//@AndroidEntryPoint
//class PersonBottomSheetInCompose : BottomSheetDialogFragment(R.layout.fragment_person) {
//
//    private var currentPersonIds: Ids = Ids()
//    private var currentCharacter: String = ""
//
//    val viewmodel by viewModels<PersonViewmodel>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        // TODO OK
//        val bundle = arguments
//        bundle?.let {
//            currentPersonIds = bundle.getParcelable(PERSON_IDS_KEY) ?: Ids()
//            currentCharacter = bundle.getString(CHARACTER_NAME_KEY) ?: ""
//        }
//
//        // TODO OK
//        viewmodel.loadPersonDetail(currentPersonIds)
//
//        // TODO OK
//        val composeView =
//            ComposeView(requireContext()).apply {
//
//                setViewCompositionStrategy(
//                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//                )
//
//                setContent {
//                    val state = viewmodel.personState.collectAsState().value
////                    PersonScreen(
////                        state = state,
////                        character = currentCharacter,
////                        isBottomSheet = true
////                    )
//                    SimpleScreen()
//
//                }
//            }
//
//        return composeView
//    }
//
//
////    override fun onStart() {
////        super.onStart()
////
////        val dialog = dialog as? BottomSheetDialog ?: return
////        val bottomSheet =
////            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
////                ?: return
////
////        // Impostiamo l'altezza a MATCH_PARENT per permettere al behavior di gestire
////        // correttamente gli stati intermedi (come HALF_EXPANDED) indipendentemente dal contenuto.
////        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
////
////        val behavior = BottomSheetBehavior.from(bottomSheet)
////
////        behavior.apply {
////            // Disabilitando fitToContents permettiamo al bottom sheet di fermarsi a metà
////            isFitToContents = false
////
////            // Rapporto per lo stato intermedio
////            halfExpandedRatio = 0.5f
////
////            // Altezza iniziale visibile (50% dello schermo)
////            peekHeight = (resources.displayMetrics.heightPixels * 0.5).toInt()
////
////            // Impostiamo lo stato subito e anche nel post per sicurezza
////            state = BottomSheetBehavior.STATE_HALF_EXPANDED
////            bottomSheet.post {
////                state = BottomSheetBehavior.STATE_HALF_EXPANDED
////            }
////        }
////    }
////    override fun onStart() {
////        super.onStart()
////
////        val bottomSheet = dialog?.findViewById<View>(
////            com.google.android.material.R.id.design_bottom_sheet
////        ) ?: return
////
////        val behavior = BottomSheetBehavior.from(bottomSheet)
////
////        behavior.apply {
////            state = BottomSheetBehavior.STATE_COLLAPSED
////            peekHeight = 400 // 👈 apertura iniziale parziale
////            isFitToContents = false
////            expandedOffset = 0 // 👈 fullscreen quando espanso
////        }
////    }
//
//
//    // TODO OK
//    companion object {
//
//        fun create(personIds: Ids, character: String): PersonBottomSheetInCompose {
//            val personFragment = PersonBottomSheetInCompose()
//            val bundle = Bundle()
//            bundle.putParcelable(PERSON_IDS_KEY, personIds)
//            bundle.putString(CHARACTER_NAME_KEY, character)
//            personFragment.arguments = bundle
//            return personFragment
//        }
//
//        private const val PERSON_IDS_KEY = "person_ids_key"
//        private const val CHARACTER_NAME_KEY = "character_ids_key"
//    }
//}
//
//
//@Composable
//fun SimpleScreen(
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(20.dp)
//            .background(Color.Blue)
//            .border(2.dp, Color.Red)
//    ) { }
//}
//
//@Preview
//@Composable
//fun PreviewSimpleScreen(modifier: Modifier = Modifier) {
//    SimpleScreen()
//}
//
//
