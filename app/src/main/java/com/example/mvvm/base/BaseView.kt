package com.example.mvvm.base

import com.example.mvvm.utils.widget.AlertDialog

interface BaseView {
    fun showLoading(isShow: Boolean)

    fun showLoading()

    fun hideLoading()

    fun showAlertPopup(completion: AlertDialog.() -> Unit)

    fun hideAlertPopup()

    fun showPopupImage(
        content: String = "",
        titleButton: String = "",
        completion: () -> Unit,
    )
}
