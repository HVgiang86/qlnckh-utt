package com.example.mvvm.views

import android.content.Context
import android.view.LayoutInflater
import com.example.mvvm.R
import com.example.mvvm.base.BaseActivity
import com.example.mvvm.databinding.ActivityMainBinding
import com.example.mvvm.utils.ext.addFragmentToActivity
import com.example.mvvm.views.auth.SignInFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initialize() {
        addFragmentToActivity(R.id.container, SignInFragment.newInstance(), addToBackStack = true)
    }

    override val context: Context
        get() = this
}
