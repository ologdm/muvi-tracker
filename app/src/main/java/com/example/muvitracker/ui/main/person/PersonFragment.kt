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
import com.example.muvitracker.utils.orDefaultText
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


        personLoadingSetup()
        loadImagesWithCustomTmdbGlide()

        mainLayoutTopEdgeToEdgeManagement()
    }


    private fun personLoadingSetup() {

        viewmodel.getPersonDetail(currentPersonIds)

        viewmodel.personState.observe(viewLifecycleOwner) { person ->
            // ok 1.1.3 ok
            binding.personName.text = person.name
                .orDefaultText(getString(R.string.unknown_name))

            // calculated, return null or age ok 1.1.3 ok
            binding.ageContent.text = person.age.toString()
                .orDefaultText(getString(R.string.unknown_age))

            // 1.1.3 ok
            binding.knownContent.text = person.knownForDepartment
                .orDefaultText(getString(R.string.unknown))

            // 1.1.3 ok
            binding.bornContent.text = "${person.birthday
                .orDefaultText(getString(R.string.unknown_birthday))}" +
                    "\n${person.birthplace
                        .orDefaultText(getString(R.string.unknown_birthplace))}"

            // 1.1.3 ok
            if (person.death.isNullOrBlank()) {
                binding.deathContent.visibility = View.GONE
                binding.deathTitle.visibility = View.GONE
            } else {
                binding.deathContent.text = person.death
                // default visible
            }

            // 1.1.3 ok
            binding.biographyContent.text = person.biography
                .orDefaultText(getString(R.string.not_available))
        }
        expandBiographySetup()
    }

    private fun expandBiographySetup() {
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
    }


    private fun loadImagesWithCustomTmdbGlide() {
        Glide.with(requireContext())
            .load(ImageTmdbRequest.Person(currentPersonIds.tmdb))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.verticalImage)
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