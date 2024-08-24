package com.example.mvvm.views.history

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentHistoryBinding
import com.example.mvvm.domain.Project
import com.example.mvvm.utils.ext.gone
import com.example.mvvm.utils.ext.visible
import com.example.mvvm.views.projectlist.adapter.ProjectListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryViewModel>() {

    private val adapter: ProjectListAdapter by lazy { ProjectListAdapter() }

    private fun init() {
        viewBinding.listProject.adapter = adapter
        viewBinding.listProject.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUp() {
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
        setUp()
        initData()
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }
}
