package com.example.mvvm.domain

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ResearcherReport(
    @Expose
    val title: String? = "",
    @Expose
    @SerializedName("create_date")
    val date: Date? = null,
    @Expose
    val content: String? = "",
    @Expose
    val reporter: String? = null,
    @Expose
    @SerializedName("documents")
    val file: List<Document>? = emptyList(),
    val supervisorComments: List<SupervisorComment>? = emptyList(),
) : Parcelable
