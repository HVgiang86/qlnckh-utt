package com.example.mvvm.views.history

import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.R
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.databinding.FragmentHistoryBinding
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ResearcherSupervisor
import com.example.mvvm.domain.Role
import com.example.mvvm.domain.User
import com.example.mvvm.utils.ext.addFragmentToActivity
import com.example.mvvm.utils.ext.gone
import com.example.mvvm.utils.ext.visible
import com.example.mvvm.views.profile.ProfileFragment
import com.example.mvvm.views.projectlist.adapter.ProjectListAdapter
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryViewModel>() {

    private val adapter: ProjectListAdapter by lazy { ProjectListAdapter() }
    private lateinit var profileResponse: ProfileResponse

    private lateinit var researcherAdapter: ResearcherSupervisorAdapter
    private lateinit var supervisorAdapter: ResearcherSupervisorAdapter

    private lateinit var researcherList: MutableList<ResearcherSupervisor>
    private lateinit var supervisorList: MutableList<ResearcherSupervisor>

    private fun init() {
        viewBinding.listProject.adapter = adapter
        viewBinding.listProject.layoutManager = LinearLayoutManager(requireContext())

        researcherList = mutableListOf()
        supervisorList = mutableListOf()

        researcherAdapter = ResearcherSupervisorAdapter(researcherList) {
            AlertDialog.Builder(context)
                .setTitle(Role.RESEARCHER.name)
                .setMessage("Bạn có chắc chắn muốn sử dụng ${it.fullName}?")
                .setPositiveButton("Xóa") { _, _ ->
                    viewModel.deleteResearcherSupervisor(it.email)
                }
                .setNegativeButton("Sửa") { _, _ ->
                    addFragmentToActivity(R.id.container, ProfileFragment.newInstance(ProfileFragment.TYPE_ADMIN, it.email), true)
                }
                .show()
        }
        supervisorAdapter = ResearcherSupervisorAdapter(supervisorList) {
            AlertDialog.Builder(context)
                .setTitle(Role.SUPERVISOR.name)
                .setMessage("Bạn có chắc chắn muốn sử dụng ${it.fullName}?")
                .setPositiveButton("Xóa") { _, _ ->
                    viewModel.deleteResearcherSupervisor(it.email)
                }
                .setNegativeButton("Sửa") { _, _ ->
                    addFragmentToActivity(R.id.container, ProfileFragment.newInstance(ProfileFragment.TYPE_ADMIN, it.email), true)
                }
                .show()
        }

        viewBinding.revResearcher.adapter = researcherAdapter
        viewBinding.revResearcher.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewBinding.revSupervisor.adapter = supervisorAdapter
        viewBinding.revSupervisor.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

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
        registerErrorHandler()
        init()
        observer()
    }

    private fun observer() {
        viewModel.profile.observe(viewLifecycleOwner) { res ->
            profileResponse = res
            if (res.role == Role.ADMIN.value) {
                viewBinding.topAppBar.title = getString(R.string.title_admin)
                viewBinding.containerNoInCharge.gone()
                viewBinding.containerHasInCharge.gone()
                viewBinding.containerResearcherSupervisor.visible()

                viewModel.getListResearcherSupervisor(Role.RESEARCHER.value)
                viewModel.getListResearcherSupervisor(Role.SUPERVISOR.value)
            } else {
                // Comment
                initData()
            }
        }

        viewModel.delete.observe(viewLifecycleOwner) {
            viewModel.getListResearcherSupervisor(Role.RESEARCHER.value)
            viewModel.getListResearcherSupervisor(Role.SUPERVISOR.value)
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

        viewModel.getListResearch.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) researcherAdapter.updateData(data as MutableList<ResearcherSupervisor>)

        }

        viewModel.getListSupervisor.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) supervisorAdapter.updateData(data as MutableList<ResearcherSupervisor>)
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
