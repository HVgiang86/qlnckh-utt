package com.example.mvvm.domain

import com.google.gson.annotations.Expose

data class GetListResearcherSupervisorResponse(
    val successfully: Map<String, List<ResearcherSupervisor>>
)

data class ResearcherSupervisor(
    @Expose
    val fullName: String,
    @Expose
    val email: String,
    @Expose
    val faculty: Any? = null,
    @Expose
    val department: Any? = null,
    @Expose
    val title: Any? = null,
    @Expose
    val major: Any? = null,
    @Expose
    val className: Any? = null,
    @Expose
    val dateOfBirth: String,
    @Expose
    val description: String,
    @Expose
    val phoneNumber: String,
    @Expose
    val role: String,
    @Expose
    val status: Boolean,
    @Expose
    val id: Long
)

fun Researcher.toResearcherSupervisor(): ResearcherSupervisor {
    return ResearcherSupervisor(
        fullName = this.name ?: "Unnamed",
        email = this.email ?: "",
        major = this.major,
        className = this.className,
        dateOfBirth = this.dob ?: "",
        role = this.role?.name ?: UserRole.RESEARCHER.name,
        status = this.status ?: false,
        id = this.id,
        description = "",
        phoneNumber = "",
        faculty = "",
        department = "",
        title = ""
    )
}

fun Supervisor.toResearcherSupervisor(): ResearcherSupervisor {
    return ResearcherSupervisor(
        fullName = this.name ?: "Unnamed",
        email = this.email ?: "",
        faculty = this.faculty,
        department = this.department,
        title = this.title,
        dateOfBirth = this.dob ?: "",
        role = this.role?.name ?: UserRole.SUPERVISOR.name,
        status = this.status ?: false,
        id = this.id,
        description = "",
        phoneNumber = "",
        major = "",
        className = ""
    )
}
