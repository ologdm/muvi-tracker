//package com.example.muvitracker.inkotlin.ui.main.allmovies.base
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.example.muvitracker.R
//import com.example.muvitracker.databinding.VhMovieBinding
//import com.example.muvitracker.inkotlin.domain.model.MovieItem
//
//
///**
// * refactoring:
// *      si utilizzerà su PopuFragment e BoxoFragment
// *      <MovieVh> unico
// *      MovieModel unico
// *
// */
//
///* TODO STEP 10
// * ListAdapter - estensione di RV.Adapter
// * bind - su VH
// * onCLickVH lambda - nel costruttore
// *
// */
//
//
//class MovieAdapter : RecyclerView.Adapter<MovieVh>() {
//
//
//    private val adapterList = mutableListOf<MovieItem>()
////    private val adapterListTest = mutableListOf<BoxoDto>()
//
//
//    /* variante 2 boxo
//     * private var adapterList = listOf<PopularDtoK>()
//     */
//
//    private var callbackVH: ((Int) -> Unit)? = null
//
//
//    // METODI
//    // 1. OK
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVh {
//        // definisco il mio binding
//        val binding = VhMovieBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return MovieVh(binding)
//    }
//
//
//    // 2. OK
//    override fun getItemCount(): Int {
//        return adapterList.size
//    }
//
//
//    // 3. OK
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: MovieVh, position: Int) {
//
//        val dto: MovieItem = adapterList[position]
//
//        with(holder.binding) {
//            //titleVH.text = "${dto.title} (${dto.year.toString()})"
//            // titleVH.text = getString(R.string.title_with_year, dto.title, dto.year)
//            titleVH.text = "${dto.title} (${dto.year})"
//
//            Glide.with(root.context)
//                .load(dto.getImageUrl())
//                .transition(DrawableTransitionOptions.withCrossFade(500))
//                .placeholder(R.drawable.glide_placeholder_base)
//                .error(R.drawable.glide_placeholder_base)
//                .into(imageVH)
//
//
//            root.setOnClickListener { // callback view - implementazione, chiamata al click
//                callbackVH?.invoke(dto.ids.trakt) // callbackVH - chiamata, implementazione su fragment
//            }
//        }
//    }
//
//
//
//    fun updateList(inputList: List<MovieItem>) {
//        adapterList.clear()
//        adapterList.addAll(inputList)
//        notifyDataSetChanged()
//    }
//
//
//
//    fun setCallbackVH(call: (Int) -> Unit) {
//        this.callbackVH = call
//    }
//
//}
//
//
//
//
//
//
//
