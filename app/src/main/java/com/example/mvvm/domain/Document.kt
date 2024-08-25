package com.example.mvvm.domain

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Document(
    @Expose val title: String,
    @Expose @SerializedName("link") val url: String,
    @Expose @SerializedName("create_date") val createDate: String,
) : Parcelable
