package com.example.hellokotlinapp.ui.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hellokotlinapp.R
import com.example.hellokotlinapp.data.local.EventEntity

class EventAdapter(private var events: List<EventEntity>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEventName: TextView = view.findViewById(R.id.tvEventName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.tvEventName.text = "${event.title} - ${event.dateIso} ${event.timeIso}"
    }

    override fun getItemCount(): Int = events.size

    fun updateList(newEvents: List<EventEntity>) {
        events = newEvents
        notifyDataSetChanged()
    }
}
