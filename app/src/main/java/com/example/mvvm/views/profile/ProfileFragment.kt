package com.example.mvvm.views.profile

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.data.source.api.model.request.UpdateProfileRequest
import com.example.mvvm.databinding.FragmentProfileBinding
import com.example.mvvm.utils.ext.isVisible
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override val viewModel: ProfileViewModel by viewModels()

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProfile()
    }

    override fun initialize() {
        registerLiveData()

        viewBinding.layoutBirthday.setOnClickListener {
            openPickDate { day, month, year ->
                viewBinding.tvDate.text = "$year-$month-$day"
            }
        }

        viewModel.profileResearcher.observe(viewLifecycleOwner) {
            viewBinding.edtName.setText(it.name ?: "")
            viewBinding.edtEmail.setText(it.email ?: "")
            viewBinding.tvDate.text = it.dob ?: ""
            viewBinding.edtFaculty.setText(it.major ?: "")
            viewBinding.edtClassDepartment.hint = "Lớp"
            viewBinding.edtClassDepartment.setText(it.className ?: "")
            viewBinding.edtStudentCodeTitle.hint = "Mã sinh viên"
            viewBinding.edtStudentCodeTitle.setText(it.studentCode ?: "")
        }

        viewModel.profileSuperVisor.observe(viewLifecycleOwner) {
            viewBinding.edtName.setText(it.name ?: "")
            viewBinding.edtEmail.setText(it.email ?: "")
            viewBinding.tvDate.text = it.dob ?: ""
            viewBinding.edtFaculty.setText(it.faculty ?: "")
            viewBinding.edtClassDepartment.hint = "Phòng ban"
            viewBinding.edtClassDepartment.setText(it.department ?: "")
            viewBinding.edtStudentCodeTitle.hint = "Học hàm/học vị"
            viewBinding.edtStudentCodeTitle.setText(it.title ?: "")
        }

        viewModel.profileAdmin.observe(viewLifecycleOwner) {
            viewBinding.edtName.setText(it.fullName)
            viewBinding.edtEmail.setText(it.email)
            viewBinding.tvDate.text = it.dateOfBirth
            viewBinding.edtFaculty.setText(it.phoneNumber ?: "")
            viewBinding.edtClassDepartment.visibility = View.GONE
            viewBinding.edtStudentCodeTitle.visibility = View.GONE
        }

        viewBinding.topAppBar.menu.getItem(0).setOnMenuItemClickListener {
            val email = viewModel.profileResearcher.value?.email ?: viewModel.profileSuperVisor.value?.email ?: ""
            val request = UpdateProfileRequest(
                fullName = viewBinding.edtName.text.toString(),
                birthday = viewBinding.tvDate.text.toString(),
                faculty = viewBinding.edtFaculty.text.toString(),
                department = viewBinding.edtClassDepartment.text.toString(),
                title = viewBinding.edtStudentCodeTitle.text.toString(),
                major = viewBinding.edtFaculty.text.toString(),
                className = viewBinding.edtClassDepartment.text.toString(),
            )

            if (email.isNotEmpty()) {
                viewModel.updateProfile(email, request)
            } else {
                Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_LONG).show()
            }

            true
        }
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

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
