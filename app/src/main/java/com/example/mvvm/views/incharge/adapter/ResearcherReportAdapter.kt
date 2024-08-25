package com.example.mvvm.views.incharge.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.databinding.ItemReportResearcherBinding
import com.example.mvvm.domain.ResearcherReport
import com.example.mvvm.utils.ext.setHyperLink
import com.example.mvvm.utils.ext.showDateDMY
import es.dmoral.toasty.Toasty

class ResearcherReportAdapter : RecyclerView.Adapter<ResearcherReportAdapter.ViewHolder>() {
    private val reports = mutableListOf<ResearcherReport>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ResearcherReport>?) {
        reports.clear()
        if (data != null) {
            reports.addAll(data)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemReportResearcherBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun bind(inflater: LayoutInflater): ItemReportResearcherBinding {
                return ItemReportResearcherBinding.inflate(inflater)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ViewHolder.bind(inflater))
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = reports[position]
        binding.textReportTitle.text = item.title
        if (item.title.isNullOrEmpty()) {
            binding.textReportTitle.text = "Báo cáo"
        }
        binding.textReportContent.text = item.content
        item.date?.let { binding.textReportDate.showDateDMY(it) }

        if (item.file.isNullOrEmpty()) {
            binding.textFileName.text = "Không có file đính kèm"
        } else {
            val title = item.file.first().title
            val url = item.file.first().url

            binding.textFileName.setHyperLink(title, url)
            binding.textFileName.setOnClickListener { Toasty.info(holder.itemView.context, url).show() }
        }
    }
}
