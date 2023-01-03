package com.nandaadisaputra.storyapp.ui.fragment.location

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.storyapp.api.ApiService
import com.nandaadisaputra.storyapp.base.viewmodel.BaseViewModel
import com.nandaadisaputra.storyapp.data.datastore.DataStorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStorePreference: DataStorePreference
) : BaseViewModel() {
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}