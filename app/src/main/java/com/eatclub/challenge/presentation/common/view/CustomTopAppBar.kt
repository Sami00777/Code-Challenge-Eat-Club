package com.eatclub.challenge.presentation.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.eatclub.challenge.databinding.CustomTopAppBarBinding

class CustomTopAppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var binding: CustomTopAppBarBinding? = null

    init {
        binding = CustomTopAppBarBinding.inflate(LayoutInflater.from(context), this, true)
    }
}
