package com.example.earthquakeqazaqedition.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.earthquakeqazaqedition.data.model.Earthquake
import com.example.earthquakeqazaqedition.data.model.Feature
import com.example.earthquakeqazaqedition.data.network.ApiService
import com.google.api.Endpoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EarthquakeListViewModel(private val service: ApiService) : ViewModel() {

    class Provider(private val service: ApiService) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EarthquakeListViewModel(service) as T
        }
    }

    private val _earthquakeListState = MutableLiveData<EarthquakeListState>()
    val earthquakeListState: LiveData<EarthquakeListState> get() = _earthquakeListState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    }


    fun fetchEarthquakes() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val response = service.fetchEarthquakes(limit = 6)
                val earthquakeList = response.features.map { Feature.toEarthquake(it) }
                withContext(Dispatchers.Main) {
                    _earthquakeListState.value = EarthquakeListState.Success(earthquakeList)
                }
            } catch (e: Exception) {
                _earthquakeListState.value = EarthquakeListState.Error(e.message ?: "Unknown error")
            }
        }
    }

//    fun filterEarthquakes(endpoint: String) {
//        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
//            try {
//                val response = service.filterEarthquakes(endpoint)
//                val earthquakeList = response.features.map { Feature.toEarthquake(it) }
//                withContext(Dispatchers.Main) {
//                    _earthquakeListState.value = EarthquakeListState.Success(earthquakeList.take(10))
//                }
//            } catch (e: Exception) {
//            }
//        }
//    }
}
sealed class EarthquakeListState {

    data class Success(val items: List<Earthquake>) : EarthquakeListState()
    data class Error(val message: String? = null) : EarthquakeListState()
}
