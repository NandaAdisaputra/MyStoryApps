package com.nandaadisaputra.storyapp.ui.fragment.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.storyapp.api.ApiService
import com.nandaadisaputra.storyapp.base.viewmodel.BaseViewModel
import com.nandaadisaputra.storyapp.data.datastore.DataStorePreference
import com.nandaadisaputra.storyapp.data.story.StoryEntity
import com.nandaadisaputra.storyapp.data.story.StoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStorePreference: DataStorePreference
) : BaseViewModel() {
    val showLoading = MutableLiveData<Boolean>()
    val listStories = MutableLiveData<ArrayList<StoryEntity>>()
    fun getStories(token: String){
        showLoading.postValue(true)
        apiService.getStories(
            "Bearer $token").enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    listStories.postValue(response.body()?.listStory)
                    showLoading.postValue(false)
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
            }
        })
    }
    fun getListStories(): LiveData<ArrayList<StoryEntity>> {
        return listStories
    }
    val getTheme = dataStorePreference.getTheme().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreference.setTheme(isDarkMode)
        }
    }
}