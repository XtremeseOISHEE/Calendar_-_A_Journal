package com.example.hellokotlinapp.ui.journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hellokotlinapp.R
import com.example.hellokotlinapp.data.local.JournalEntity
import java.text.SimpleDateFormat
import java.util.*
class JournalAdapter(private var journals: List<JournalEntity>) :
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
        val journal = journals[position]
        holder.tvTitle.text = journal.text.take(30) + if (journal.text.length > 30) "..." else ""

        // Convert timestamp to readable date
        holder.tvDate.text = journal.createdAt?.let {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            sdf.format(Date(it))
        } ?: "No date"
    }

    override fun getItemCount(): Int = journals.size

    fun updateList(newJournals: List<JournalEntity>) {
        journals = newJournals
        notifyDataSetChanged()
    }
}
