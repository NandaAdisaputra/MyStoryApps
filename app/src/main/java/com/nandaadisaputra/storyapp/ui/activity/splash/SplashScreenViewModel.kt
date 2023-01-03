package com.nandaadisaputra.storyapp.ui.activity.splash

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.nandaadisaputra.storyapp.api.ApiService
import com.nandaadisaputra.storyapp.base.viewmodel.BaseViewModel
import com.nandaadisaputra.storyapp.data.datastore.DataStorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val dataStorePreference: DataStorePreference
) : BaseViewModel() {
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}