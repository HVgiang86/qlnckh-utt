package com.example.mvvm.domain

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class ProjectResponse(
    @Expose
    val id: Long,
    @Expose
    val title: String? = "",
    @Expose
    @SerializedName("label")
    val description: String? = "",
    @Expose
    @SerializedName("currentState")
    var state: String? = ProjectState.NEW.name,
    @Expose
    @SerializedName("studentsOfTopic")
    val researcher: List<Researcher>? = emptyList(),
    @Expose
    val supervisor: Supervisor? = null,
    @Expose
    val documents: List<Document>? = emptyList(),
    @Expose
    var reports: List<ResearcherReport>? = emptyList(),
    @Expose
    val score: Double? = 0.0,
) : Parcelable {
    fun toProject() = Project(
        id = id,
        title = title,
        description = description,
        state = state?.let { ProjectState.valueOf(it) },
        researcher = researcher,
        supervisor = supervisor,
        documents = documents,
        reports = reports,
        score = score,
    )
}

@Parcelize
data class Project(
    @Expose
    val id: Long,
    @Expose
    val title: String? = "",
    @Expose
    @SerializedName("label")
    val description: String? = "",
    @Expose
    @SerializedName("currentState")
    var state: ProjectState? = ProjectState.NEW,
    @Expose
    @SerializedName("studentsOfTopic")
    val researcher: List<Researcher>? = emptyList(),
    @Expose
    val supervisor: Supervisor? = null,
    @Expose
    val documents: List<Document>? = emptyList(),
    @Expose
    var reports: List<ResearcherReport>? = emptyList(),
    @Expose
    val score: Double? = 0.0,
) : Parcelable, Serializable
