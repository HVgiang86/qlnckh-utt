package com.example.mvvm.data.source.api.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewAttachmentRequest(
    @Expose
    val name: String,
    @Expose
    val url: String,
): Parcelable
