package com.example.muvitracker.inkotlin.ui.main.prefs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvitracker.R
import com.example.muvitracker.inkotlin.data.dto.DetaDto

/**
 *
 * updateList()
 * updateFavoriteIcon()
 *
 * callbackVH
 * callbackLiked
 * callbackWatched
 */


class PrefsAdapter : RecyclerView.Adapter<PrefsVH>() {

    // lista
    private val adapterList = mutableListOf<DetaDto>()

    // callbacks necessarie
    private var callbackVH: ((movieId: Int) -> Unit)? = null
    private var callbackLiked: ((dto: DetaDto) -> Unit)? = null
    private var callbackWatched: ((dto: DetaDto) -> Unit)? = null

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

        val imageView: ImageView = holder.itemView.findViewById(R.id.imageVH)
        var titleVH: TextView = holder.itemView.findViewById(R.id.titleVH)
        var yearVH: TextView = holder.itemView.findViewById(R.id.yearVH)

        val watchedCkBox: CheckBox = holder.itemView.findViewById(R.id.checkBox)
        val likedButton: ImageButton = holder.itemView.findViewById(R.id.likedButton)

        titleVH.text = onBindDto.title
        yearVH.text = onBindDto.year.toString()

        Glide
            .with(holder.itemView.context)
            .load(onBindDto.getImageUrl())
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
    fun updateList(inputList: List<DetaDto>) {
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

    fun setCallbackLiked(call: (dtoToToggle: DetaDto) -> Unit) {
        callbackLiked = call
    }

    fun setCallbackWatched(call: (updatedDto: DetaDto) -> Unit) {
        callbackWatched = call
    }

}