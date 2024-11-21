package com.example.muvitracker.ui.main.person

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.data.imagetmdb.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.FragmPersonBottomsheetBinding
import com.example.muvitracker.utils.calculateAge
import com.example.muvitracker.utils.dateFormatterInddMMMyyy
import com.example.muvitracker.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonFragment : BottomSheetDialogFragment(R.layout.fragm_person_bottomsheet) {

    private var currentPersonIds: Ids = Ids()
    private var currentCharacter: String = ""


    val viewmodel by viewModels<PersonViewmodel>()
    val binding by viewBinding(FragmPersonBottomsheetBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val bundle = arguments
        bundle?.let {
            currentPersonIds = bundle.getParcelable(PERSON_IDS_KEY) ?: Ids()
            currentCharacter = bundle.getString(CHARACTER_NAME_KEY) ?: ""
        }


        viewmodel.getPerson(currentPersonIds.trakt)
        viewmodel.personState.observe(viewLifecycleOwner) { personDto ->
            personDto?.let { personDto ->
                binding.personName.text = personDto.name

                binding.personAge.text =
                    calculateAge(personDto.birthday, personDto.death).toString() ?: "no information"

                binding.bornContent.text =
                    "${personDto.birthday?.dateFormatterInddMMMyyy()}\n${personDto.birthplace} " ?: "no information"



                if (personDto.death != null) {
                    binding.deathContent.text =
                        "${personDto.death.dateFormatterInddMMMyyy()}" // TODO test
                } else {
                    binding.deathContent.visibility = View.GONE
                    binding.deathTitle.visibility = View.GONE
                }

                binding.biographyContent.text = personDto.biography
            }
        }

        // the only element from who createst the Fragment
        binding.character.text = currentCharacter

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
        fun create(personIds: Ids, character: String): PersonFragment {
            val personFragment = PersonFragment()
            val bundle = Bundle()
            bundle.putParcelable(PERSON_IDS_KEY, personIds)
            bundle.putString(CHARACTER_NAME_KEY, character)
            personFragment.arguments = bundle
            return personFragment
        }

        // from search (person -> personExtended)
        fun create(personIds: Ids): PersonFragment {
            val personFragment = PersonFragment()
            val bundle = Bundle()
            bundle.putParcelable(PERSON_IDS_KEY, personIds)
            personFragment.arguments = bundle
            return personFragment
        }

        private const val PERSON_IDS_KEY = "person_ids_key"
        private const val CHARACTER_NAME_KEY = "character_ids_key"
    }
}