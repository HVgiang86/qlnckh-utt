package com.example.mvvm.domain

data class GetListResearcherSupervisorResponse(
    val successfully: List<ResearcherSupervisor>
)

data class ResearcherSupervisor(
    val fullName: String? = null,
    val email: String? = null,
    val faculty: String? = null,
    val department: String? = null,
    val title: String? = null,
    val avatar: String? = null,
    val major: String? = null,
    val className: String? = null,
    val dateOfBirth: String? = null,
    val description: String? = null,
    val phoneNumber: String? = null,
    val role: String? = null,
    val status: Boolean? = null,
    val id: Long? = null
)
