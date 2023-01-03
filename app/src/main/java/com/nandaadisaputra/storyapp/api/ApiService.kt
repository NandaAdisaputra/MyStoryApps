package com.nandaadisaputra.storyapp.api

import com.nandaadisaputra.storyapp.data.login.LoginResponse
import com.nandaadisaputra.storyapp.data.story.AddResponse
import com.nandaadisaputra.storyapp.data.story.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
     fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): String

    @GET("stories")
    fun getStories(
        @Header("Authorization") auth: String
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody?,
        @Part("lon") longitude: RequestBody?,
    ): Call<AddResponse>
}