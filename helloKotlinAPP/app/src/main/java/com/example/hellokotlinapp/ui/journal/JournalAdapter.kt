package com.example.hellokotlinapp.ui.journal



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hellokotlinapp.R

class JournalAdapter(private val journals: List<String>) :
    RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvJournalTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvJournalDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_journal, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        holder.tvTitle.text = journals[position]
        holder.tvDate.text = "Date info here" // you can replace with real date
    }

    override fun getItemCount(): Int = journals.size
}
