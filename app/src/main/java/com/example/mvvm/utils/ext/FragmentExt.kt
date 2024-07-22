package com.example.mvvm.utils.ext

import android.app.Activity
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mvvm.R
import com.example.mvvm.constants.AnimateType

fun Fragment.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType = AnimateType.FADE,
) {
    activity?.supportFragmentManager?.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        replace(containerId, fragment, tag)
    }, animateType)
}

fun Fragment.addFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String? = fragment::class.java.simpleName,
    animateType: AnimateType = AnimateType.FADE,
) {
    activity?.supportFragmentManager?.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType)
}

fun Fragment.addChildFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType = AnimateType.FADE,
) {
    childFragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType)
}

fun Fragment.goBackFragment(): Boolean {
    activity?.supportFragmentManager?.notNull {
        val isShowPreviousPage = it.backStackEntryCount > 0
        if (isShowPreviousPage) {
            it.popBackStackImmediate()
        }
        return isShowPreviousPage
    }
    return false
}

fun Fragment.showChildFragment(
    @IdRes containerId: Int,
    showFragment: Fragment,
) {
    val existFragment = childFragmentManager.findFragmentByTag(showFragment.javaClass.simpleName)
    childFragmentManager.beginTransaction().apply {
        if (existFragment != null) {
            show(existFragment)
        } else {
            addChildFragment(containerId, showFragment, true)
        }
        commit()
    }
}

fun Fragment.hideChildFragment(hideFragments: Fragment) {
    val existFragment = childFragmentManager.findFragmentByTag(hideFragments.javaClass.simpleName)
    childFragmentManager.beginTransaction().apply {
        existFragment?.let { if (!it.isHidden) hide(existFragment) }
        commit()
    }
}

fun Fragment.addFragmentToActivity(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType? = AnimateType.FADE,
) {
    (this.activity as AppCompatActivity).addFragmentToActivity(
        containerId,
        fragment,
        addToBackStack,
        tag,
        animateType,
    )
}

/**
 * Runs a FragmentTransaction, then calls commitAllowingStateLoss().
 */
inline fun FragmentManager.transact(
    action: FragmentTransaction.() -> Unit,
    animateType: AnimateType? = AnimateType.FADE,
) {
    beginTransaction().apply {
        setCustomAnimations(this, animateType)
        action()
    }.commitAllowingStateLoss()
}

fun setCustomAnimations(
    transaction: FragmentTransaction,
    animateType: AnimateType? = AnimateType.FADE,
) {
    when (animateType) {
        AnimateType.FADE ->
            transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out,
            )
        AnimateType.SLIDE_TO_RIGHT ->
            transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right,
            )
        AnimateType.SLIDE_TO_LEFT ->
            transaction.setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right,
                R.anim.slide_in_right,
                R.anim.slide_out_left,
            )

        // Not implement
        null -> {}
    }
}

fun delayTask(
    func: () -> Unit,
    duration: Long = 1000,
) {
    Handler().postDelayed(func, duration)
}

fun setupDismissKeyBoard(
    context: Activity?,
    view: View?,
) {
    // Set up touch listener for non-text box views to hide keyboard.
    if (view !is EditText) {
        view?.setOnTouchListener { _, _ ->
            val inputMethodManager =
                context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)

            false
        }
    }

    // If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            setupDismissKeyBoard(context, innerView)
        }
    }
}

// fun Fragment.initToolbar(
//    layoutToolbarBinding: LayoutToolbarBinding? = null,
//    title: String?,
//    iconRight: Int? = null,
//    isShowIconLeft: Boolean = true,
//    setLeftOnClickListener: (() -> Boolean)? = null,
//    setRightOnClickListener: (() -> Unit)? = null
// ) {
//    layoutToolbarBinding?.apply {
//        titleToolbarTextView.text = title
//        rightImageButton.apply {
//            iconRight?.let {
//                visible()
//                setBackgroundResource(it)
//            } ?: invisible()
//            setOnClickListener {
//                it.onClickWithAnimation {
//                    setRightOnClickListener?.invoke()
//                }
//            }
//        }
//        leftImageButton.apply {
//            if (isShowIconLeft) {
//                visible()
//                setOnClickListener {
//                    it.onClickWithAnimation {
//                        if (setLeftOnClickListener?.invoke() == true) {
//                            return@onClickWithAnimation
//                        }
//                        this@initToolbar.goBackFragment()
//                    }
//                }
//            } else invisible()
//        }
//    }
// }

// fun Fragment.initSearchToolBar(
//    searchToolbarBinding: LayoutSearchToolbarBinding? = null,
//    hint: String? = getString(R.string.find_your_group),
//    defaultKeyword: String? = "",
//    iconRight: Int? = null,
//    isShowIconLeft: Boolean = true,
//    setLeftOnClickListener: (() -> Boolean)? = null,
//    setRightOnClickListener: (() -> Unit)? = null,
//    setDoOnTextChanged: ((String) -> Unit)? = null
// ) {
//    searchToolbarBinding?.apply {
//        editTextSearch.apply {
//            setHint(hint)
//            if (!defaultKeyword.isNullOrEmpty()) setText(defaultKeyword)
//            defaultKeyword?.let {
//                if (it.length > 1)
//                    setSelection(it.length)
//            }
//            requestFocus()
//            onShowSoftKeyBoard()
//            doOnTextChanged { text, start, count, after ->
//                setDoOnTextChanged?.invoke(text.toString())
//            }
//        }
//
//        buttonSearch.apply {
//            iconRight?.let {
//                visible()
//                setBackgroundResource(it)
//            } ?: gone()
//            setOnClickListener {
//                it.onClickWithAnimation {
//                    setRightOnClickListener?.invoke()
//                }
//            }
//        }
//        leftImageButton.apply {
//            if (isShowIconLeft) {
//                visible()
//                setOnClickListener {
//                    it.onClickWithAnimation {
//                        if (setLeftOnClickListener?.invoke() == true) {
//                            return@onClickWithAnimation
//                        }
//                        this@initSearchToolBar.goBackFragment()
//                    }
//                }
//            } else gone()
//        }
//    }
// }

// fun Fragment.showItemsAlertDialog(
//    items: List<AlertDialogItem>,
//    isShowSelected: Boolean = true,
//    completion: ItemsAlertDialog.() -> Unit
// ) = ItemsAlertDialog(
//    requireContext(),
//    items,
//    isShowSelected
// ).apply {
//    completion.invoke(this)
//    show()
// }
//
// fun Fragment.showDatePickerAlertDialog(
//    dayOfMonth: Int,
//    month: Int,
//    year: Int,
//    completion: DatePickerAlertDialog.() -> Unit
// ) = DatePickerAlertDialog(requireContext()).apply {
//    initDatePicker(dayOfMonth, month, year)
//    completion.invoke(this)
//    show()
// }
