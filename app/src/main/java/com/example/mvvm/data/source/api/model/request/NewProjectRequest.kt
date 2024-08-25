package com.example.mvvm.data.source.api.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewProjectRequest(
    @Expose val name: String, @Expose @SerializedName("label") val description: String, @Expose @SerializedName("lecturerId") val supervisorId: Long,
    @Expose @SerializedName("field") val field: String? = "",
    @Expose @SerializedName("purpose") val purpose: String? = "",

    ) : Parcelable
