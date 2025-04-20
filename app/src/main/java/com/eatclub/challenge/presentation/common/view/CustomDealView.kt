package com.eatclub.challenge.presentation.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.eatclub.challenge.R
import com.eatclub.challenge.data.api.model.Deal
import com.eatclub.challenge.databinding.CustomDealViewBinding

class CustomDealView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var binding: CustomDealViewBinding? = null

    init {
        binding = CustomDealViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setUpDealView(deal: Deal?) {
        if (deal == null) {
            binding?.root?.visibility = GONE
            return
        }

        val dineInSuffix =
            if (deal.dineIn == true) {
                context.getString(R.string.dine_in_suffix)
            } else {
                context.getString(R.string.take_away_suffix)
            }

        val title =
            context.getString(
                R.string.deal_title,
                deal.discount,
                dineInSuffix,
            )

        val subtitle =
            if (deal.start.isNullOrBlank() && deal.end.isNullOrBlank()) {
                context.getString(R.string.deal_subtitle_anytime)
            } else {
                context.getString(
                    R.string.deal_subtitle_range,
                    deal.start.orEmpty(),
                )
            }

        binding?.imgLightning?.isVisible = deal.lightning == true
        binding?.txtDealTitle?.text = title
        binding?.txtDealSubtitle?.text = subtitle
        binding?.root?.visibility = VISIBLE
    }
}
