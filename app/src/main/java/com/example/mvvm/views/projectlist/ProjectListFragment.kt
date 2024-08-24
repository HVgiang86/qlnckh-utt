package com.example.mvvm.views.projectlist

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentProjectBinding
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.Project
import com.example.mvvm.utils.ext.gone
import com.example.mvvm.utils.ext.visible
import com.example.mvvm.views.projectlist.adapter.ProjectListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectListFragment : BaseFragment<FragmentProjectBinding, ProjectListViewModel>() {
    private val adapter: ProjectListAdapter by lazy { ProjectListAdapter() }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProject()
    }

    private fun setData(data: List<Project>) {
        adapter.setData(data)

        val fabType = getProjectListFAB(AppState.userRole)
        viewBinding.fab.show()
        when (fabType) {
            ProjectListFAB.NEW -> {
                viewBinding.fab.text = "Thêm"
            }

            ProjectListFAB.PROPOSED -> {
                viewBinding.fab.text = "Đề xuất"
            }
        }
    }

    private fun openAddProject() {
    }

    override val viewModel: ProjectListViewModel by viewModels()

    override fun inflateViewBinding(inflater: LayoutInflater) = FragmentProjectBinding.inflate(inflater)
    override fun initialize() {
        viewBinding.listProject.adapter = adapter
        viewBinding.listProject.layoutManager = LinearLayoutManager(requireContext())

        viewModel.projects.observe(viewLifecycleOwner) {
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
//            Intent(activity, AddActivity::class.java).apply {
//                putExtra("type", AddActivity.TYPE_ADD_PROJECT)
//                startActivity(this)
//            }
        }

        viewModel.getAllProject()
    }

    companion object {
        fun newInstance() = ProjectListFragment()
    }
}
