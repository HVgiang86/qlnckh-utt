package com.example.mvvm.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.mvvm.R
import com.example.mvvm.base.AppConstants.TOAST_DURATION
import com.example.mvvm.utils.ext.showToast
import com.example.mvvm.utils.widget.AlertDialog
import com.example.mvvm.utils.widget.DialogManager
import com.example.mvvm.utils.widget.DialogManagerImpl
import es.dmoral.toasty.Toasty

abstract class BaseActivity<viewBinding : ViewBinding, viewModel : BaseViewModel> : AppCompatActivity(), BaseView {

    protected lateinit var viewBinding: viewBinding
    abstract fun inflateViewBinding(inflater: LayoutInflater): viewBinding

    protected abstract fun initialize()

    abstract val context: Context

    private lateinit var dialogManager: DialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflateViewBinding(layoutInflater)
        dialogManager = DialogManagerImpl(this)
        setContentView(viewBinding.root)
        initialize()
    }

    override fun showLoading() {
        dialogManager.showLoading()
    }

    override fun hideLoading() {
        dialogManager.hideLoading()
    }

    override fun showAlertPopup(completion: AlertDialog.() -> Unit) {
        dialogManager.showAlertPopup(completion)
    }

    override fun hideAlertPopup() {
        dialogManager.dismissAlertPopup()
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        fragments.forEach {
            if (it is BaseFragment<*, *>) {
                if (it.isVisible && it.onBackPress()) return
            }
        }
        onBackPressedDispatcher.onBackPressed()
    }

    fun handleUserDeniedPermission(permission: String, requestSettingLauncher: ActivityResultLauncher<Intent>, customAction: String? = null) {
        if (!shouldShowRequestPermissionRationale(permission)) {
            showAlertPopup {
                title(R.string.app_name)
                message("Bạn đã chọn từ chối và không hỏi lại khi ứng dụng cần cung cấp quyền, vui lòng vào Cài đặt để cấp quyền cho ứng dụng.")
                setCancelable()
                leftButton("Huỷ")
                rightButton("Cài đặt") {
                    val intent = Intent(customAction ?: Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                    requestSettingLauncher.launch(intent)
                }
            }
        } else {
            showToast("Cấp quyền bị từ chối")
        }
    }

    fun showMessage(message: String, bgType: BGType) {
        when (bgType) {
            BGType.BG_TYPE_NORMAL ->
                Toasty.normal(context, message).show()

            BGType.BG_TYPE_SUCCESS ->
                Toasty.success(context, message, TOAST_DURATION, true).show()

            BGType.BG_TYPE_WARNING ->
                Toasty.warning(context, message, TOAST_DURATION, true).show()

            BGType.BG_TYPE_ERROR ->
                Toasty.error(context, message, TOAST_DURATION, true).show()
        }
    }
}
