package com.example.mvvm.views.add

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.mvvm.R
import com.example.mvvm.base.BaseViewHolder
import com.example.mvvm.databinding.ItemNewBinding
import com.example.mvvm.databinding.ItemTitleBinding
import com.example.mvvm.domain.Document
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.Supervisor

class ItemAdapter(
    private val onClickAdd: () -> Unit,
    private val onClickRemove: (Item) -> Unit,
) : RecyclerView.Adapter<BaseViewHolder<ViewBinding>>() {

    private val items = mutableListOf<Item>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<Item>, title: String) {
        items.clear()
        items.addAll(newItems)
        items.add(0, Item.TitleItem(title))
        items.add(Item.AddItem)
        notifyDataSetChanged()
    }

    fun setItemSingleList(newItems: Item?, title: String) {
        items.clear()
        if (newItems != null) {
            items.add(newItems)
        }
        items.add(0, Item.TitleItem(title))
        if (items.size == 1) {
            items.add(Item.AddItem)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding> {
        return when (viewType) {
            TYPE_TITLE -> {
                TitleViewHolder(ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }

            TYPE_NEW_ITEM, TYPE_ADD -> {
                NewItemViewHolder(ItemNewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }

            else -> {
                throw Exception("Invalid view type")
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding>, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Item.TitleItem -> TYPE_TITLE
            is Item.DocumentItem, is Item.SuperVisorItem, is Item.ResearcherItem -> TYPE_NEW_ITEM
            is Item.AddItem -> TYPE_ADD
        }
    }

    inner class TitleViewHolder(binding: ItemTitleBinding) :
        BaseViewHolder<ItemTitleBinding>(binding) {
            override fun bind(item: Any, position: Int) {
                (item as Item).let {
                    binding.tvTitle.text = it.content
                }
            }
        }

    inner class NewItemViewHolder(binding: ItemNewBinding) : BaseViewHolder<ItemNewBinding>(binding) {
        override fun bind(item: Any, position: Int) {
            (item as Item).let { itm ->
                binding.imgIcon.setImageResource(
                    when (itm) {
                        is Item.DocumentItem, is Item.ResearcherItem, is Item.SuperVisorItem -> {
                            R.drawable.ic_cancel
                        }

                        is Item.AddItem -> {
                            R.drawable.ic_add
                        }

                        else -> {
                            throw Exception("Invalid view type")
                        }
                    },
                )

                when (itm) {
                    is Item.AddItem -> {
                        binding.layoutItem.setOnClickListener {
                            onClickAdd()
                        }
                    }

                    is Item.DocumentItem, is Item.ResearcherItem, is Item.SuperVisorItem -> {
                        binding.imgIcon.setOnClickListener {
                            onClickRemove(itm)
                        }
                    }

                    else -> {}
                }
                binding.tvContent.text = itm.content
            }
        }
    }

    companion object {
        const val TYPE_TITLE = 1
        const val TYPE_NEW_ITEM = 2
        const val TYPE_ADD = 3
    }
}

sealed class Item(val content: String) {
    data class DocumentItem(val document: Document) : Item(document.title)
    data class SuperVisorItem(val supervisor: Supervisor) : Item(supervisor.name.toString())
    data class ResearcherItem(val researcher: Researcher) : Item(researcher.name.toString())
    data class TitleItem(val title: String) : Item(title)
    data object AddItem : Item("Thêm")
}
