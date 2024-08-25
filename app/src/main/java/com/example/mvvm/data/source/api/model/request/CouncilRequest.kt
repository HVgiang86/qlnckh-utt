package com.example.mvvm.data.source.api.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CouncilRequest(
    @Expose
    @SerializedName("lecturer1Email")
    val lecturer1Email: String?,
    @Expose
    @SerializedName("lecturer2Email")
    val lecturer2Email: String?,
    @Expose
    @SerializedName("lecturer3Email")
    val lecturer3Email: String?,
    @Expose
    @SerializedName("lecturer4Email")
    val lecturer4Email: String?,
    @Expose
    @SerializedName("lecturer5Email")
    val lecturer5Email: String?,
)
