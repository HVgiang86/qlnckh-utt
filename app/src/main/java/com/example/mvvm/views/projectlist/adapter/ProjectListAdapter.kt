package com.example.mvvm.views.projectlist.adapter

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.databinding.ItemProjectBinding
import com.example.mvvm.domain.Project
import com.example.mvvm.utils.ext.getStateName
import com.example.mvvm.utils.ext.getStateTagBackground
import com.example.mvvm.utils.ext.invisible
import com.example.mvvm.utils.ext.visible

class ProjectListAdapter : RecyclerView.Adapter<ProjectListAdapter.ViewHolder>() {
    private var data: List<Project> = emptyList()
    private var onItemClickListener: ((Project) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Project>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Project) -> Unit) {
        onItemClickListener = listener
    }

    class ViewHolder(val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun bind(inflater: LayoutInflater): ViewHolder {
                return ViewHolder(ItemProjectBinding.inflate(inflater))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.bind(LayoutInflater.from(parent.context))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = data[position]

        binding.textTitle.text = item.title
        binding.textDescription.text = item.description

        val researcherAdapter = item.researcher?.let {
            ArrayAdapter(
                holder.itemView.context,
                R.layout.simple_list_item_1,
                it.map { researcher -> researcher.name },
            )
        }
        binding.listResearcher.adapter = researcherAdapter

        val supervisorAdapter = ArrayAdapter(
            holder.itemView.context,
            R.layout.simple_list_item_1,
            listOf(item.supervisor?.name ?: ""),
        )
        binding.listSupervisor.adapter = supervisorAdapter

        binding.textStatusTag.background = item.state?.getStateTagBackground()?.let {
            AppCompatResources.getDrawable(
                holder.itemView.context,
                it,
            )
        }
        binding.textStatusTag.text = item.state?.getStateName()

        if (item.score == null) {
            binding.textScore.invisible()
        } else {
            binding.textScore.visible()
            binding.textScore.text = "${item.score}/10"
        }

        binding.root.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }
}
