package com.eatclub.challenge.presentation.restaurantList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eatclub.challenge.R
import com.eatclub.challenge.data.api.model.Restaurant
import com.eatclub.challenge.databinding.FragmentRestaurantListBinding
import com.eatclub.challenge.domain.common.state.ErrorType
import com.eatclub.challenge.domain.common.state.NetworkRequestState
import com.eatclub.challenge.presentation.common.extension.ViewExtension.visibleIf
import com.eatclub.challenge.presentation.common.helper.AlertDialogHelper
import com.eatclub.challenge.presentation.restaurantList.adapter.RestaurantsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {
    private var _binding: FragmentRestaurantListBinding? = null
    private val binding get() = _binding!!

    private val restaurantsAdapter by lazy { RestaurantsAdapter() }
    private val viewModel: RestaurantListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRestaurantListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setUpViewModelObserver()
        setUpViews()
        onBackPressedSetup()
    }

    private fun setUpViewModelObserver()  {
        viewModel.filteredRestaurants.observe(viewLifecycleOwner, this::handleState)
    }

    private fun setUpViews() {
        binding.editTextSearch.addTextChangedListener { editable ->
            editable?.toString()?.let { query ->
                val trimmedQuery = query.trim()
                if (trimmedQuery.length >= 2) {
                    viewModel.filterRestaurants(trimmedQuery)
                } else if (trimmedQuery.isEmpty()) {
                    viewModel.resetFilter()
                }
            }
        }
        binding.buttonRetry.setOnClickListener {
            viewModel.getRestaurantList()
        }
    }

    private fun handleState(state: NetworkRequestState<List<Restaurant>>) {
        binding.txtViewError.visibleIf(state is NetworkRequestState.Error)
        binding.recyclerView.visibleIf(state is NetworkRequestState.Success)
        binding.buttonRetry.visibleIf(state is NetworkRequestState.Error)
        binding.progressBar.visibleIf(state is NetworkRequestState.Loading)
        when (state) {
            is NetworkRequestState.Loading -> {}
            is NetworkRequestState.Success -> {
                restaurantsAdapter.submitList(state.data)
            }
            is NetworkRequestState.Error -> {
                handleErrorState(state.error)
            }
        }
    }

    private fun handleErrorState(type: ErrorType) {
        restaurantsAdapter.submitList(emptyList())
        binding.buttonRetry.visibleIf(type is ErrorType.UnavailableNetwork || type is ErrorType.NetworkError)
        when (type) {
            is ErrorType.EmptyResult -> {
                binding.txtViewError.text = type.message
            }
            is ErrorType.NetworkError -> {
                binding.txtViewError.text = type.exception.message
            }
            is ErrorType.GeneralError -> {
                binding.txtViewError.text = type.exception.message
            }
            is ErrorType.UnavailableNetwork -> {
                binding.txtViewError.text = type.message
            }
        }
    }

    private fun setupRecyclerView() {
        restaurantsAdapter.onRestaurantClick = { restaurant ->
            navigate(
                RestaurantListFragmentDirections.actionRestaurantListFragmentToRestaurantDetailFragment(
                    restaurant,
                ),
            )
        }

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = restaurantsAdapter
        }
    }

    fun navigate(destination: NavDirections) =
        with(findNavController()) {
            currentDestination?.getAction(destination.actionId)
                ?.let { navigate(destination) }
        }

    private fun onBackPressedSetup() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showExitConfirmation()
                }
            },
        )
    }

    private fun showExitConfirmation() {
        AlertDialogHelper(requireContext()).show(
            title = getString(R.string.exit_app),
            message = getString(R.string.are_you_sure_you_want_to_exit),
            onPositiveButtonClick = { requireActivity().finish() },
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }
}
