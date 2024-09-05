package com.example.myapplication;

import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;
import com.example.myapplication.dataType.RestaurantFilterModel;
import com.example.myapplication.dataType.RestaurantFilterModel.SortType;
import com.example.myapplication.RestaurantRepository;
import com.example.myapplication.FilterViewModel.FilterData;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.HttpException;

@HiltViewModel
public class RestaurantViewModel extends ViewModel {

    public static class UiState {
        private RestaurantFilterModel filter;
        private boolean showErrorLayout;
        @StringRes
        private int errorMessageId;
        private boolean showCountriesList;
        private boolean isLoading;
        private AtomicInteger retryCount;

        public UiState() {
            this.filter = new RestaurantFilterModel();
            this.showErrorLayout = false;
            this.errorMessageId = R.string.message_error_unknown_error;
            this.showCountriesList = false;
            this.isLoading = false;
            this.retryCount = new AtomicInteger(0);
        }

        public RestaurantFilterModel getFilter() {
            return filter;
        }

        public boolean isShowErrorLayout() {
            return showErrorLayout;
        }

        @StringRes
        public int getErrorMessageId() {
            return errorMessageId;
        }

        public boolean isShowCountriesList() {
            return showCountriesList;
        }

        public boolean isLoading() {
            return isLoading;
        }

        public int getRetryCount() {
            return retryCount.get();
        }

        public void setFilter(RestaurantFilterModel filter) {
            this.filter = filter;
        }

        public void setShowErrorLayout(boolean showErrorLayout) {
            this.showErrorLayout = showErrorLayout;
        }

        public void setErrorMessageId(@StringRes int errorMessageId) {
            this.errorMessageId = errorMessageId;
        }

        public void setShowCountriesList(boolean showCountriesList) {
            this.showCountriesList = showCountriesList;
        }

        public void setLoading(boolean loading) {
            isLoading = loading;
        }

        public void incrementRetryCount() {
            this.retryCount.incrementAndGet();
        }

        public int getFilterCount() {
            int sortCount = filter.getSortType() == SortType.NONE ? 0 : 1;
            int filterCount = filter.getCuisineTypes().size();
            return sortCount + filterCount;
        }
    }

    private final UiState uiState = new UiState();
    private final RestaurantRepository restaurantsRepository;

//    @Inject
    public RestaurantViewModel(RestaurantRepository restaurantsRepository) {
        this.restaurantsRepository = restaurantsRepository;
    }

    public UiState getUiState() {
        return uiState;
    }

    public void search(String searchQuery) {
        RestaurantFilterModel updatedFilter = new RestaurantFilterModel(
                searchQuery,
                uiState.getFilter().getSortType(),
                uiState.getFilter().getCuisineTypes()
        );
        uiState.setFilter(updatedFilter);
    }

    public void applyFilters(FilterData filters) {
        RestaurantFilterModel updatedFilter = new RestaurantFilterModel(
                uiState.getFilter().getSearchQuery(),
                filters.getSortType(),
                filters.getCuisineTypes()
        );
        uiState.setFilter(updatedFilter);
    }

    public void resetFilters() {
        RestaurantFilterModel resetFilter = new RestaurantFilterModel(
                null,
                SortType.NONE,
                new HashSet<>()
        );
        uiState.setFilter(resetFilter);
    }

    public void loadFailed(Throwable exception) {
        int errorMessageId;
        if (exception instanceof UnknownHostException) {
            errorMessageId = R.string.message_error_internet_error;
//        } else if (exception instanceof HttpException) {
//            errorMessageId = R.string.message_error_server_error;
        }
        else {
            errorMessageId = R.string.message_error_unknown_error;
        }
        uiState.setErrorMessageId(errorMessageId);
        uiState.setShowCountriesList(false);
        uiState.setShowErrorLayout(true);
        uiState.setLoading(false);
    }

    public void loadSucceeded() {
        uiState.setShowCountriesList(true);
        uiState.setShowErrorLayout(false);
        uiState.setLoading(false);
    }

    public void isLoading() {
        uiState.setLoading(true);
        uiState.setShowErrorLayout(false);
        uiState.setShowCountriesList(true);
    }

    public void retryLoading() {
        uiState.incrementRetryCount();
        uiState.setLoading(true);
        uiState.setShowErrorLayout(false);
        uiState.setShowCountriesList(true);
    }
}
