package com.example.mvvm.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Supervisor(
    override val id: Long,

    @SerializedName("fullName")
    override val name: String,
    override val email: String,
    override val role: UserRole,

    @SerializedName("dateOfBirth")
    override val dob: String,
    val faculty: String,
    val title: String,
    val department: String,
    override val status: Boolean = true,
) : User(
    id,
    name,
    email,
    role,
    dob,
    status,
),
    Parcelable
