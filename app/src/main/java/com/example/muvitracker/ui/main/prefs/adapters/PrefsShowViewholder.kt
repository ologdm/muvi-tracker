package com.example.muvitracker.ui.main.prefs.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.data.glide.ImageTmdbRequest
import com.example.muvitracker.databinding.ViewholderPrefsShowBinding
import com.example.muvitracker.domain.model.Show
import com.example.muvitracker.ui.main.detailshow.DetailShowFragment.ShowDefaults
import com.example.muvitracker.utils.orDefaultText
import java.time.LocalDate

class PrefsShowViewholder(
    val binding: ViewholderPrefsShowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(show: Show) {
        val ctx = itemView.context

        binding.run {
            title.text = show.title.orDefaultText(ShowDefaults.TITLE)

            // 1°
            val networkList = show.networks.filter { it.isNotBlank() }
            val networkText =
                if (networkList.isNullOrEmpty()) {
                    ShowDefaults.NETWORK
                } else {
                    networkList.joinToString(", ")
                }

            // 3°
            val countryList = show.countries.filter { it.isNotBlank() }
            val countryText = if (countryList.isNullOrEmpty()){
                ShowDefaults.COUNTRY
            } else {
                "${countryList.joinToString(", ")}"
            }

            // 2°
            val currentYear = LocalDate.now().year
            val firstYear: Int? = show.year // 2019
            val lastYear: Int? = show.lastAirDate // 2019-05-19
                ?.takeIf { it.length >= 4 } // if() return this, or null - stringa < 4
                ?.substring(0, 4)
                ?.toIntOrNull()

            val yearText = when {
                firstYear != null &&
                        lastYear != null &&
                        lastYear > firstYear &&
                        lastYear != currentYear -> "$firstYear - $lastYear"

                firstYear != null -> ctx.getString(R.string.from_release_year, "$firstYear")

                else -> ShowDefaults.YEAR
            }

            // 4°
            val statusText = show.status?.replaceFirstChar { it.uppercaseChar() } // 1.1.3 OK
                .orDefaultText(ShowDefaults.STATUS)  // es released


            otherInfo.text = "$yearText  |  $networkText  |  $countryText  |  $statusText"
//                "${show.networks.joinToString(", ")}, ${show.year}, ${show.countries.joinToString(", ").uppercase()}, ${show.status}"

            Glide
                .with(binding.root.context)
                .load(ImageTmdbRequest.ShowVertical(show.ids.tmdb))
                .placeholder(R.drawable.glide_placeholder_base)
                .error(R.drawable.glide_placeholder_base)
                .into(image)

            // update watched states
            watchedCounterProgressBar.max = show.airedEpisodes
            watchedCounterProgressBar.progress = show.watchedCount
            watchedCounterTextview.text = "${show.watchedCount}/${show.airedEpisodes}"

        }
    }
}