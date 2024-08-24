package com.example.mvvm.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ResearcherReport(
    val id: Long,
    val title: String,
    val date: Date,
    val content: String,
    val reporter: Researcher,
    val file: Document,
    val supervisorComments: List<SupervisorComment>,
) : Parcelable
