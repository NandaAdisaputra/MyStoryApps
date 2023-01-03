package com.nandaadisaputra.storyapp.ui.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nandaadisaputra.storyapp.R

class ForGlide {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["imageUrl"], requireAll = false)
        fun loadImage(view: ImageView, imageUrl: String?) {
            view.setImageDrawable(null)

            imageUrl?.let {
                Glide
                    .with(view.context)
                    .load(imageUrl)
                    .apply(RequestOptions.centerCropTransform())
                    .placeholder(com.crocodic.core.R.drawable.placeholder_img)
                    .error(R.drawable.ic_block)
                    .into(view)
            }
        }
    }
}