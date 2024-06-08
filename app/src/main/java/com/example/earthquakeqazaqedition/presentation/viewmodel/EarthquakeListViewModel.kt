package com.example.earthquakeqazaqedition.presentation.viewmodel

import androidx.lifecycle.*
import com.example.earthquakeqazaqedition.data.model.Earthquake
import com.example.earthquakeqazaqedition.data.model.Feature
import com.example.earthquakeqazaqedition.data.network.ApiService
import kotlinx.coroutines.*

class EarthquakeListViewModel(private val service: ApiService) : ViewModel() {
    private var currentOffset = 1
    private val itemsPerPage = 8

    private val _loadedItems = mutableListOf<Earthquake>()
    private var isLoading = false

    private val _earthquakeListState = MutableLiveData<EarthquakeListState>()
    val earthquakeListState: LiveData<EarthquakeListState> get() = _earthquakeListState

    private val _isLoadingMoreItems = MutableLiveData<Boolean>()
    val isLoadingMoreItems: LiveData<Boolean> get() = _isLoadingMoreItems

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    }
    private fun fetchEarthquakesInternal(offset: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val response = service.fetchEarthquakes(
                    limit = itemsPerPage,
                    offset = offset,
                    minmagnitude = 0.0
                )
                val earthquakeList = response.features.map { Feature.toEarthquake(it) }
                _loadedItems.addAll(earthquakeList)

                withContext(Dispatchers.Main) {
                    _earthquakeListState.value = EarthquakeListState.Success(_loadedItems)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _earthquakeListState.value = EarthquakeListState.Error(e.message)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoadingMoreItems.value = false
                    isLoading = false
                }
            }
        }
    }
    fun fetchEarthquakes() {
        if (isLoading) return
        isLoading = true
        _earthquakeListState.value = EarthquakeListState.Loading(true)
        _isLoadingMoreItems.value = true
        fetchEarthquakesInternal(currentOffset)
        currentOffset += itemsPerPage
    }

    fun loadMoreItems() {
        if (isLoading) return

        isLoading = true
        _isLoadingMoreItems.value = true

        fetchEarthquakesInternal(currentOffset)
        currentOffset += itemsPerPage
    }
    fun filterEarthquakes(filter: String) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val response = when (filter) {
                    "m1" -> service.fetchEarthquakes(limit = itemsPerPage, offset = 1, minmagnitude = 1.0)
                    "m25" -> service.fetchEarthquakes( limit = itemsPerPage, offset = 1, minmagnitude = 2.5)
                    "m45" -> service.fetchEarthquakes( limit = itemsPerPage, offset = 1, minmagnitude = 4.5)
                    else -> service.fetchEarthquakes(limit = itemsPerPage, offset = 1, minmagnitude = 0.0)
                }
                val earthquakeList = response.features.map { Feature.toEarthquake(it) }
                withContext(Dispatchers.Main) {
                    _earthquakeListState.value = EarthquakeListState.Success(earthquakeList)
                }
            } catch (e: Exception) {
                _earthquakeListState.value = EarthquakeListState.Error(e.message ?: "Unknown error")
            }
        }
    }



    class Provider(private val service: ApiService) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EarthquakeListViewModel(service) as T
        }
    }

}

sealed class EarthquakeListState {
    data class Loading(val isLoading: Boolean) : EarthquakeListState()
    data class Success(val items: List<Earthquake>) : EarthquakeListState()
    data class Error(val message: String? = null) : EarthquakeListState()
}
