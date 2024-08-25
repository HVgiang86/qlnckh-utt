package com.example.mvvm.views.assign

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentAssignBinding
import com.example.mvvm.domain.Supervisor
import com.example.mvvm.utils.ext.goBackFragment
import com.example.mvvm.utils.formatDateToDDMMYYYY
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class AssignFragment : BaseFragment<FragmentAssignBinding, AssignViewModel>() {
    override val viewModel: AssignViewModel by viewModels()

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentAssignBinding {
        return FragmentAssignBinding.inflate(inflater)
    }

    override fun initialize() {
        setUp()
    }

    private val visorAdapter by lazy {
        ItemAdapter(onClickAdd = {
            showSupervisorSelectionDialog(viewModel.supervisorData.value ?: emptyList())
        }, onClickRemove = { item ->
            when (item) {
                Item.AddItem -> TODO()
                is Item.SuperVisorItem -> viewModel.removeSuperVisor(item.supervisor)
                is Item.TitleItem -> TODO()
            }

        })
    }

    fun setUp() {

        val type = arguments?.getInt(KEY_TYPE, 0)
        val projectId = arguments?.getLong(KEY_PROJECT_ID, 0)
        setupUI(type ?: 0)
        viewModel.getAllSupervisor()
        setupObserver()
        viewBinding.layoutDate.setOnClickListener {
            openPickDate { day, month, year ->
                viewBinding.tvDate.text = "$day/$month/$year"
            }
        }
        viewBinding.btnSave.setOnClickListener {
            if (projectId != null) {
                if (type == 1) {
                    viewModel.approveProject(topicId = projectId)
                } else {
                    viewModel.assignProject(topicId = projectId, viewBinding.tvDate.text.toString())
                }
            }
        }

        viewBinding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
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

    private fun setupObserver() {
        viewModel.supervisors.observe(this) {
            visorAdapter.setItems(it.map { email ->
                Item.SuperVisorItem(email)
            }, "Supervisor")
        }

        viewModel.response.observe(this) {
            goBackFragment()
        }
    }
    

    private fun showSupervisorSelectionDialog(supervisors: List<Supervisor>) {
        val supervisorNames = supervisors.map { it.name }.toTypedArray()
        var selectedSupervisorIndex = -1
        AlertDialog.Builder(requireContext())
            .setTitle("Select Supervisor")
            .setSingleChoiceItems(supervisorNames, selectedSupervisorIndex) { _, which ->
                // Update the selected index when the user selects an option
                selectedSupervisorIndex = which
            }
            .setPositiveButton("OK") { dialog, _ ->
                if (selectedSupervisorIndex != -1) {
                    // Get the selected supervisor
                    val selectedSupervisor = supervisors[selectedSupervisorIndex]

                    // Handle the selected supervisor (e.g., send data to ViewModel)
                    onSupervisorSelected(selectedSupervisor)
                } else {
                    Toast.makeText(requireContext(), "No supervisor selected", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun onSupervisorSelected(supervisor: Supervisor) {
        supervisor.email?.let { viewModel.addSuperVisor(it) }
    }



    @SuppressLint("SetTextI18n")
    fun setupUI(type: Int) {
        viewBinding.tvDate.text = formatDateToDDMMYYYY(Date())
        viewBinding.rcvDocument.apply {
            adapter = visorAdapter
            layoutManager = LinearLayoutManager(requireContext())
            visorAdapter.setItems(listOf(), "SuperVisor")
        }
        if (type == 1) {
            viewBinding.tvDate.visibility = View.GONE
            viewBinding.layoutDate.visibility = View.GONE
            viewBinding.tvToolbar.text = "Approve"

        } else {
            viewBinding.tvDate.visibility = View.VISIBLE
            viewBinding.layoutDate.visibility = View.VISIBLE
            viewBinding.tvToolbar.text = "Assign"
            visorAdapter.setType(2)
        }

    }

    companion object {
        const val KEY_TYPE = "type"
        const val KEY_PROJECT_ID = "projectId"

        fun newInstance(projectId: Long, type: Int) = AssignFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_PROJECT_ID, projectId)
                putInt(KEY_TYPE, type)
            }
        }
    }

}
