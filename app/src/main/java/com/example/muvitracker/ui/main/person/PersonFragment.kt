package com.example.muvitracker.ui.main.person

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.base.Ids
import com.example.muvitracker.databinding.FragmPersonBottomsheetBinding
import com.example.muvitracker.utils.calculateAge
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
            personDto?.let {
                binding.personName.text = it.name

                binding.personAge.text =
                    it.birthday?.let { bDay -> calculateAge(bDay).toString() }

                binding.personBorn.text = "${it.birthday}\n${it.birthplace} "

                if (it.death != null) {
                    binding.personDeath.text = "${it.death}" // TODO test
                } else {
                    binding.personDeath.visibility = View.GONE
                    binding.deathTitle.visibility = View.GONE
                }

                binding.biography.text = it.biography
            }
        }

        binding.character.text = currentCharacter

    }


    companion object {
        fun create(personIds: Ids, character: String): PersonFragment {
            val personFragment = PersonFragment()
            val bundle = Bundle()
            bundle.putParcelable(PERSON_IDS_KEY, personIds)
            bundle.putString(CHARACTER_NAME_KEY, character)
            personFragment.arguments = bundle
            return personFragment
        }

        private const val PERSON_IDS_KEY = "person_ids_key"
        private const val CHARACTER_NAME_KEY = "chracter_ids_key"
    }
}