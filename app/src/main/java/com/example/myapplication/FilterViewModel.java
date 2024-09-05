package com.example.myapplication;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.dataType.RestaurantFilterModel.CuisineType;
import com.example.myapplication.dataType.RestaurantFilterModel.SortType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FilterViewModel extends ViewModel {

    public static final String KEY_FILTER_DATA = "filter_data";

    public static class FilterData implements Serializable {
        private final SortType sortType;
        private final Set<CuisineType> cuisineTypes;

        public FilterData(SortType sortType, Set<CuisineType> cuisineTypes) {
            this.sortType = sortType == null ? SortType.NONE : sortType;
            this.cuisineTypes = cuisineTypes;
        }

        public SortType getSortType() {
            return sortType;
        }

        public Set<CuisineType> getCuisineTypes() {
            return cuisineTypes;
        }
    }

    public static class UiState {
        private final SortType sortType;
        private final Set<CuisineType> cuisineTypes;
        private final boolean clearAllFilters;
        private final boolean applyAllFilters;

        public UiState(SortType sortType, Set<CuisineType> cuisineTypes, boolean clearAllFilters, boolean applyAllFilters) {
            this.sortType = sortType == null ? SortType.NONE : sortType;
            this.cuisineTypes = cuisineTypes == null ? new HashSet<>() : cuisineTypes;
            this.clearAllFilters = clearAllFilters;
            this.applyAllFilters = applyAllFilters;
        }

        public UiState() {
            this(SortType.NONE, new HashSet<>(), false, false);
        }

        public boolean isClearAllFilters() {
            return clearAllFilters;
        }

        public boolean isApplyAllFilters() {
            return applyAllFilters;
        }

        public int getFilterCount() {
            int sortCount = sortType == SortType.NONE ? 0 : 1;
            int filterCount = cuisineTypes.size();
            return sortCount + filterCount;
        }

        public SortType getSortType() {
            return sortType;
        }

        public Set<CuisineType> getCuisineTypes() {
            return cuisineTypes;
        }

        public boolean isChipAlphabeticalSortChecked() {
            return sortType == SortType.ALPHABETICAL_ASC;
        }

        public boolean isChipByRatingSortChecked() {
            return sortType == SortType.RATING_ASC;
        }

        public boolean isChipByMedalSortChecked() {
            return sortType == SortType.MEDALS;
        }

        public UiState copy(SortType sortType, Set<CuisineType> cuisineTypes) {
            return new UiState(sortType, cuisineTypes, this.clearAllFilters, this.applyAllFilters);
        }

        public UiState copy(boolean clearAllFilters, Set<CuisineType> cuisineTypes, SortType sortType) {
            return new UiState(sortType, cuisineTypes, clearAllFilters, this.applyAllFilters);
        }

        public UiState copyApplyFilters(boolean applyAllFilters) {
            return new UiState(this.sortType, this.cuisineTypes, this.clearAllFilters, applyAllFilters);
        }
    }

    private final MutableLiveData<UiState> _uiState;
    private final LiveData<UiState> uiState;

    public FilterViewModel(SavedStateHandle savedStateHandle) {
        // Instantiate the MutableLiveData
        _uiState = new MutableLiveData<>(new UiState());
        uiState = _uiState;

        FilterData filterData = savedStateHandle.get(KEY_FILTER_DATA);
        if (filterData != null) {
            _uiState.setValue(_uiState.getValue().copy(filterData.getSortType(), filterData.getCuisineTypes()));
        }
    }

    public LiveData<UiState> getUiState() {
        return uiState;
    }

    public void setSortType(SortType sortType) {
        _uiState.setValue(_uiState.getValue().copy(sortType, _uiState.getValue().getCuisineTypes()));
    }

    public void selectCuisineType(CuisineType cuisineType) {
        _uiState.setValue(_uiState.getValue().copy(_uiState.getValue().getSortType(), addCuisineType(cuisineType)));
    }

    public void deselectCuisineType(CuisineType cuisineType) {
        _uiState.setValue(_uiState.getValue().copy(_uiState.getValue().getSortType(), removeCuisineType(cuisineType)));
    }

    private Set<CuisineType> addCuisineType(CuisineType cuisineType) {
        Set<CuisineType> cuisineTypes = new HashSet<>(_uiState.getValue().getCuisineTypes());
        cuisineTypes.add(cuisineType);
        return cuisineTypes;
    }

    private Set<CuisineType> removeCuisineType(CuisineType cuisineType) {
        Set<CuisineType> cuisineTypes = new HashSet<>(_uiState.getValue().getCuisineTypes());
        cuisineTypes.remove(cuisineType);
        return cuisineTypes;
    }

    public void resetFilters() {
        _uiState.setValue(_uiState.getValue().copy(true, new HashSet<>(), SortType.NONE));
    }

    public void applyFilters() {
        _uiState.setValue(_uiState.getValue().copyApplyFilters(true));
    }
}
