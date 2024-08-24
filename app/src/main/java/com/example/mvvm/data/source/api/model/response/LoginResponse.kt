package com.example.mvvm.data.source.api.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @Expose
    @SerializedName("successfully")
    val result: LoginResult,
) : Parcelable

@Parcelize
data class LoginResult(
    @SerializedName("token")
    @Expose
    val token: String,

    @SerializedName("refreshToken")
    @Expose
    val refreshToken: String,
) : Parcelable
