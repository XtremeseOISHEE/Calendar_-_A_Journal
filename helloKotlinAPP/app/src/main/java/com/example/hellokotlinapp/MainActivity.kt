package com.example.hellokotlinapp


import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvEvents: TextView
    private lateinit var btnAddEvent: Button

    private val eventsMap = mutableMapOf<String, MutableList<String>>()
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView = findViewById(R.id.calendarView)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvEvents = findViewById(R.id.tvEvents)
        btnAddEvent = findViewById(R.id.btnAddEvent)

        // When user selects a date on calendar
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // month is 0-based, so add 1
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            tvSelectedDate.text = "Selected: $selectedDate"
            showEvents()
        }

        btnAddEvent.setOnClickListener {
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Select a date first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val input = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Add Event")
                .setView(input)
                .setPositiveButton("Save") { _, _ ->
                    val eventText = input.text.toString().trim()
                    if (eventText.isNotEmpty()) {
                        val list = eventsMap.getOrPut(selectedDate) { mutableListOf() }
                        list.add(eventText)
                        showEvents()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showEvents() {
        val events = eventsMap[selectedDate] ?: emptyList()
        if (events.isEmpty()) {
            tvEvents.text = "No events for $selectedDate"
        } else {
            tvEvents.text = events.joinToString("\n")
        }
    }
}
