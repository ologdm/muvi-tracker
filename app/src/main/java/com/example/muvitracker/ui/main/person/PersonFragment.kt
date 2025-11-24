package com.example.muvitracker.ui.main.person

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.data.dto._support.Ids
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.FragmentPersonBinding
import com.example.muvitracker.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

// !!! similar to PersonBottomSheetFragment

@AndroidEntryPoint
class PersonFragment : Fragment(R.layout.fragment_person) {

    private var currentPersonIds: Ids = Ids()

    val viewmodel by viewModels<PersonViewmodel>()
    val binding by viewBinding(FragmentPersonBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // layout adaptation
        binding.buttonBack.setOnClickListener { requireActivity().onBackPressed() }
        binding.character.visibility = View.GONE


        val bundle = arguments
        bundle?.let {
            currentPersonIds = bundle.getParcelable(PERSON_IDS_KEY) ?: Ids()
        }

        // TODO wrap
        viewmodel.getPerson(currentPersonIds.trakt)
        viewmodel.personState.observe(viewLifecycleOwner) { person ->
            binding.personName.text = person.name
            binding.ageContent.text = person.age // calculated
            binding.knownContent.text = "for ${person.knownForDepartment }"

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

            binding.biographyContent.text = person.biography.ifBlank {
                getString(R.string.not_available)
            }
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
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.verticalImage)

        mainLayoutTopEdgeToEdgeManagement()
    }

    private fun mainLayoutTopEdgeToEdgeManagement() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // aggiorno solo lati che mi servono
            v.updatePadding(top = systemBars.top)
            insets
        }
    }




    companion object {
        // from search (personDto -> personExtended)
        fun create(personIds: Ids): PersonFragment {
            val personFragment = PersonFragment()
            val bundle = Bundle()
            bundle.putParcelable(PERSON_IDS_KEY, personIds)
            personFragment.arguments = bundle
            return personFragment
        }

        private const val PERSON_IDS_KEY = "person_ids_key"
    }
}