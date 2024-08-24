package com.example.mvvm.data.source.api.model.response

import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.Supervisor
import com.example.mvvm.domain.UserRole
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetProfileResponse(
    @Expose
    @SerializedName("successfully")
    val profile: ProfileResponse,
)

data class ProfileResponse(
    @Expose
    val fullName: String,
    @Expose
    val email: String,
    @Expose
    val faculty: String?,
    @Expose
    val department: String?,
    @Expose
    val title: String?,
    @Expose
    val avatar: String?,
    @Expose
    val major: String?,
    @Expose
    val className: String?,
    @Expose
    val dateOfBirth: String,
    @Expose
    val description: String?,
    @Expose
    val phoneNumber: String?,
    @Expose
    val role: String,
    @Expose
    val status: Boolean,
    @Expose
    val id: Long,
) {
    fun isSupervisor() = role == "supervisor"
    fun isResearcher() = role == "researcher"
    fun isAdmin() = role == "admin"
    fun getSupervisor(): Supervisor {
        return Supervisor(
            id,
            fullName,
            email,
            UserRole.SUPERVISOR,
            dateOfBirth,
            faculty ?: "",
            title ?: "",
            department ?: "",
            status,
        )
    }
    fun getResearcher(): Researcher {
        return Researcher(
            id,
            fullName,
            email,
            UserRole.RESEARCHER,
            dateOfBirth,
            "",
            className ?: "",
            major ?: "",
            status,
        )
    }
}
