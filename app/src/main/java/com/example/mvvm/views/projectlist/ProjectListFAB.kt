package com.example.mvvm.views.projectlist

import com.example.mvvm.domain.UserRole

enum class ProjectListFAB {
    NEW,
    PROPOSED,
}

fun getProjectListFAB(role: UserRole): ProjectListFAB {
    return when (role) {
        UserRole.RESEARCHER -> {
            ProjectListFAB.PROPOSED
        }

        UserRole.SUPERVISOR -> {
            ProjectListFAB.PROPOSED
        }

        UserRole.ADMIN -> {
            ProjectListFAB.NEW
        }
    }
}
