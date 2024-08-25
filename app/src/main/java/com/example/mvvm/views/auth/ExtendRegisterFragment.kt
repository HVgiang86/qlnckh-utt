package com.example.mvvm.views.auth

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.mvvm.R
import com.example.mvvm.base.BGType
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentExtendRegisterBinding
import com.example.mvvm.domain.UserRole
import com.example.mvvm.utils.ext.addClearBackStackFragment
import com.example.mvvm.utils.ext.goBackFragment
import com.example.mvvm.utils.ext.gone
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

        viewBinding.edtStudentCodeTitle.hint = when (registerInfo?.role) {
            UserRole.RESEARCHER -> {
                "Mã sinh viên"
            }

            UserRole.SUPERVISOR -> {
                "Học hàm/Học vị"
            }

            UserRole.ADMIN -> {
                viewBinding.edtStudentCodeTitle.gone()
                ""
            }

            null -> {
                goBackFragment()
                ""
            }
        }

        viewBinding.edtClassDepartment.hint = when (registerInfo?.role) {
            UserRole.RESEARCHER -> {
                "Lớp"
            }

            UserRole.SUPERVISOR -> {
                "Phòng ban"
            }

            UserRole.ADMIN -> {
                viewBinding.edtClassDepartment.gone()
                ""
            }

            null -> {
                goBackFragment()
                ""
            }
        }

        viewBinding.edtFaculty.hint = when (registerInfo?.role) {
            UserRole.RESEARCHER, UserRole.SUPERVISOR -> {
                "Khoa"
            }

            UserRole.ADMIN -> {
                viewBinding.edtFaculty.gone()
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
        registerErrorHandler()

        arguments?.takeIf { it.containsKey(ARG_REGISTER_INFO) }?.let {
            registerInfo = it.getSerializable(ARG_REGISTER_INFO) as RegisterInfo
        }
        Timber.d("registerInfo: $registerInfo")
        setUp()

        if (registerInfo?.role == UserRole.ADMIN) {
            viewBinding.edtStudentCodeTitle.gone()
            viewBinding.edtClassDepartment.gone()
            viewBinding.edtFaculty.gone()
        }

        viewModel.signUpSuccess.observe(viewLifecycleOwner) {
            if (it) {
                showMessage("Đăng ký thành công!", BGType.BG_TYPE_SUCCESS)
                addClearBackStackFragment(R.id.container, SignInFragment.newInstance())
            }
        }
    }

    private fun onClickSignUp() {
        val studentCodeTitle = viewBinding.edtStudentCodeTitle.text?.trim().toString()
        val classDepartment = viewBinding.edtClassDepartment.text?.trim().toString()
        val faculty = viewBinding.edtFaculty.text?.trim().toString()
        if (studentCodeTitle.isEmpty()) {
            if (registerInfo?.role == UserRole.ADMIN) {
                showMessage("Vui lòng nhập học vị/học hàm!", BGType.BG_TYPE_ERROR)
            } else {
                showMessage("Vui lòng nhập mã sinh viên!", BGType.BG_TYPE_ERROR)
            }
            return
        }
        if (classDepartment.isEmpty()) {
            if (registerInfo?.role == UserRole.ADMIN) {
                showMessage("Vui lòng nhập phòng ban!", BGType.BG_TYPE_ERROR)
            } else {
                showMessage("Vui lòng nhập lớp!", BGType.BG_TYPE_ERROR)
            }
            return
        }
        if (faculty.isEmpty()) {
            showMessage("Vui lòng nhập khoa!", BGType.BG_TYPE_ERROR)
            return
        }

        val toRegister = RegisterInfo(
            name = registerInfo?.name.toString(),
            email = registerInfo?.email.toString(),
            password = registerInfo?.password.toString(),
            birthday = registerInfo?.birthday.toString(),
            role = UserRole.RESEARCHER,
            faculty = faculty,
            department = classDepartment,
            title = studentCodeTitle,
            major = faculty,
            className = classDepartment,
        )

        if (registerInfo?.role == UserRole.SUPERVISOR) {
            viewModel.signUpSupervisor(toRegister)
        } else if (registerInfo?.role == UserRole.RESEARCHER) {
            viewModel.signUpResearcher(toRegister)
        }
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
