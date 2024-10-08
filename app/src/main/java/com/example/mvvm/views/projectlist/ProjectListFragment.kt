package com.example.mvvm.views.projectlist

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.R
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentProjectBinding
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.UserRole
import com.example.mvvm.utils.ext.addFragment
import com.example.mvvm.utils.ext.gone
import com.example.mvvm.utils.ext.visible
import com.example.mvvm.views.add.AddFragment
import com.example.mvvm.views.projectdetail.ProjectDetailFragment
import com.example.mvvm.views.projectlist.adapter.ProjectListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectListFragment : BaseFragment<FragmentProjectBinding, ProjectListViewModel>() {
    private val adapter: ProjectListAdapter by lazy { ProjectListAdapter() }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProject()
    }

    private fun setData(data: List<Project>) {
        adapter.setData(data)
    }

    private fun openAddProject() {
    }

    override val viewModel: ProjectListViewModel by viewModels()

    private fun initFAB() {
        val fabType = getProjectListFAB(AppState.userRole)
        println("userRole: ${AppState.userRole} hasInChargeProject: ${AppState.hasInChargeProject}")
        if (AppState.hasInChargeProject && AppState.userRole == UserRole.RESEARCHER) {
            viewBinding.fab.gone()
            viewBinding.fab2.gone()
        } else {
            viewBinding.fab.visible()
            viewBinding.fab2.visible()
        }

        when (fabType) {
            ProjectListFAB.NEW -> {
                viewBinding.fab.text = "Thêm"
                viewBinding.fab2.text = "Thêm"
            }

            ProjectListFAB.PROPOSED -> {
                viewBinding.fab.text = "Đề xuất"
                viewBinding.fab2.text = "Đề xuất"
            }

            null -> {
                viewBinding.fab.gone()
                viewBinding.fab2.gone()
            }
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater) = FragmentProjectBinding.inflate(inflater)
    override fun initialize() {
        viewBinding.swipeRefresh.isRefreshing = false
        viewBinding.swipeRefresh.setEnabled(false)
        registerErrorHandler()
        lifecycleScope.launch {
            viewModel.isLoading.collect {
                if (it) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        }

        viewBinding.swipeRefresh.setOnRefreshListener {
            viewModel.getAllProject()
        }

        viewBinding.listProject.adapter = adapter
        viewBinding.listProject.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnItemClickListener {
            addFragment(R.id.container, ProjectDetailFragment.newInstance(it), addToBackStack = true)
        }

        viewModel.projects.observe(viewLifecycleOwner) {
            println("..")
            initFAB()
            if (it.isNullOrEmpty()) {
                viewBinding.containerNoInCharge.visible()
                viewBinding.containerHasInCharge.gone()
            } else {
                viewBinding.containerNoInCharge.gone()
                viewBinding.containerHasInCharge.visible()
                setData(it)
            }
        }

        viewBinding.fab.setOnClickListener {
            addFragment(R.id.container, AddFragment.newInstance(1, null), addToBackStack = true)
        }

        viewBinding.fab2.setOnClickListener {
            addFragment(R.id.container, AddFragment.newInstance(1, null), addToBackStack = true)
        }

        viewModel.getAllProject()
    }

    companion object {
        fun newInstance() = ProjectListFragment()
    }
}
