package com.example.mvvm.views.projectdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.R
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentProjectDetailBinding
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.Project
import com.example.mvvm.utils.ext.addFragment
import com.example.mvvm.utils.ext.getStateName
import com.example.mvvm.utils.ext.getStateTagBackground
import com.example.mvvm.utils.ext.goBackFragment
import com.example.mvvm.utils.ext.gone
import com.example.mvvm.views.assign.AssignFragment
import com.example.mvvm.views.incharge.ProjectFAB
import com.example.mvvm.views.incharge.adapter.DocumentAdapter
import com.example.mvvm.views.incharge.getBackgroundColor
import com.example.mvvm.views.incharge.getIcon
import com.example.mvvm.views.incharge.getProjectFAB
import com.example.mvvm.views.incharge.getText
import com.example.mvvm.views.incharge.getTextColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectDetailFragment : BaseFragment<FragmentProjectDetailBinding, ProjectDetailViewModel>() {
    override val viewModel: ProjectDetailViewModel by viewModels()
    private var project: Project? = null
    var fabType: ProjectFAB? = null

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentProjectDetailBinding {
        return FragmentProjectDetailBinding.inflate(inflater)
    }

    override fun initialize() {
        registerErrorHandler()
        project = arguments?.let {
            it.getSerializable(KEY_PROJECT) as Project
        }
        project?.let {
            setData(it)
        }
    }

    override fun onBackPress(): Boolean {
        goBackFragment()
        return true
    }

    private fun setupObserver() {
        viewModel.response.observe(this) {
            goBackFragment()
        }
    }

    private fun setData(data: Project) {
        viewBinding.topAppBar.setNavigationOnClickListener {
            goBackFragment()
        }
        setupObserver()

        viewBinding.textTitle.text = data.title
        viewBinding.textDescription.text = data.description
        viewBinding.textStatusTag.background = data.state?.let { resources.getDrawable(it.getStateTagBackground()) }
        viewBinding.textStatusTag.text = data.state?.getStateName() ?: "Unknown"

        val researcherAdapter = data.researcher?.let { ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it.map { it.name }) }
        viewBinding.listResearcher.adapter = researcherAdapter

        val supervisorAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listOf(data.supervisor?.name))
        viewBinding.listSupervisor.adapter = supervisorAdapter

        val documentAdapter = DocumentAdapter()
        viewBinding.listDocument.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.listDocument.adapter = documentAdapter
        data.documents?.let { documentAdapter.setData(it) }

        fabType = data.state?.let { getProjectFAB(it, AppState.userRole) }

        println("fabType: $fabType")

        if (fabType == null) {
            viewBinding.fab.gone()
        } else {
            viewBinding.fab.show()
            viewBinding.fab.icon = requireContext().getDrawable(fabType!!.getIcon())
            viewBinding.fab.backgroundTintList = resources.getColorStateList(fabType!!.getBackgroundColor())
            viewBinding.fab.setTextColor(resources.getColor(fabType!!.getTextColor()))
            viewBinding.fab.text = fabType!!.getText()
            viewBinding.fab.iconTint = resources.getColorStateList(fabType!!.getTextColor())
        }

        viewBinding.fab.setOnClickListener {
            when (fabType) {
                null -> {}/* no-op */
                ProjectFAB.ASSIGN -> addFragment(R.id.container, AssignFragment.newInstance(data.id, 2), addToBackStack = true)
                ProjectFAB.APPROVE -> addFragment(R.id.container, AssignFragment.newInstance(data.id, 1), addToBackStack = true)
                ProjectFAB.SCORE -> showMarkScoreDialog(data.id)
                ProjectFAB.MARK_FINISH -> {
                    val dialog = com.example.mvvm.utils.widget.AlertDialog(requireContext())
                    dialog.title("Xác nhận")
                    dialog.message("Bạn có chắc chắn muốn đánh dấu dự án này được phép bảo vệ?")
                    dialog.leftButton("OK") {
                        viewModel.markFinish(data.id)
                    }
                    dialog.rightButton("Hủy") {
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            }
        }
    }

    private fun showMarkScoreDialog(projectId: Long) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.score_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).setView(dialogView)
        val dialog = dialogBuilder.create()
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btnSubmit)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            val etScore = dialogView.findViewById<EditText>(R.id.etScore)
            val score = etScore.text.toString().toDoubleOrNull()

            if (score != null && score >= 0 && score <= 10) {
                viewModel.setTopicScore(projectId, score)
            } else {
                Toast.makeText(requireContext(), "Invalid score entered", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        const val KEY_PROJECT = "KEY_PROJECT"
        fun newInstance(project: Project) = ProjectDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_PROJECT, project)
            }
        }
    }
}
