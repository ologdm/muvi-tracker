package com.example.muvitracker.ui.main.prefs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.data.dto.DetailDto


class PrefsAdapter : RecyclerView.Adapter<PrefsVH>() {


    private val adapterList = mutableListOf<DetailDto>()

    private var callbackVH: ((movieId: Int) -> Unit)? = null
    private var callbackLiked: ((dto: DetailDto) -> Unit)? = null
    private var callbackWatched: ((dto: DetailDto) -> Unit)? = null

    // ADAPTER METHODS
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefsVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.vh_prefs, parent, false)
        return PrefsVH(view)
    }


    override fun getItemCount(): Int {
        return adapterList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PrefsVH, position: Int) {
        var onBindDto = adapterList.get(position)

        val imageView: ImageView = holder.itemView.findViewById(R.id.image)
        var titleVH: TextView = holder.itemView.findViewById(R.id.title)
        var yearVH: TextView = holder.itemView.findViewById(R.id.year)

        val watchedCkBox: CheckBox = holder.itemView.findViewById(R.id.watchedCheckBox)
        val likedButton: ImageButton = holder.itemView.findViewById(R.id.likedButton)

        titleVH.text = onBindDto.title
        yearVH.text = onBindDto.year.toString()

        Glide
            .with(holder.itemView.context)
            .load(onBindDto.imageUrl())
            .into(imageView)

        // icons
        val context: Context = holder.itemView.context
        val iconFilled = context.getDrawable(R.drawable.baseline_liked)
        val iconEmpty = context.getDrawable(R.drawable.baseline_liked_border)

        updateFavoriteIcon(
            likedButton,
            onBindDto.liked,
            iconFilled,
            iconEmpty
        )

        // clickVH -> apri details
        holder.itemView.setOnClickListener {
            callbackVH?.invoke(onBindDto.ids.trakt)
        }

        // implemento comportamento al click button
        likedButton.setOnClickListener {
            callbackLiked?.invoke(onBindDto)
            // copia nellla repo

            updateFavoriteIcon(likedButton, onBindDto.liked, iconFilled, iconEmpty)
        }

        // set dto, passa dto a db, set checkbox
        watchedCkBox.setOnCheckedChangeListener { b, isChecked ->
            if (onBindDto.watched != watchedCkBox.isChecked) {
                onBindDto = onBindDto.copy(watched = isChecked)
                // serve copiare??
                callbackWatched?.invoke(onBindDto)
                watchedCkBox.isChecked = onBindDto.watched
            }
        }
        watchedCkBox.isChecked = onBindDto.watched
    }


    // UPDATE FUNCTIONS
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(inputList: List<DetailDto>) {
        adapterList.clear()
        adapterList.addAll(inputList)
        notifyDataSetChanged()
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


    // SET LAMBDA
    fun setCallbackVH(call: (movieId: Int) -> Unit) {
        callbackVH = call
    }

    fun setCallbackLiked(call: (dtoToToggle: DetailDto) -> Unit) {
        callbackLiked = call
    }

    fun setCallbackWatched(call: (updatedDto: DetailDto) -> Unit) {
        callbackWatched = call
    }

}


class PrefsVH(
     val itemView: View
) : RecyclerView.ViewHolder(itemView) {
}