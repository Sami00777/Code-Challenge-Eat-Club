package com.eatclub.challenge.presentation.common.view

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.eatclub.challenge.R

class CustomCircularProgressDrawable(context: Context) : CircularProgressDrawable(context) {
    init {
        val color = R.color.gray
        setColorSchemeColors(color)
        strokeWidth = 30f
        centerRadius = 60f
        alpha = 250
        setStartEndTrim(0f, 0.85f)
        start()
    }
}
