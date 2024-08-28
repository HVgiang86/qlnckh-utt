package com.example.mvvm.datacore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @Expose @SerializedName("code") var code: Int,
    @Expose @SerializedName("message") var message: String? = "",
)
