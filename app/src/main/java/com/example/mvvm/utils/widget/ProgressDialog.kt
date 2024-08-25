package com.example.mvvm.utils.widget

import android.app.Dialog
import android.content.Context
import com.example.mvvm.R

class ProgressDialog(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.progress_dialog)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
