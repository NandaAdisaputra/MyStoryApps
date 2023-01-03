package com.nandaadisaputra.storyapp.data.story

import com.google.gson.annotations.SerializedName

data class StoryResponse(

    @field:SerializedName("listStory")
    val listStory: ArrayList<StoryEntity>
)

