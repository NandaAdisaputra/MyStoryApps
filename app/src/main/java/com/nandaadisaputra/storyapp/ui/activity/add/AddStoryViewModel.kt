package com.nandaadisaputra.storyapp.ui.activity.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.storyapp.api.ApiService
import com.nandaadisaputra.storyapp.base.viewmodel.BaseViewModel
import com.nandaadisaputra.storyapp.data.datastore.DataStorePreference
import com.nandaadisaputra.storyapp.data.story.AddResponse
import com.nandaadisaputra.storyapp.ui.utils.reduceFileImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(private val apiService: ApiService, private val dataStorePreference: DataStorePreference) : BaseViewModel() {
    val showLoading = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun uploadImage(desc: String, getFile: File?, lat: Double?, lon: Double?) {
        if (getFile != null && desc.isNotEmpty()) {
            showLoading.postValue(true)
            val file = reduceFileImage(getFile)

            val description = desc.toRequestBody("text/plain".toMediaType())
            var latitude: RequestBody? = null
            var longitude: RequestBody? = null

            if (lat != null && lon != null) {
                latitude = lat.toString().toRequestBody("text/plain".toMediaType())
                longitude = lon.toString().toRequestBody("text/plain".toMediaType())
            }

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val service = apiService.addStory(imageMultipart, description, latitude, longitude)
            showLoading.postValue(true)
            service.enqueue(object : Callback<AddResponse> {
                override fun onResponse(
                    call: Call<AddResponse>,
                    response: Response<AddResponse>
                ) {
                    showLoading.postValue(false)
                    _isError.value = !response.isSuccessful
                }
                override fun onFailure(call: Call<AddResponse>, t: Throwable) {
                    _isError.value = true
                    showLoading.postValue(false)
                }
            })
        } else {
            _isError.value = true
        }
    }
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}