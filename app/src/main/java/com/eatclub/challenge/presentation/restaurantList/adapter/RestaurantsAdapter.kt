package com.eatclub.challenge.presentation.restaurantList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eatclub.challenge.R
import com.eatclub.challenge.data.api.model.Deal
import com.eatclub.challenge.data.api.model.Restaurant
import com.eatclub.challenge.databinding.RestaurantItemLayoutBinding
import com.eatclub.challenge.presentation.common.extension.ViewExtension.glideImage

class RestaurantsAdapter() : ListAdapter<Restaurant, RestaurantsAdapter.RestaurantViewHolder>(DiffCallback) {
    var onRestaurantClick: (Restaurant) -> Unit = {}

    object DiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(
            oldItem: Restaurant,
            newItem: Restaurant,
        ) = oldItem.objectId == newItem.objectId

        override fun areContentsTheSame(
            oldItem: Restaurant,
            newItem: Restaurant,
        ) = oldItem == newItem
    }

    inner class RestaurantViewHolder(private val binding: RestaurantItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) =
            with(binding) {
                val restaurantLocation = restaurant.suburb ?: txtRestaurantLocation.context.getString(R.string.unknown_location)
                val firstDeal = restaurant.deals?.getOrNull(0)
                val secondDeal = restaurant.deals?.getOrNull(1)
                val formattedDealOffers = formatDealOptions(restaurant.deals.orEmpty(), root.context)
                val restaurantType = formatCuisines(restaurant.cuisines)

                txtRestaurantName.text = restaurant.name.orEmpty()
                txtRestaurantType.text = restaurantType
                txtRestaurantLocation.text = restaurantLocation
                txtRestaurantOrderOption.text = formattedDealOffers
                imageView.glideImage(restaurant.imageLink)
                dealViewTop.isVisible = firstDeal != null
                dealViewTop.setUpDealView(firstDeal)
                dealViewBottom.isVisible = secondDeal != null
                dealViewBottom.setUpDealView(secondDeal)

                root.setOnClickListener {
                    onRestaurantClick(restaurant)
                }
            }
    }

    private fun formatCuisines(cuisineTypes: List<String?>?): String {
        return cuisineTypes
            ?.filterNotNull()
            ?.joinToString(", ")
            .orEmpty()
    }

    private fun formatDealOptions(
        dealOffers: List<Deal>,
        context: Context,
    ): String {
        val hasDineIn = dealOffers.any { it.dineIn == true }
        val hasTakeAway = dealOffers.any { it.dineIn == false }
        return when {
            hasDineIn && hasTakeAway -> context.getString(R.string.dine_in_and_take_away)
            hasDineIn -> context.getString(R.string.dine_in)
            hasTakeAway -> context.getString(R.string.takeaway)
            else -> context.getString(R.string.no_deals_available)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RestaurantViewHolder {
        val binding = RestaurantItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RestaurantViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
