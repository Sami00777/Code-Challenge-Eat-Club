package com.eatclub.challenge.presentation.common.extension

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eatclub.challenge.R
import com.eatclub.challenge.presentation.common.view.CustomCircularProgressDrawable

object ViewExtension {
    fun ImageView.glideImage(url: String?) {
        val customProgressDrawable = CustomCircularProgressDrawable(this.context)
        Glide.with(this.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .placeholder(customProgressDrawable)
            .error(R.drawable.error_placeholder)
            .into(this)
    }

    fun View.visibleIf(condition: Boolean) {
        isVisible = condition
    }
}
