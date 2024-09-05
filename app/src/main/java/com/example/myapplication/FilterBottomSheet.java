package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.dataType.RestaurantFilterModel;
import com.example.myapplication.databinding.BottomSheetFilterBinding;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.chip.Chip;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FilterBottomSheet extends BottomSheetDialogFragment implements LifecycleObserver {

    private BottomSheetFilterBinding binding;
    private FilterViewModel viewModel;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private BadgeDrawable badgeDrawable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout with data binding
        binding = BottomSheetFilterBinding.inflate(inflater, container, false);

        // Create and bind the ViewModel
        viewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        binding.setViewModel(viewModel);

        // Set the lifecycle owner so that LiveData can update the UI automatically
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Add lifecycle observer
        getViewLifecycleOwner().getLifecycle().addObserver(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
                bottomSheetBehavior.setHideable(false);
                View bottomSheetParent = binding.bottomSheetParent;
                BottomSheetBehavior.from((View) bottomSheetParent.getParent()).setPeekHeight(bottomSheetParent.getHeight());
                bottomSheetBehavior.setPeekHeight(bottomSheetParent.getHeight());
                bottomSheetParent.getParent().requestLayout();
            }
        });

        // Handle chip group changes
        binding.cgSortTypes.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                viewModel.setSortType(RestaurantFilterModel.SortType.NONE);
            } else {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chipAlphabeticalSort) {
                    viewModel.setSortType(RestaurantFilterModel.SortType.ALPHABETICAL_ASC);
                } else if (checkedId == R.id.chipByMedalsSort) {
                    viewModel.setSortType(RestaurantFilterModel.SortType.MEDALS);
                } else if (checkedId == R.id.chipByRatingSort) {
                    viewModel.setSortType(RestaurantFilterModel.SortType.RATING_ASC);
                }
            }
        });

        // Handle cuisine type chips
        for (int index = 0; index < binding.cgCuisineType.getChildCount(); index++) {
            Chip chip = (Chip) binding.cgCuisineType.getChildAt(index);
            RestaurantFilterModel.CuisineType cuisineType = getCuisineTypeFromString(chip.getText().toString());
            if (cuisineType == null) {
                throw new IllegalArgumentException("Cuisine Type must not be null");
            }
            chip.setChecked(viewModel.getUiState().getValue().getCuisineTypes().contains(cuisineType));
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    viewModel.selectCuisineType(cuisineType);
                } else {
                    viewModel.deselectCuisineType(cuisineType);
                }
            });
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        super.onStart();
        executorService.execute(() -> updateBadgeDrawable(viewModel.getUiState().getValue().getFilterCount()));

        executorService.execute(() -> {
            while (viewModel.getUiState().getValue().isApplyAllFilters() || viewModel.getUiState().getValue().isClearAllFilters()) {
                if (viewModel.getUiState().getValue().isApplyAllFilters()) {
                    FilterViewModel.FilterData filterData = new FilterViewModel.FilterData(
                            viewModel.getUiState().getValue().getSortType(),
                            viewModel.getUiState().getValue().getCuisineTypes()
                    );
                    // Handle filterData application or saving
                    dismiss();
                }

                if (viewModel.getUiState().getValue().isClearAllFilters()) {
                    dismiss();
                }

                try {
                    Thread.sleep(500); // Check every 500ms (Adjust as necessary)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private RestaurantFilterModel.CuisineType getCuisineTypeFromString(String cuisineTypeName) {
        for (RestaurantFilterModel.CuisineType cuisineType : RestaurantFilterModel.CuisineType.values()) {
            if (cuisineType.name().equalsIgnoreCase(cuisineTypeName)) {
                return cuisineType;
            }
        }
        return null;
    }

    private void updateBadgeDrawable(int count) {
        if (badgeDrawable != null) {
            if (count > 0) {
                badgeDrawable.setNumber(count);
                int errorColor = MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorError, null);
                badgeDrawable.setBackgroundColor(errorColor);
            } else {
                badgeDrawable.clearNumber();
                int transparentColor = ResourcesCompat.getColor(getResources(), android.R.color.transparent, null);
                badgeDrawable.setBackgroundColor(transparentColor);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        executorService.shutdownNow();
    }
}
