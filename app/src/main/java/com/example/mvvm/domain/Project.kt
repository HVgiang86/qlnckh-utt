package com.example.mvvm.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Project(
    val id: Long,
    val title: String,
    val description: String,
    var state: ProjectState,
    val researcher: List<Researcher>,
    val supervisor: List<Supervisor>,
    val documents: List<Document>,
    var reports: List<ResearcherReport>,
    val score: Double?,
) : Parcelable
