package com.nandaadisaputra.storyapp.ui.activity.register

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
class RegisterViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStorePreference: DataStorePreference
) : BaseViewModel() {
    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        _apiResponse.send(ApiResponse().responseLoading("Loading.."))
        ApiObserver(
            { apiService.register(name, email, password) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val status = response.getBoolean("error")
                    if (!status) {
                        val data = response.getString("message")
                        Timber.d("User created : $data")
                        _apiResponse.send(ApiResponse().responseSuccess())
                    } else {
                        _apiResponse.send(ApiResponse().responseError())
                    }
                }
            })
    }
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}