package com.example.mvvm.views.auth

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.mvvm.R
import com.example.mvvm.base.BGType
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentSignUpBinding
import com.example.mvvm.domain.UserRole
import com.example.mvvm.utils.ext.addFragment
import com.example.mvvm.utils.ext.goBackFragment
import com.example.mvvm.utils.utils.AppUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding, AuthViewModel>() {
    override val viewModel: AuthViewModel by viewModels()

    private val authViewModel: AuthViewModel by viewModels()

    private fun setUp() {
        registerLiveData()

        viewBinding.apply {
            btnNext.setOnClickListener {
                onClickNext()
            }
            layoutTitle.setOnClickListener {
                goBackFragment()
            }
            layoutBirthday.setOnClickListener {
                openPickDate { day, month, year ->
                    tvDate.text = "$year-$month-$day"
                }
            }
        }

        collectLifecycleFlow(authViewModel.responseMessage) {
            showMessage(it.message, it.bgType)
        }
        collectLifecycleFlow(authViewModel.isLoading) {
            if (it) showLoading() else hideLoading()
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater) = FragmentSignUpBinding.inflate(inflater)

    override fun initialize() {
        setUp()
    }

    private fun openPickDate(onPicked: (String, String, String) -> Unit) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), { _, y, monthOfYear, dayOfMonth ->
            val d = if (dayOfMonth >= 10) dayOfMonth.toString() else "0$dayOfMonth"
            val m = if (monthOfYear + 1 >= 10) (monthOfYear + 1).toString() else "0${monthOfYear + 1}"
            onPicked(d, m, "$y")
        }, year, month, day)
        dpd.show()
    }

    @SuppressLint("SetTextI18n")
    private fun onClickNext() {
        hideKeyboard()
        val email = viewBinding.edtEmail.text?.trim().toString()
        val name = viewBinding.edtName.text?.trim().toString()
        val pass = viewBinding.edtPassword.text?.trim().toString()
        val confirm = viewBinding.edtConfirmPassword.text?.trim().toString()
        val birthday = viewBinding.tvDate.text.toString()
        if (!AppUtils.isValidatedName(name)) {
            showMessage("Tên không hợp lệ!", BGType.BG_TYPE_ERROR)
            viewBinding.edtName.setText("")
            return
        }
        if (birthday.isEmpty()) {
            showMessage("Ngày sinh không hợp lệ!", BGType.BG_TYPE_ERROR)
            return
        }
        if (!AppUtils.isValidatedEmail(email)) {
            showMessage("Email không hợp lệ!", BGType.BG_TYPE_ERROR)
            viewBinding.edtEmail.setText("")
            return
        }
        if (!AppUtils.isValidatedPassword(pass)) {
            showMessage("Mật khẩu không hợp lệ!", BGType.BG_TYPE_ERROR)
            viewBinding.edtPassword.setText("")
            return
        }
        if (!AppUtils.isValidatedConfirmPass(pass, confirm)) {
            showMessage("Xác nhận mật khẩu không hợp lệ!", BGType.BG_TYPE_ERROR)
            viewBinding.edtConfirmPassword.setText("")
            return
        }
        val value = RegisterInfo(
            name = name,
            email = email,
            password = pass,
            birthday = birthday,
            role = if (viewBinding.rbNCS.isChecked) UserRole.RESEARCHER else UserRole.SUPERVISOR,
            faculty = "",
            department = "",
            title = "",
            major = "",
            className = "",
        )
        addFragment(R.id.container, ExtendRegisterFragment.newInstance(value), addToBackStack = true)
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}
