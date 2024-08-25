package com.example.mvvm.data.source.api.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @Expose @SerializedName("successfully") var successfully: T,
)
