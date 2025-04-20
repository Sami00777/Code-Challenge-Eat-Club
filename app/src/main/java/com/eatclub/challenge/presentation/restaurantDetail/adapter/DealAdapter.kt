package com.eatclub.challenge.presentation.restaurantDetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.eatclub.challenge.R
import com.eatclub.challenge.data.api.model.Deal
import com.eatclub.challenge.databinding.DealItemLayoutBinding

class DealAdapter(
    private val deals: List<Deal>,
) : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {
    inner class DealViewHolder(private val binding: DealItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(deal: Deal) {
            binding.apply {
                val openTime = deal.opening ?: deal.start
                val close = deal.closing ?: deal.end
                val availableHours =
                    if (!openTime.isNullOrBlank() && !close.isNullOrBlank()) {
                        root.context.getString(R.string.between_placeholder, openTime, close)
                    } else {
                        root.context.getString(R.string.deal_subtitle_anytime)
                    }
                val consumptionType =
                    if (deal.dineIn == true) {
                        root.context.getString(R.string.dine_in_suffix)
                    } else {
                        root.context.getString(R.string.take_away_suffix)
                    }
                val discount = root.context.getString(R.string.placeholder_off, deal.discount?.toString() ?: "0")
                val discountAndConsumptionType = root.context.getString(R.string.discount_with_offer_type, discount, consumptionType)

                imgLightning.isVisible = deal.lightning == true
                textHours.text = availableHours
                textViewOffer.text = discountAndConsumptionType
                textDealsLeft.text = textDealsLeft.context.getString(R.string.placeholder_deals_left, deal.qtyLeft ?: "0")
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DealViewHolder {
        val binding = DealItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DealViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DealViewHolder,
        position: Int,
    ) {
        holder.bind(deals[position])
    }

    override fun getItemCount(): Int = deals.size
}
