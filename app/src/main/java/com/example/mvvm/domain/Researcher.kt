package com.example.mvvm.domain

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Researcher(
    @Expose override val id: Long,
    @Expose @SerializedName("fullName") override val name: String? = "",
    @Expose override val email: String? = "",
    @Expose override val role: UserRole? = UserRole.RESEARCHER,
    @Expose @SerializedName("dateOfBirth") override val dob: String? = "",
    @Expose val studentCode: String? = "",
    @Expose val className: String? = "",
    @Expose val major: String? = "",
    @Expose override val status: Boolean? = true,
) : User(
    id,
    name,
    email,
    role,
    dob,
    status,
),
    Parcelable
