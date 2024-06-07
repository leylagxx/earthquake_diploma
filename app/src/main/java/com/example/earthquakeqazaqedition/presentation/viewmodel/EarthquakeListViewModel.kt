package com.example.earthquakeqazaqedition.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.earthquakeqazaqedition.data.model.Earthquake
import com.example.earthquakeqazaqedition.data.model.Feature
import com.example.earthquakeqazaqedition.data.network.ApiService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EarthquakeListViewModel(private val service: ApiService) : ViewModel() {
    private var currentOffset = 1
    private val itemsPerPage = 3

    private val _loadedItems = mutableListOf<Earthquake>()
    private var isLoading = false

    private val _earthquakeListState = MutableLiveData<EarthquakeListState>()
    val earthquakeListState: LiveData<EarthquakeListState> get() = _earthquakeListState

    private val _isLoadingMoreItems = MutableLiveData<Boolean>()
    val isLoadingMoreItems: LiveData<Boolean> get() = _isLoadingMoreItems

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        // Handle exceptions here if needed
    }

    fun fetchEarthquakes() {
        if (isLoading) return

        isLoading = true
        _earthquakeListState.value = EarthquakeListState.Loading(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val response = async {
                    service.fetchEarthquakes(
                        limit = itemsPerPage,
                        offset = currentOffset,
                        minmagnitude = 0
                    )
                }
                val earthquakeList = response.await().features.map { Feature.toEarthquake(it) }
                currentOffset += itemsPerPage

                _loadedItems.addAll(earthquakeList)

                withContext(Dispatchers.Main) {
                    _earthquakeListState.value = EarthquakeListState.Success(_loadedItems)
                    _earthquakeListState.postValue(EarthquakeListState.Loading(false))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _earthquakeListState.value = EarthquakeListState.Error(e.message)
                }
            } finally {
                _isLoadingMoreItems.postValue(false)
                isLoading = false
            }
        }
    }

    fun loadMoreItems() {
        if (isLoading) return

        isLoading = true
        _isLoadingMoreItems.postValue(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val response = async {
                    service.fetchEarthquakes(
                        limit = itemsPerPage,
                        offset = currentOffset,
                        minmagnitude = 0
                    )
                }
                val earthquakeList = response.await().features.map { Feature.toEarthquake(it) }
                currentOffset += itemsPerPage

                _loadedItems.addAll(earthquakeList)

                withContext(Dispatchers.Main) {
                    _earthquakeListState.value = EarthquakeListState.Success(_loadedItems)
                    _earthquakeListState.postValue(EarthquakeListState.Loading(false))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _earthquakeListState.value = EarthquakeListState.Error(e.message)
                }
            } finally {
                _isLoadingMoreItems.postValue(false)
                isLoading = false
            }
        }
    }

    // Другие методы, если есть

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
