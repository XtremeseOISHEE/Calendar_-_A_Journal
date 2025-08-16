package com.example.hellokotlinapp



import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hellokotlinapp.databinding.ActivityMain2Binding
import com.example.hellokotlinapp.ui.event.CreateEventActivity
import com.example.hellokotlinapp.ui.event.EventListActivity
import com.example.hellokotlinapp.ui.journal.CreateJournalActivity
import com.example.hellokotlinapp.ui.journal.JournalListActivity

class MainActivity2 : AppCompatActivity() {

    private lateinit var b: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnCreateEvent.setOnClickListener {
            startActivity(Intent(this, CreateEventActivity::class.java))
        }

        b.btnCreateJournal.setOnClickListener {
            startActivity(Intent(this, CreateJournalActivity::class.java))
        }

        b.btnViewEvents.setOnClickListener {
            startActivity(Intent(this, EventListActivity::class.java))
        }

        b.btnViewJournals.setOnClickListener {
            startActivity(Intent(this, JournalListActivity::class.java))
        }
    }
}
