package com.example.muvitracker.ui.main.allmovies.base

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.muvitracker.R
import com.example.muvitracker.databinding.VhBaseListBinding
import com.example.muvitracker.domain.model.base.Movie

class MovieVH(
    val binding: VhBaseListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.title.text = "${movie.title} (${movie.year})"

        Glide.with(binding.root.context)
            .load(movie.imageUrl())
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .placeholder(R.drawable.glide_placeholder_base)
            .error(R.drawable.glide_placeholder_base)
            .into(binding.image)
    }
}


    // con tmdb - test
//    val retrofit = Retrofit.Builder()
//        .baseUrl("https://api.themoviedb.org/3/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//    val tmdbApi = retrofit.create(TMDbApi::class.java
//    )
//    val tmdbRepo = TmdbRepository(tmdbApi)
//
//
//
//    fun bind(movie: Movie) {
//        binding.title.text = "${movie.title} (${movie.year})"
//
//        CoroutineScope(Dispatchers.Main).launch {
//            val posterUrl = tmdbRepo.getMoviePoster(movie.ids.tmdb)
//            Glide.with(binding.root.context)
//                .load(tmdbRepo.getMoviePoster(movie.ids.tmdb))
//                .transition(DrawableTransitionOptions.withCrossFade(500))
//                .placeholder(R.drawable.glide_placeholder_base)
//                .error(R.drawable.glide_placeholder_base)
//                .into(binding.image)
//        }
//    }








