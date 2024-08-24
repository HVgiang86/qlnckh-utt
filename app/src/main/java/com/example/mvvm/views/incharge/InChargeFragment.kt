package com.example.mvvm.views.incharge

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentInChargeBinding
import com.example.mvvm.domain.UserRole
import com.example.mvvm.utils.ext.getStateName
import com.example.mvvm.utils.ext.getStateTagBackground
import com.example.mvvm.utils.ext.gone
import com.example.mvvm.utils.ext.visible
import com.example.mvvm.views.incharge.adapter.DocumentAdapter
import com.example.mvvm.views.incharge.adapter.ResearcherReportAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InChargeFragment : BaseFragment<FragmentInChargeBinding, InChargeViewModel>() {
    override val viewModel: InChargeViewModel by viewModels()
    var fabType: InChargeFAB? = null
    override fun inflateViewBinding(inflater: LayoutInflater): FragmentInChargeBinding {
        return FragmentInChargeBinding.inflate(inflater)
    }

    override fun initialize() {
        viewModel.inCharge.observe(viewLifecycleOwner) {
            if (it == null) {
                viewBinding.containerNoInCharge.visible()
                viewBinding.containerHasInCharge.gone()
            } else {
                viewBinding.containerNoInCharge.gone()
                viewBinding.containerHasInCharge.visible()

                setData()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setData() {
        val data = viewModel.inCharge.value

        Timber.d("data: $data")

        if (data == null) return

        viewBinding.textTitle.text = data.title
        viewBinding.textDescription.text = data.description
        viewBinding.textStatusTag.background = resources.getDrawable(data.state.getStateTagBackground())
        viewBinding.textStatusTag.text = data.state.getStateName()

        val researcherAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_list_item_1, data.researcher.map { it.name })
        viewBinding.listResearcher.adapter = researcherAdapter

        val supervisorAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_list_item_1, data.supervisor.map { it.name })
        viewBinding.listSupervisor.adapter = supervisorAdapter

        val documentAdapter = DocumentAdapter()
        viewBinding.listDocument.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.listDocument.adapter = documentAdapter
        documentAdapter.setData(data.documents)

        val adapter = ResearcherReportAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        viewBinding.listReport.adapter = adapter
        viewBinding.listReport.layoutManager = layoutManager
        adapter.setData(data.reports)

        fabType = getInChargeFAB(data.state, UserRole.RESEARCHER)

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
                InChargeFAB.NEW_REPORT -> {
                    addReport()
                }

                InChargeFAB.PAUSE -> {
                    pauseProject()
                }

                InChargeFAB.RESUME -> {
                    resumeProject()
                }

                InChargeFAB.CANCEL -> {
                    cancelProject()
                }

                InChargeFAB.REVIEW -> TODO()
                InChargeFAB.MARK_FINISH -> TODO()
                null -> TODO()
            }
        }
    }

    private fun resumeProject() {
        viewModel.resumeProject()
    }

    private fun pauseProject() {
        viewModel.pauseProject()
    }

    private fun addReport() {
        // TODO Add
    }

    private fun cancelProject() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Hủy dự án")
            setMessage("Bạn có chắc chắn muốn hủy dự án này?")
            setPositiveButton("Có") { _, _ ->
                viewModel.cancelProject()
            }
            setNeutralButton("Không") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
        viewModel.cancelProject()
    }

    companion object {
        fun newInstance() = InChargeFragment()
    }
}
