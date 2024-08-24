package com.example.mvvm.utils.widget

interface DialogManager {

    fun showLoading()

    fun showProcessing()

    fun hideLoading()

    fun onRelease()

    fun showAlertPopup(completion: AlertDialog.() -> Unit)

    fun dismissAlertPopup()
}
