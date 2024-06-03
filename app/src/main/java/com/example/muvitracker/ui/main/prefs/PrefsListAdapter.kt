package com.example.muvitracker.ui.main.prefs

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.DetailDto
import com.example.muvitracker.databinding.VhPrefsBinding


class PrefsListAdapter(
    private val onClickVH: (movieId:Int) -> Unit,
    private val onCLickLiked: (DetailDto) -> Unit,
    private val onClickWatched: (DetailDto) -> Unit
) : ListAdapter<DetailDto, PrefsVHnuovo>(PrefsListAdapter) {

    companion object : DiffUtil.ItemCallback<DetailDto>() {
        override fun areItemsTheSame(oldItem: DetailDto, newItem: DetailDto): Boolean {
            return oldItem.ids.trakt == oldItem.ids.trakt
        }

        override fun areContentsTheSame(oldItem: DetailDto, newItem: DetailDto): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefsVHnuovo {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VhPrefsBinding.inflate(layoutInflater, parent, false)
        return PrefsVHnuovo(binding)
    }


    override fun onBindViewHolder(holder: PrefsVHnuovo, position: Int) {
        var dto = getItem(position)


        holder.binding.run {
            title.text = dto.title
            year.text = dto.year.toString()

            Glide
                .with(holder.itemView.context)
                .load(dto.imageUrl())
                .into(image)

            // icons
            val context: Context = holder.itemView.context
            val iconFilled = context.getDrawable(R.drawable.baseline_liked)
            val iconEmpty = context.getDrawable(R.drawable.baseline_liked_border)

            updateFavoriteIcon(
                likedButton,
                dto.liked,
                iconFilled,
                iconEmpty
            )

            // clickVH -> apri details
            holder.itemView.setOnClickListener {
                onClickVH.invoke(dto.ids.trakt)
            }


            // implemento comportamento al click button
            likedButton.setOnClickListener {
                onCLickLiked.invoke(dto)
                // copia nellla repo

                updateFavoriteIcon(likedButton, dto.liked, iconFilled, iconEmpty)
            }

            // set dto, passa dto a db, set checkbox
            checkBox.setOnCheckedChangeListener { b, isChecked ->
                if (dto.watched != checkBox.isChecked) {
                    dto = dto.copy(watched = isChecked)
                    // serve copiare??
                    onClickWatched.invoke(dto)
                    checkBox.isChecked = dto.watched
                }
            }
            checkBox.isChecked = dto.watched
        }
    }


    fun updateFavoriteIcon(
        likedButton: ImageButton,
        isLiked: Boolean,
        iconFilled: Drawable?,
        iconEmpty: Drawable?
    ) {
        if (isLiked)
            likedButton.setImageDrawable(iconFilled)
        else
            likedButton.setImageDrawable(iconEmpty)
    }
}


    //    private var callbackVH: ((movieId: Int) -> Unit)? = null
//    private var callbackLiked: ((dto: DetailDto) -> Unit)? = null
//    private var callbackWatched: ((dto: DetailDto) -> Unit)? = null


    // SET LAMBDA
//    fun setCallbackVH(call: (movieId: Int) -> Unit) {
//        callbackVH = call
//    }
//
//    fun setCallbackLiked(call: (dtoToToggle: DetailDto) -> Unit) {
//        callbackLiked = call
//    }
//
//    fun setCallbackWatched(call: (updatedDto: DetailDto) -> Unit) {
//        callbackWatched = call
//    }


//        val imageView: ImageView = holder.itemView.findViewById(R.id.imageVH)
//        var titleVH: TextView = holder.itemView.findViewById(R.id.titleVH)
//        var yearVH: TextView = holder.itemView.findViewById(R.id.yearVH)
//        val watchedCkBox: CheckBox = holder.itemView.findViewById(R.id.checkBox)
//        val likedButton: ImageButton = holder.itemView.findViewById(R.id.likedButton)
