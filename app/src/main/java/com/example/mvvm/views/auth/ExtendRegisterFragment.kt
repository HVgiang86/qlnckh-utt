package com.example.mvvm.views.auth

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.mvvm.R
import com.example.mvvm.base.BGType
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentExtendRegisterBinding
import com.example.mvvm.domain.UserRole
import com.example.mvvm.utils.ext.addFragment
import com.example.mvvm.utils.ext.goBackFragment
import com.example.mvvm.views.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ExtendRegisterFragment : BaseFragment<FragmentExtendRegisterBinding, AuthViewModel>() {
    override val viewModel: AuthViewModel by viewModels()

    private var registerInfo: RegisterInfo? = null

    fun setUp() {
        viewBinding.apply {
            layoutTitle.setOnClickListener {
                goBackFragment()
            }
        }

        viewBinding.btnSignUp.setOnClickListener {
            onClickSignUp()
        }

        viewBinding.edtKhoa.hint = when (registerInfo?.role) {
            UserRole.RESEARCHER -> {
                "Khoa"
            }

            UserRole.SUPERVISOR -> {
                "Học hàm/Học vị"
            }

            UserRole.ADMIN -> {
                ""
            }

            null -> {
                goBackFragment()
                ""
            }
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater) = FragmentExtendRegisterBinding.inflate(inflater)
    override fun initialize() {
        arguments?.takeIf { it.containsKey(ARG_REGISTER_INFO) }?.let {
            registerInfo = it.getSerializable(ARG_REGISTER_INFO) as RegisterInfo
        }
        Timber.d("registerInfo: $registerInfo")
        setUp()
    }

    private fun onClickSignUp() {
        val studentCode = viewBinding.edtStudentCode.text?.trim().toString()
        val lop = viewBinding.edtClass.text?.trim().toString()
        val khoa = viewBinding.edtKhoa.text?.trim().toString()
        if (studentCode.isEmpty()) {
            showMessage("Mã sinh viên không hợp lệ!", BGType.BG_TYPE_ERROR)
            return
        }
        if (lop.isEmpty()) {
            showMessage("Lớp không hợp lệ!", BGType.BG_TYPE_ERROR)
            return
        }
        if (khoa.isEmpty()) {
            showMessage("Khoa không hợp lệ!", BGType.BG_TYPE_ERROR)
            return
        }
        showMessage("Đăng ký thành công!", BGType.BG_TYPE_SUCCESS)

        addFragment(R.id.container, HomeFragment.newInstance())
    }

    companion object {
        const val ARG_REGISTER_INFO = "registerInfo"

        fun newInstance(registerInfo: RegisterInfo) = ExtendRegisterFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_REGISTER_INFO, registerInfo)
            }
        }
    }
}
