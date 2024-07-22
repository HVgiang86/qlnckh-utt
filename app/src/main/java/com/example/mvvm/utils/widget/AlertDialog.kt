package com.example.mvvm.utils.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.mvvm.databinding.CustomAlertDialogViewBinding

class AlertDialog(private val context: Context) {
    private val dialogView by lazy {
        CustomAlertDialogViewBinding.inflate(LayoutInflater.from(context), null, false)
    }

    private var builder: AlertDialog.Builder? = null

    private var alertDialog: AlertDialog? = null

    init {
        initialize()
    }

    private fun initialize() {
        builder = AlertDialog.Builder(context).setView(dialogView.root)
        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    fun create() =
        alertDialog?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

    fun title(title: CharSequence) {
        dialogView.titleTextView.text = title
    }

    fun title(
        @StringRes resId: Int,
    ) {
        dialogView.titleTextView.text = context.getString(resId)
    }

    fun message(message: CharSequence) {
        dialogView.messageTextView.text = message
    }

    fun message(
        @StringRes resId: Int,
    ) {
        dialogView.messageTextView.text = context.getString(resId)
    }

    fun leftButton(
        text: String,
        completion: () -> Unit = {},
    ) = with(dialogView) {
        leftButton.text = text
        midButton.isVisible = false
        leftButton.isVisible = true
        leftButton.setOnUnMultiClickListener {
            completion()
            alertDialog?.dismiss()
        }
    }

    fun leftButton(
        @StringRes resId: Int,
        completion: () -> Unit = {},
    ) = leftButton(context.getString(resId), completion)

    fun rightButton(
        text: String,
        completion: () -> Unit = {},
    ) = with(dialogView) {
        rightButton.text = text
        rightButton.isVisible = true
        midButton.isVisible = false
        rightButton.setOnUnMultiClickListener {
            completion()
            alertDialog?.dismiss()
        }
    }

    fun rightButton(
        @StringRes resId: Int,
        completion: () -> Unit = {},
    ) = rightButton(context.getString(resId), completion)

    fun midButton(
        @StringRes resId: Int,
        completion: () -> Unit = {},
    ) = midButton(context.getString(resId), completion)

    fun midButton(
        text: String,
        completion: () -> Unit = {},
    ) = with(dialogView) {
        midButton.text = text
        midButton.isVisible = true
        leftButton.isVisible = false
        rightButton.isVisible = false
        midButton.setOnUnMultiClickListener {
            completion()
            alertDialog?.dismiss()
        }
    }

    fun dismiss() {
        alertDialog?.dismiss()
    }

    fun setCancelable() {
        alertDialog?.setCancelable(false)
    }

    fun show() {
        alertDialog?.show()
    }
}
