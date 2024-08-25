package com.example.mvvm.data.source.api.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateProfileRequest(
    @Expose
    val fullName: String,
    @Expose
    val birthday: String,
    @Expose
    val faculty: String,
    @Expose
    val department: String,
    @Expose
    val title: String,
    @Expose
    val major: String,
    @Expose
    val className: String,
) : Parcelable
