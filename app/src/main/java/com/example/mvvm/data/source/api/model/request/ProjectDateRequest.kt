package com.example.mvvm.data.source.api.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectDateRequest(
    @Expose
    val date: String
): Parcelable
