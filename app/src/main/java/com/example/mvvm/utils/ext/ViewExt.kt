package com.example.mvvm.utils.ext

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.SystemClock
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar

var lastClickTime = 0L

fun handleMultiClick(companion: () -> Unit) {
    if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
        return
    }
    lastClickTime = SystemClock.elapsedRealtime()
    companion()
}

fun View.onClickWithAnimation(block: () -> Unit) {
    val scaleX =
        ObjectAnimator
            .ofFloat(this, View.SCALE_X, 1.0f, 1.05f)
            .setDuration(200)
    val scaleY =
        ObjectAnimator
            .ofFloat(this, View.SCALE_Y, 1.0f, 1.05f)
            .setDuration(200)
    val downscaleX =
        ObjectAnimator
            .ofFloat(this, View.SCALE_X, 1.05f, 1.0f)
            .setDuration(200)
    val downscaleY =
        ObjectAnimator
            .ofFloat(this, View.SCALE_Y, 1.05f, 1.0f)
            .setDuration(200)
    AnimatorSet().apply {
        playTogether(scaleX, scaleY)
        play(downscaleX).after(scaleX)
        playTogether(downscaleX, downscaleY)
        start()
    }
    handleMultiClick {
        block()
    }
}

fun View.setOnUnMultiClickListener(block: () -> Unit) {
    setOnClickListener {
        handleMultiClick {
            block()
        }
    }
}

fun View.setOnUnMultiClickListener(listener: View.OnClickListener) {
    setOnClickListener {
        handleMultiClick {
            listener.onClick(it)
        }
    }
}

fun View.showSnackBar(data: Any) {
    val message =
        when (data) {
            is Int -> context.getString(data)
            is Throwable -> data.message.toString()
            else -> data.toString()
        }
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun Context.showToast(data: Any) {
    val message =
        when (data) {
            is Int -> getString(data)
            is Throwable -> data.message
            else -> data.toString()
        }
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.setColorDrawable(
    drawable: Int,
    color: Any?,
) = ResourcesCompat.getDrawable(resources, drawable, context?.theme)?.let {
    val wrappedDrawable = DrawableCompat.wrap(it)
    background = wrappedDrawable
    when (color) {
        is Int ->
            DrawableCompat.setTint(wrappedDrawable.mutate(), resources.getColor(color, context?.theme))

        is String ->
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(color))
    }
}

fun View.setColorDrawable(color: Any?) =
    apply {
        when (color) {
            is Int ->
                DrawableCompat.setTint(background, resources.getColor(color, context?.theme))

            is String ->
                DrawableCompat.setTint(background, Color.parseColor(color))
        }
    }

fun View.setBackgroundDrawable(
    @DrawableRes resId: Int,
) = ContextCompat.getDrawable(context, resId).also {
    background = it
}

fun View.OnClickListener.listenToViews(vararg views: View) {
    views.forEach { it.setOnClickListener(this) }
}

fun View.OnClickListener.listenOnSafeClickedViews(vararg views: View) {
    views.forEach { it.setOnUnMultiClickListener(this) }
}

fun View.setAnimationResource(
    resId: Int,
    duration: Long = 250,
    onAfter: () -> Unit = {},
) {
    try {
        val animation =
            AnimationUtils.loadAnimation(context, resId).apply {
                this.duration = duration
                setAnimationListener(
                    object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            // TODO implement later
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            onAfter()
                        }

                        override fun onAnimationRepeat(animation: Animation?) {
                            // TODO implement later
                        }
                    },
                )
            }
        startAnimation(animation)
    } catch (e: Exception) {
        // Resource not found
    }
}

fun View.showViewWithAnimation() {
    alpha = 0.2f
    isVisible = true
    animate().alpha(1f).setListener(null).duration = DURATION_SHOW_VIEW
}

fun View.hideViewWithAnimation() {
    alpha = 1f
    isVisible = false
    animate().alpha(0f).setListener(null).duration = DURATION_SHOW_VIEW
}

fun View.onHideSoftKeyBoard() {
    val inputMng: InputMethodManager =
        context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMng.hideSoftInputFromWindow(windowToken, 0)
}

fun View.onShowSoftKeyBoard() {
    val inputMng: InputMethodManager =
        context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMng.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

const val DURATION_SHOW_VIEW = 1500L

fun View.isVisible() = visibility == View.VISIBLE

fun View.isGone() = visibility == View.GONE

fun View.isInvisible() = this.visibility == View.INVISIBLE

fun View.visible() {
    if (!isVisible()) this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun visibleViews(vararg views: View?) {
    views.map { it?.visible() }
}

fun invisibleViews(vararg views: View?) {
    views.map { it?.invisible() }
}

fun goneViews(vararg views: View?) {
    views.map { it?.gone() }
}

fun View.safeClick(
    blockInMillis: Long = 1000L,
    onClick: (View) -> Unit,
) {
    var lastClickTime = 0L
    this.setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickTime < blockInMillis) return@setOnClickListener
        lastClickTime = SystemClock.elapsedRealtime()
        onClick(this)
    }
}
