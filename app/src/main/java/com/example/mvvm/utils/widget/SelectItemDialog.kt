package com.example.mvvm.utils.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.R
import com.example.mvvm.databinding.DialogSelectItemBinding
import com.example.mvvm.databinding.ItemPopupSelectBinding
import com.example.mvvm.utils.ext.setOnUnMultiClickListener

class SelectItemDialog<T>(private val context: Context) {
    private val dialogView by lazy {
        DialogSelectItemBinding.inflate(LayoutInflater.from(context), null, false)
    }

    private val itemList: MutableList<T> = mutableListOf()
    private var getText: ((T) -> String)? = null

    private var builder: AlertDialog.Builder? = null

    private var alertDialog: AlertDialog? = null

    private var selectedItem: T? = null

    init {
        initialize()
    }

    private fun initialize() {
        builder = AlertDialog.Builder(context).setView(dialogView.root)
        alertDialog = builder?.create()
        alertDialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    fun create() = alertDialog?.apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun title(title: CharSequence) {
        dialogView.titleTextView.text = title
    }

    fun title(
        @StringRes resId: Int,
    ) {
        dialogView.titleTextView.text = context.getString(resId)
    }

    fun setFunGetText(getText: (T) -> String) {
        this.getText = getText
    }

    fun setItemList(itemList: List<T>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        val adapter = DialogSelectAdapter(itemList, getText ?: { it.toString() }) {
            selectedItem = it
        }
        dialogView.recycler.adapter = adapter

    }

    fun okButton(
        text: String,
        completion: (T?) -> Unit = {},
    ) = with(dialogView) {
        btnOk.text = text
        btnOk.isVisible = true
        btnOk.setOnUnMultiClickListener {
            completion(selectedItem)
            alertDialog?.dismiss()
        }
    }

    fun dismiss() {
        alertDialog?.dismiss()
    }

    fun setCancelable() {
        alertDialog?.setCancelable(false)
    }

    fun show() {
        alertDialog?.show()
    }
}

class DialogSelectAdapter<T>(val items: List<T>, private val getText: (T) -> String, private val onItemClick: (T) -> Unit) :
    RecyclerView.Adapter<DialogSelectAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPopupSelectBinding) : RecyclerView.ViewHolder(binding.root)

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPopupSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (selectedPosition == position) {
            holder.binding.textView.background = holder.binding.root.context.getDrawable(R.color.light_pink)
        } else {
            holder.binding.textView.background = holder.binding.root.context.getDrawable(R.color.blur_gray)
        }

        holder.binding.textView.text = getText(items[position])
        holder.binding.root.setOnClickListener {
            onItemClick(items[position])
            selectedPosition = position
            refresh()
        }
    }

    fun refresh() {
        notifyDataSetChanged()
    }
}
