package com.eatclub.challenge.presentation.restaurantDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.eatclub.challenge.R
import com.eatclub.challenge.data.api.model.Deal
import com.eatclub.challenge.data.api.model.Restaurant
import com.eatclub.challenge.databinding.FragmentRestaurantDetailBinding
import com.eatclub.challenge.presentation.common.extension.ViewExtension.glideImage
import com.eatclub.challenge.presentation.restaurantDetail.adapter.DealAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantDetailFragment : Fragment() {
    private var _binding: FragmentRestaurantDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RestaurantDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRestaurantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.restaurantDetails.observe(viewLifecycleOwner) { restaurant ->
            restaurant?.let {
                displayRestaurantDetails(it)
            }
        }
    }

    private fun displayRestaurantDetails(restaurant: Restaurant) {
        val location = "${restaurant.address.orEmpty()}, ${restaurant.suburb.orEmpty()}"
        binding.apply {
            txtViewRestaurantName.text = restaurant.name.orEmpty()
            txtViewRestaurantType.text = restaurant.cuisines?.joinToString(" ● ").orEmpty()
            txtViewHours.text = getString(R.string.hours, restaurant.opening.orEmpty(), restaurant.closing.orEmpty())
            txtViewLocation.text = location
            restaurantImageView.glideImage(restaurant.imageLink.orEmpty())
            setupRecyclerView(restaurant.deals ?: emptyList())
        }
    }

    private fun setupRecyclerView(deals: List<Deal>) {
        binding.recyclerViewDeals.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = DealAdapter(deals)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
