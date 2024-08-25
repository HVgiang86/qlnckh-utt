package com.example.mvvm.utils.widget

import android.content.Context
import java.lang.ref.WeakReference

class DialogManagerImpl(ctx: Context?) : DialogManager {

    private var context: WeakReference<Context?>? = null
    private var progressDialog: ProgressDialog? = null
    private var alertDialog: AlertDialog? = null

    init {
        context = WeakReference(ctx).apply {
            progressDialog = ProgressDialog(this.get()!!)
            alertDialog = AlertDialog(this.get()!!)
        }
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun showProcessing() {
        progressDialog?.show()
    }

    override fun hideLoading() {
        progressDialog?.dismiss()
    }

    override fun onRelease() {
        progressDialog = null
        alertDialog = null
    }

    override fun showAlertPopup(completion: AlertDialog.() -> Unit) {
        alertDialog?.apply {
            completion()
            create()
        }?.show()
    }

    override fun dismissAlertPopup() {
        alertDialog?.dismiss()
    }
}
