package com.example.mvvm.views.incharge

import com.example.mvvm.R
import com.example.mvvm.domain.ProjectState
import com.example.mvvm.domain.UserRole

enum class ProjectFAB {
    APPROVE,
    ASSIGN,
    SCORE,
}

fun getProjectFAB(state: ProjectState, role: UserRole): ProjectFAB? {
    when (state) {
        ProjectState.NEW -> {
            return null

        }

        ProjectState.PROPOSED -> {
          return null
        }

        ProjectState.IN_PROGRESS -> {
            return null

        }

        ProjectState.UNDER_REVIEW -> {
            if (role == UserRole.ADMIN) {
                return ProjectFAB.ASSIGN
            } else {
                return null
            }
        }

        ProjectState.PAUSED -> {
            return null
        }

        ProjectState.COMPLETED -> {
            if (role == UserRole.ADMIN) {
                return ProjectFAB.SCORE
            } else {
                return null
            }
        }

        ProjectState.CANCELLED -> {
            return null
        }

        ProjectState.PENDING -> {
            if (role == UserRole.ADMIN) {
                return ProjectFAB.APPROVE
            } else {
                return null
            }
        }
    }
}

fun ProjectFAB.getIcon() = when (this) {
    ProjectFAB.ASSIGN -> R.drawable.ic_add
    ProjectFAB.APPROVE -> R.drawable.ic_review
    ProjectFAB.SCORE -> R.drawable.ic_done
}

fun ProjectFAB.getBackgroundColor() = when (this) {
    ProjectFAB.ASSIGN -> R.color.pending_tag
    ProjectFAB.APPROVE -> R.color.inprogress_tag
    ProjectFAB.SCORE -> R.color.complete_tag
}

fun ProjectFAB.getTextColor() = when (this) {
    ProjectFAB.ASSIGN -> R.color.text_dark_color
    ProjectFAB.APPROVE -> R.color.text_dark_color
    ProjectFAB.SCORE -> R.color.text_dark_color
}

fun ProjectFAB.getText() = when (this) {
    ProjectFAB.ASSIGN -> "Chỉ định hội đồng"
    ProjectFAB.APPROVE -> "Phê duyệt"
    ProjectFAB.SCORE -> "Chấm điểm"
}
