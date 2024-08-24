package com.example.mvvm.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.example.mvvm.utils.widget.AlertDialog
import com.example.mvvm.utils.widget.DialogManagerImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseFragment<viewBinding : ViewBinding, viewModel : BaseViewModel> : Fragment(), BaseView {
    protected abstract val viewModel: viewModel
    private var _viewBinding: viewBinding? = null
    protected val viewBinding get() = _viewBinding!! // ktlint-disable

    abstract fun inflateViewBinding(inflater: LayoutInflater): viewBinding

    protected abstract fun initialize()
    override fun showAlertPopup(completion: AlertDialog.() -> Unit) {
        (activity as? BaseActivity<*, *>)?.showAlertPopup(completion)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DialogManagerImpl(getContext())
    }

    open fun registerLiveData() {
        lifecycleScope.launch {
            viewModel.run {
                isLoading.collect {
                    if (it) showLoading() else hideLoading()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.responseMessage.collect {
                (activity as? BaseActivity<*, *>)?.showMessage(it.message, it.bgType)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = inflateViewBinding(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    fun showMessage(message: String, bgType: BGType) {
        println("BaseFragment showMessage: $message")
        (activity as? BaseActivity<*, *>)?.showMessage(message, bgType)
    }

    override fun showLoading() {
        (activity as? BaseActivity<*, *>)?.showLoading()
    }

    override fun hideLoading() {
        (activity as? BaseActivity<*, *>)?.hideLoading()
    }

    override fun hideAlertPopup() {
        (activity as? BaseActivity<*, *>)?.hideAlertPopup()
    }

    open fun onBackPress() = false

    /**
     * Fragments outlive their views. Make sure you clean up any references to
     * the binding class instance in the fragment's onDestroyView() method.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    fun hideKeyboard() {
        val view = (activity as? BaseActivity<*, *>)?.currentFocus
        if (view != null) {
            val imm = (activity as? BaseActivity<*, *>)?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun onHideSoftKeyBoard() {
        val inputMng: InputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMng.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    fun isViewBindingAvailable(): Boolean {
        return _viewBinding != null
    }

    fun handleUserDeniedPermission(permission: String, requestSettingLauncher: ActivityResultLauncher<Intent>) {
        (activity as? BaseActivity<*, *>)?.handleUserDeniedPermission(permission, requestSettingLauncher)
    }

    fun <T> collectLifecycleFlow(flow: Flow<T>, collect: (T) -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                flow.collect(collect)
            }
        }
    }
}
