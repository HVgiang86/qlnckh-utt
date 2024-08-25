package com.example.mvvm.views

import android.content.Context
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mvvm.R
import com.example.mvvm.base.BGType
import com.example.mvvm.base.BaseActivity
import com.example.mvvm.databinding.ActivityMainBinding
import com.example.mvvm.utils.ext.addFragmentToActivity
import com.example.mvvm.views.auth.SignInFragment
import com.example.mvvm.views.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    val viewModel: MainViewModel by viewModels()

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initialize() {
        if (viewModel.getLoginState()) {
            viewModel.getProfile()
        } else {
            navigateToLogin()
        }

        lifecycleScope.launch {
            viewModel.responseMessage.collect {
                showMessage(it.message, it.bgType)
                navigateToLogin()
            }
        }

        viewModel.canAutoLogin.observe(this) {
            if (it) {
                navigateToHome()
            }
        }
    }

    override val context: Context
        get() = this

    fun navigateToLogin() {
        viewModel.clearLoggedIn()
        addFragmentToActivity(R.id.container, SignInFragment.newInstance(), addToBackStack = true)
    }

    fun navigateToHome() {
        showMessage("Đăng nhập thành công!", BGType.BG_TYPE_SUCCESS)
        viewModel.setLoggedIn()
        addFragmentToActivity(R.id.container, HomeFragment.newInstance(), addToBackStack = true)
    }
}
