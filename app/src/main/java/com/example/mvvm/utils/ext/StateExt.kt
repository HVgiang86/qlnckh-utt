package com.example.mvvm.utils.ext

import com.example.mvvm.R
import com.example.mvvm.domain.ProjectState

fun ProjectState.getStateName() = when (this) {
    ProjectState.NEW -> "Mới"
    ProjectState.UNDER_REVIEW -> "Bảo vệ"
    ProjectState.PROPOSED -> "Đề xất"
    ProjectState.IN_PROGRESS -> "Đang thực hiện"
    ProjectState.PAUSED -> "Tạm dừng"
    ProjectState.COMPLETED -> "Hoàn thành"
    ProjectState.CANCELLED -> "Đã huỷ"
}

fun ProjectState.getStateTagBackground() = when (this) {
    ProjectState.PROPOSED -> R.drawable.bg_status_propose
    ProjectState.IN_PROGRESS -> R.drawable.bg_status_inprogress
    ProjectState.NEW -> R.drawable.bg_status_new
    ProjectState.UNDER_REVIEW -> R.drawable.bg_status_under_review
    ProjectState.PAUSED -> R.drawable.bg_status_pause
    ProjectState.COMPLETED -> R.drawable.bg_status_complete
    ProjectState.CANCELLED -> R.drawable.bg_status_cancel
}
