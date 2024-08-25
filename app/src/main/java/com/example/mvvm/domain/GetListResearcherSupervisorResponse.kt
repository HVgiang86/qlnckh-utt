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
    val avatar: Any? = null,
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
