package com.example.mvvm.data.source.api.model.response

import android.os.Parcelable
import com.example.mvvm.domain.Project
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllProjectResponse(
    @SerializedName("")
    @Expose
    val projects: List<Project>,
) : Parcelable
