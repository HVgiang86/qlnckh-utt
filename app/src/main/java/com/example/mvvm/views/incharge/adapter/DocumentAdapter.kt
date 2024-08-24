package com.example.mvvm.views.incharge.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.databinding.ItemDocumentNoBackgroundBinding
import com.example.mvvm.domain.Document
import com.example.mvvm.utils.ext.setHyperLink

class DocumentAdapter : RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {
    private val data = mutableListOf<Document>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Document>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemDocumentNoBackgroundBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun bind(inflater: LayoutInflater): ItemDocumentNoBackgroundBinding {
                return ItemDocumentNoBackgroundBinding.inflate(inflater)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ViewHolder.bind(inflater))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.textFileName.setHyperLink(item.title, item.url)
    }
}
