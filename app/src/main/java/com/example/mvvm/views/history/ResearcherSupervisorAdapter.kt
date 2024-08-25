package com.example.mvvm.views.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.R
import com.example.mvvm.domain.ResearcherSupervisor

class ResearcherSupervisorAdapter(
    private var researcherSupervisors : MutableList<ResearcherSupervisor>,
    private val onItemClick: (ResearcherSupervisor) -> Unit
    ): RecyclerView.Adapter<ResearcherSupervisorAdapter.ViewHolder>() {

    fun updateData(researcherSupervisors: MutableList<ResearcherSupervisor>) {
        this.researcherSupervisors = researcherSupervisors
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFullNameValue: TextView = itemView.findViewById(R.id.tvFullNameValue)
        val tvEmailValue: TextView = itemView.findViewById(R.id.tvEmailValue)
        val tvDateOfBirthValue: TextView = itemView.findViewById(R.id.tvDateOfBirthValue)
        val tvPhoneNumberValue: TextView = itemView.findViewById(R.id.tvPhoneNumberValue)
        val container: CardView = itemView.findViewById(R.id.container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_researcher, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return researcherSupervisors.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val researcherSupervisor = researcherSupervisors[position]
        holder.tvFullNameValue.text = researcherSupervisor.fullName
        holder.tvEmailValue.text = researcherSupervisor.email
        holder.tvDateOfBirthValue.text = researcherSupervisor.dateOfBirth
        holder.tvPhoneNumberValue.text = researcherSupervisor.phoneNumber

        holder.container.setOnClickListener{
            onItemClick(researcherSupervisor)
        }
    }
}
