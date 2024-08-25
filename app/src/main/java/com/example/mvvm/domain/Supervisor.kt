package com.example.mvvm.domain

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Supervisor(
    @Expose
    override val id: Long,
    @Expose
    @SerializedName("fullName")
    override val name: String? = "",
    @Expose
    override val email: String? = "",
    @Expose
    override val role: UserRole? = UserRole.SUPERVISOR,
    @Expose
    @SerializedName("dateOfBirth")
    override val dob: String? = "",
    @Expose
    val faculty: String? = "",
    @Expose
    val title: String? = "",
    @Expose
    val department: String? = "",
    @Expose
    override val status: Boolean? = true,
) : User(
    id,
    name,
    email,
    role,
    dob,
    status,
),
    Parcelable
