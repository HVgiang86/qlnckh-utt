package com.example.mvvm.views.assign

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentAssignBinding
import com.example.mvvm.utils.formatDateToDDMMYYYY
import dagger.hilt.android.AndroidEntryPoint
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

    private val documentAdapter by lazy {
        ItemAdapter(onClickAdd = {}, onClickRemove = { item ->
            when (item) {
                Item.AddItem -> TODO()
                is Item.ResearcherItem -> TODO()
                is Item.SuperVisorItem -> TODO()
                is Item.TitleItem -> TODO()
            }

        })
    }

    fun setUp() {

        val type = arguments?.getInt(KEY_TYPE, 0)
        val projectId = arguments?.getLong(KEY_PROJECT_ID, 0)
        setupUI(type ?: 0)

        setupObserver()

        viewBinding.btnSave.setOnClickListener {
            if (projectId != null) {
//                viewModel.approveProject(topicId = projectId)
//                viewModel.assignProject(topicId = projectId, binding.tvDate.text.toString())
            }
        }

        viewBinding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun setupObserver() {
//        viewModel.documents.observe(this) {
//            documentAdapter.setItems(it.map { document ->
//                Item.DocumentItem(document)
//            }, "Documents")
//        }
    }


    @SuppressLint("SetTextI18n")
    fun setupUI(type: Int) {

        viewBinding.rcvDocument.apply {
            adapter = documentAdapter
            layoutManager = LinearLayoutManager(requireContext())
            documentAdapter.setItems(listOf(), "SuperVisor")
        }

        viewBinding.tvDate.text = formatDateToDDMMYYYY(Date())

    }

    companion object {
        const val KEY_TYPE = "type"
        const val KEY_PROJECT_ID = "projectId"

        fun newInstance() = AssignFragment()
    }

}
