package com.example.mvvm.views.assign

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.mvvm.R
import com.example.mvvm.base.BaseViewHolder
import com.example.mvvm.databinding.ItemNewBinding
import com.example.mvvm.databinding.ItemTitleBinding
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.Supervisor

class ItemAdapter(
    private val onClickAdd: () -> Unit,
    private val onClickRemove: (Item) -> Unit,
) : RecyclerView.Adapter<BaseViewHolder<ViewBinding>>() {

    private val items = mutableListOf<Item>()
    private var type = 1;

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<Item>, title: String) {
        items.clear()
        items.addAll(newItems)
        items.add(0, Item.TitleItem(title))
        if (newItems.isEmpty() || type == 2) {
            items.add(Item.AddItem)
        }
        notifyDataSetChanged()
    }

    fun setType (type: Int) {
        this.type = type
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
            is Item.SuperVisorItem -> TYPE_NEW_ITEM
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
                        is Item.SuperVisorItem -> {
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

                    is Item.SuperVisorItem -> {
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
    data class SuperVisorItem(val supervisor: String) : Item(supervisor)
    data class TitleItem(val title: String) : Item(title)
    data object AddItem : Item("Add")
}
