package com.example.hellokotlinapp.ui.journal



import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hellokotlinapp.R
import com.example.hellokotlinapp.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class JournalListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var journalAdapter: JournalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_list)

        recyclerView = findViewById(R.id.rvJournals)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Temporary journals list
        val journals = listOf("Journal 1", "Journal 2", "Journal 3")
        journalAdapter = JournalAdapter(journals)
        recyclerView.adapter = journalAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                // TODO: Add journal logic
                true
            }
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
