package com.example.mvvm.utils.ext

import android.app.Activity
import android.content.Intent
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mvvm.R
import com.example.mvvm.base.BaseActivity
import com.example.mvvm.constants.AnimateType

fun AppCompatActivity.replaceFragmentInActivity(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType = AnimateType.FADE,
) {
    supportFragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        replace(containerId, fragment, tag)
    }, animateType)
}

fun AppCompatActivity.addFragmentToActivity(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType? = AnimateType.FADE,
) {
    supportFragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType)
}

fun AppCompatActivity.goBackFragment(): Boolean {
    val isShowPreviousPage = supportFragmentManager.backStackEntryCount > 0
    if (isShowPreviousPage) {
        supportFragmentManager.popBackStackImmediate()
    }
    return isShowPreviousPage
}

fun AppCompatActivity.addClearBackStackFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType = AnimateType.FADE,
) {
    supportFragmentManager.transact({
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        replace(containerId, fragment, tag)
    }, animateType)
}

fun AppCompatActivity.startActivity(
    @NonNull intent: Intent,
    flags: Int? = null,
) {
    flags.notNull {
        intent.flags = it
    }
    startActivity(intent)
}

fun AppCompatActivity.startActivityForResult(
    @NonNull intent: Intent,
    requestCode: Int,
    flags: Int? = null,
) {
    flags.notNull {
        intent.flags = it
    }
    startActivityForResult(intent, requestCode)
}

fun AppCompatActivity.isExistFragment(fragment: Fragment): Boolean {
    return supportFragmentManager.findFragmentByTag(fragment::class.java.simpleName) != null
}

fun AppCompatActivity.switchFragment(
    @IdRes containerId: Int,
    currentFragment: Fragment,
    newFragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = newFragment::class.java.simpleName,
) {
    supportFragmentManager.transact({
        setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.fade_out,
        )
        if (isExistFragment(newFragment)) {
            hide(currentFragment).show(
                newFragment,
            )
        } else {
            hide(currentFragment)
            if (addToBackStack) {
                addToBackStack(tag)
            }
            add(containerId, newFragment, tag)
        }
    })
}

fun AppCompatActivity.removeFragment(
    tag: String?,
    isPopBackStack: Boolean = true,
) {
    supportFragmentManager.findFragmentByTag(tag).notNull {
        supportFragmentManager.beginTransaction().apply {
            remove(it)
            commit()
        }
        if (isPopBackStack) supportFragmentManager.popBackStack()
    }
}

fun AppCompatActivity.clearAllFragment() {
    repeat((0..supportFragmentManager.backStackEntryCount).count()) { supportFragmentManager.popBackStack() }
}

fun Activity.showPopupError(
    title: String = "Thông báo",
    content: String,
    textButton: String = getString(R.string.ok),
    completion: () -> Unit = {},
    onClickOk: (() -> Unit)? = null,
) {
    (this as BaseActivity<*, *>).showAlertPopup {
        setCancelable()
        title(title)
        message(content)
        midButton(textButton) {
            if (textButton == "Đồng ý") {
                dismiss()
                completion()
            }
            if (textButton == getString(R.string.ok)) {
                dismiss()
                onClickOk?.invoke()
            }
        }
    }
}

// fun Application.getContext(): Context {
//    this.resources.isNull { return this }
//    val config = Configuration(this.resources.configuration)
//    return this.createConfigurationContext(config)
// }
//
// fun Activity.showError(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
//    Snackbar.make(findViewById(android.R.id.content), message, duration).show()
// }
//
// fun Activity.showPopupError(
//    title: String = getString(R.string.notification),
//    content: String,
//    textButton: String = getString(R.string.ok),
//    completion: () -> Unit = {},
//    onClickOk: (() -> Unit)? = null
// ) {
//    (this as BaseActivity<*, *>).showAlertPopup {
//        setCancelable()
//        title(title)
//        message(content)
//        midButton(textButton) {
//            if (textButton == getString(R.string.agree)) {
//                dismiss()
//                completion()
//            }
//            if (textButton == getString(R.string.ok)) {
//                dismiss()
//                onClickOk?.invoke()
//            }
//        }
//    }
// }
//
// fun BaseActivity<*, *>?.handleDefaultApiError(
//    apiError: Throwable,
//    isSignIn: Boolean = false,
//    onClickOk: (() -> Unit)? = null
// ) {
//    this?.let {
//        when (apiError) {
//            is HttpException -> {
//                getErrorMessage(apiError)?.let {
//                    if (!isSignIn) {
//                        when (apiError.code()) {
//                            STATUS_CODE_SEVER_DEPLOY ->
//                                showPopupError(content = getString(R.string.msg_sever_deploy))
//                            STATUS_CODE_TOKEN_EXPIRED ->
//                                showPopupError(content = getString(R.string.msg_sever_deploy),
//                                    completion = { (it as? MainActivity)?.onTokenExpired() })
//                            else -> showPopupError(content = it, onClickOk = onClickOk)
//                        }
//                    } else { showPopupError(content = it, onClickOk = onClickOk) }
//                }
//            }
//            is SocketTimeoutException -> {
//                showPopupError(content = getString(R.string.msg_error_time_out), onClickOk = onClickOk)
//            }
//            is IOException -> {
//                showPopupError(content = getString(R.string.msg_error_no_internet), onClickOk = onClickOk)
//            }
//            else -> {
//                showPopupError(content = getString(R.string.msg_error_data_parse), onClickOk = onClickOk)
//            }
//        }
//    }
// }
//
// fun BaseActivity<*, *>.getErrorMessage(e: Exception): String? {
//    val responseBody = (e as HttpException).response()?.errorBody()
//    val errorCode = e.response()?.code()
//    if (errorCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
//        // TODO reLogin
//    }
//
//    return responseBody?.let {
//        try {
//            // Handle get message error when request api, depend on format json api
//            val jsonObject = JSONObject(responseBody.string())
//            val message = jsonObject.getString("messages")
//            if (!message.isNullOrBlank()) {
//                message
//            } else {
//                getString(R.string.msg_error_data_parse)
//            }
//        } catch (ex: Exception) {
//            e.message
//        }
//    } ?: kotlin.run {
//        getString(R.string.msg_error_data_parse)
//    }
// }
