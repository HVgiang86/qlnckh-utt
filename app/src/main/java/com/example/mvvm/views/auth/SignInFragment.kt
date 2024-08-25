package com.example.mvvm.views.auth

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.mvvm.R
import com.example.mvvm.base.BGType
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentSignInBinding
import com.example.mvvm.domain.AppState
import com.example.mvvm.utils.ext.addClearBackStackFragment
import com.example.mvvm.utils.ext.addFragment
import com.example.mvvm.views.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding, AuthViewModel>() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    override val viewModel: AuthViewModel by viewModels()

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentSignInBinding {
        return FragmentSignInBinding.inflate(inflater)
    }

    override fun initialize() {
        registerLiveData()

        Glide.with(this).load(activity?.getDrawable(R.drawable.ic_uni)).centerInside().into(viewBinding.imageLogo)

        viewBinding.apply {
            tvTitle.text = SpannableString("Welcome Back").apply {
                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.blue_dark)),
                    8,
                    12,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
            }

            btnLogin.setOnClickListener {
                onClickLogin()
            }

            layoutSignUp.setOnClickListener {
                addFragment(R.id.container, SignUpFragment.newInstance(), addToBackStack = true)
            }
        }

        viewModel.userRole.observe(viewLifecycleOwner) {
            if (it != null) {
                AppState.userRole = it
                AppState.logined = true
                showMessage("Đăng nhập thành công!", BGType.BG_TYPE_SUCCESS)
                addClearBackStackFragment(R.id.container, HomeFragment.newInstance())
            }
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.getProfile()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onClickLogin() {
        hideKeyboard()
        println("onClickLogin")
        val email = viewBinding.edtEmail.text?.trim().toString()
        val pass = viewBinding.edtPassword.text?.trim().toString()

//        if (!AppUtils.isValidatedEmail(email)) {
//            showMessage("Email không hợp lệ!", BGType.BG_TYPE_ERROR)
//            viewBinding.edtEmail.setText("")
//            return
//        }
//        if (!AppUtils.isValidatedPassword(pass)) {
//            showMessage("Mật khẩu không hợp lệ!", BGType.BG_TYPE_ERROR)
//            viewBinding.edtPassword.setText("")
//            return
//        }
        viewModel.signIn(email, pass)
//        viewModel.signIn("sv3@gmail.com", "123456")
    }
}
