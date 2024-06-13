package com.example.earthquakeqazaqedition.presentation.viewmodel

import androidx.lifecycle.*
import com.example.earthquakeqazaqedition.data.model.Earthquake
import com.example.earthquakeqazaqedition.data.model.Feature
import com.example.earthquakeqazaqedition.data.network.ApiService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterViewModel(private val service: ApiService) : ViewModel() {
    private var currentOffset = 1
    private val itemsPerPage = 6
    private var magnitudeFilter: Double = 0.0
    class Provider(private val service: ApiService) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FilterViewModel(service) as T
        }
    }

    private val _loadedItems = mutableListOf<Earthquake>()
    private var isLoading = false

    private val _filterState = MutableLiveData<FilterState>()
    val filterState: LiveData<FilterState> get() = _filterState

    private val _isLoadingMoreItems = MutableLiveData<Boolean>()
    val isLoadingMoreItems: LiveData<Boolean> get() = _isLoadingMoreItems

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    }
    private fun fetchEarthquakesInternal(offset: Int, magnitude: Double) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val response = service.fetchEarthquakes(
                        limit = itemsPerPage,
                        offset = offset,
                        minmagnitude = magnitude
                )
                val earthquakeList = response.features.map { Feature.toEarthquake(it) }
                _loadedItems.addAll(earthquakeList)

                withContext(Dispatchers.Main) {
                    _filterState.value = FilterState.Success(_loadedItems)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _filterState.value = FilterState.Error(e.message)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoadingMoreItems.value = false
                    isLoading = false
                }
            }
        }

    }

    fun fetchEarthquakes(magnitude: Double) {
        if (isLoading) return
        isLoading = true
        _filterState.value = FilterState.Loading(true)
        _isLoadingMoreItems.value = true
        fetchEarthquakesInternal(currentOffset, magnitude)
        currentOffset += itemsPerPage
    }
    fun setMagnitudeFilter(magnitude: Double) {
        magnitudeFilter = magnitude
    }

    fun applyFilters() {
        fetchEarthquakes(magnitudeFilter)
    }
}

sealed class FilterState {
    data class Loading(val isLoading: Boolean) : FilterState()
    data class Success(val items: List<Earthquake>) : FilterState()
    data class Error(val message: String? = null) : FilterState()
}
