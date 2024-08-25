package com.example.mvvm.views.projectdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentProjectDetailBinding
import com.example.mvvm.domain.Project
import com.example.mvvm.utils.ext.getStateName
import com.example.mvvm.utils.ext.getStateTagBackground
import com.example.mvvm.utils.ext.goBackFragment
import com.example.mvvm.views.incharge.adapter.DocumentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectDetailFragment : BaseFragment<FragmentProjectDetailBinding, ProjectDetailViewModel>() {
    override val viewModel: ProjectDetailViewModel by viewModels()
    private var project: Project? = null

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentProjectDetailBinding {
        return FragmentProjectDetailBinding.inflate(inflater)
    }

    override fun initialize() {
        registerLiveData()
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

    private fun setData(data: Project) {
        viewBinding.topAppBar.setNavigationOnClickListener {
            goBackFragment()
        }

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
