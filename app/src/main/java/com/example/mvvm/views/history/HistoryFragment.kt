package com.example.mvvm.views.history

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.R
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.databinding.FragmentHistoryBinding
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.Role
import com.example.mvvm.utils.ext.gone
import com.example.mvvm.utils.ext.visible
import com.example.mvvm.views.projectlist.adapter.ProjectListAdapter
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryViewModel>() {

    private val adapter: ProjectListAdapter by lazy { ProjectListAdapter() }
    private lateinit var profileResponse: ProfileResponse

    private fun init() {
        viewBinding.listProject.adapter = adapter
        viewBinding.listProject.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initData() {
        viewModel.getHistory()
    }

    private fun setData(data: List<Project>) {
        adapter.setData(data)
    }

    override val viewModel: HistoryViewModel by viewModels()

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentHistoryBinding {
        return FragmentHistoryBinding.inflate(inflater)
    }

    override fun initialize() {
        init()
        observer()
    }

    private fun observer() {
        viewModel.profile.observe(viewLifecycleOwner) { res ->
            profileResponse = res
            if(res.role == Role.ADMIN.value) {
                viewBinding.topAppBar.title = getString(R.string.title_admin)
                viewBinding.containerNoInCharge.gone()
                viewBinding.containerHasInCharge.gone()

                viewModel.getListResearcherSupervisor(Role.RESEARCHER.value)
                viewModel.getListResearcherSupervisor(Role.SUPERVISOR.value)
            } else {
                // Comment
                initData()
            }
        }

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

        viewModel.getListResearch.observe(viewLifecycleOwner) {
            val a = it
        }

        viewModel.getListSupervisor.observe(viewLifecycleOwner) {
            val a = it
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProfile()
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }
}
