package com.example.muvitracker.ui.main.person

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.FragmPersonBinding
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonBottomSheetFragment : BottomSheetDialogFragment(R.layout.fragment_person) {

    private var currentPersonIds: Ids = Ids()
    private var currentCharacter: String = ""

    val viewmodel by viewModels<PersonViewmodel>()
    val binding by viewBinding(FragmPersonBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // layout adaptation
        binding.buttonBackLayout.visibility = View.GONE

        val bundle = arguments
        bundle?.let {
            currentPersonIds = bundle.getParcelable(PERSON_IDS_KEY) ?: Ids()
            currentCharacter = bundle.getString(CHARACTER_NAME_KEY) ?: ""
        }

        binding.character.text = currentCharacter // the only element from who create the Fragment

        viewmodel.getPerson(currentPersonIds.trakt)
        viewmodel.personState.observe(viewLifecycleOwner) { person ->
            binding.personName.text = person.name
            binding.known.text = "for ${person.knownForDepartment }"
            binding.personAge.text = person.age // calculated

            binding.bornContent.text = "${person.birthday}\n${person.birthplace}"

            if (person.death.isNotBlank()) {
                binding.deathContent.text = person.death
                // default visible
            } else {
                binding.deathContent.visibility = View.GONE
                binding.deathTitle.visibility = View.GONE
            }

            if (person.death.isNotBlank()) {
                binding.deathContent.text = person.death
            } else {
                binding.deathContent.visibility = View.GONE
                binding.deathTitle.visibility = View.GONE
            }

            binding.biographyContent.text = person.biography.ifBlank { "N/A" }
        }


        var isTextExpanded = false
        binding.biographyContent.setOnClickListener {
            if (isTextExpanded) {
                binding.biographyContent.maxLines = 5
                binding.biographyContent.ellipsize = TextUtils.TruncateAt.END
            } else {
                binding.biographyContent.maxLines = Int.MAX_VALUE
                binding.biographyContent.ellipsize = null
            }
            isTextExpanded = !isTextExpanded
        }

        Glide.with(requireContext())
            .load(ImageTmdbRequest.Person(currentPersonIds.tmdb))
            .into(binding.imageVertical)
    }


    companion object {
        // from movie, show (castMember -> personExtended)
        fun create(personIds: Ids, character: String): PersonBottomSheetFragment {
            val personFragment = PersonBottomSheetFragment()
            val bundle = Bundle()
            bundle.putParcelable(PERSON_IDS_KEY, personIds)
            bundle.putString(CHARACTER_NAME_KEY, character)
            personFragment.arguments = bundle
            return personFragment
        }

        private const val PERSON_IDS_KEY = "person_ids_key"
        private const val CHARACTER_NAME_KEY = "character_ids_key"
    }
}