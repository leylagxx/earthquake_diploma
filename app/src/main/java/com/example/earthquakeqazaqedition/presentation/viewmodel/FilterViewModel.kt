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

    class Provider(private val service: ApiService) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FilterViewModel(service) as T
        }
    }

    private val _earthquakeListState = MutableLiveData<FilterState>()
    val earthquakeListState: LiveData<FilterState> get() = _earthquakeListState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _earthquakeListState.value = FilterState.Error(exception.message ?: "Unknown error")
    }

    fun fetchEarthquakes(filter: String) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val response = when (filter) {
                    "m1" -> service.fetchEarthquakes(limit = 8, offset = 1, minmagnitude = 1.0)
                    "m25" -> service.fetchEarthquakes( limit = 9, offset = 1, minmagnitude = 1.0)
                    "m45" -> service.fetchEarthquakes( limit = 9, offset = 1, minmagnitude = 1.0)
                    else -> service.fetchEarthquakes(limit = 9, offset = 1, minmagnitude = 0.0)
                }
                val earthquakeList = response.features.map { Feature.toEarthquake(it) }
                withContext(Dispatchers.Main) {
                    _earthquakeListState.value = FilterState.Success(earthquakeList)
                }
            } catch (e: Exception) {
                _earthquakeListState.value = FilterState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class FilterState {
    data class Success(val items: List<Earthquake>) : FilterState()
    data class Error(val message: String? = null) : FilterState()
}
