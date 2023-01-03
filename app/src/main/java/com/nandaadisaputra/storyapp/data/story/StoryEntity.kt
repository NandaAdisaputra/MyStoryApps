package com.nandaadisaputra.storyapp.data.story

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@kotlinx.parcelize.Parcelize
data class StoryEntity(
    @PrimaryKey
    @Expose
    @SerializedName("id_room")
    val idRoom: Int,

    @field:SerializedName("photoUrl")
    val photoUrl: String?,

    @field:SerializedName("createdAt")
    val createdAt: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("description")
    val description: String?,

    @field:SerializedName("lon")
    val lon: String?,

    @field:SerializedName("id")
    val id: String?,

    @field:SerializedName("lat")
    val lat: String?
) : Parcelable