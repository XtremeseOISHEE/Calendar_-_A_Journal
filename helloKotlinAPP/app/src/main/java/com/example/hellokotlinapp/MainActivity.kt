package com.example.hellokotlinapp



import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hellokotlinapp.databinding.ActivityMainBinding
import com.example.hellokotlinapp.ui.auth.LoginActivity
import com.example.hellokotlinapp.ui.event.CreateEventActivity
import com.example.hellokotlinapp.ui.journal.CreateJournalActivity

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Background image: set in XML layout using ImageView under root
        b.btnCreateEvent.setOnClickListener { startActivity(Intent(this, CreateEventActivity::class.java)) }
        b.btnCreateJournal.setOnClickListener { startActivity(Intent(this, CreateJournalActivity::class.java)) }
        b.btnLoginSignup.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
    }
}
