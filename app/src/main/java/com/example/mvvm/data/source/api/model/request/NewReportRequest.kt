package com.example.mvvm.data.source.api.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewReportRequest(
    @Expose val content: String,
    @Expose val link: String? = "",
    @Expose val topicId: Long,
): Parcelable
