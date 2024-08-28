package com.example.mvvm.datacore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @Expose @SerializedName("code") var code: Int,
    @Expose @SerializedName("message") var message: String? = "",
    @Expose @SerializedName("data") var data: T?,
)
