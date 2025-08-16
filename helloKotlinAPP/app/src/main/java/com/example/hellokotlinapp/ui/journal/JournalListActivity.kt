package com.example.hellokotlinapp.ui.journal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hellokotlinapp.R
import com.example.hellokotlinapp.ui.auth.LoginActivity
import com.example.hellokotlinapp.data.local.AppDatabase
import com.example.hellokotlinapp.data.remote.SyncRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class JournalListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var journalAdapter: JournalAdapter
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val vm by lazy {
        JournalViewModel(
            repo = com.example.hellokotlinapp.data.repo.JournalRepository(
                AppDatabase.get(this),
                SyncRepository(FirebaseFirestore.getInstance())
            ),
            userIdProvider = { auth.currentUser?.uid ?: "local" },
            remoteUidProvider = { auth.currentUser?.uid }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_list)

        recyclerView = findViewById(R.id.rvJournals)
        recyclerView.layoutManager = LinearLayoutManager(this)
        journalAdapter = JournalAdapter(emptyList())
        recyclerView.adapter = journalAdapter

        // Fetch Firebase journals
        lifecycleScope.launch {
            vm.journals().collectLatest { journals ->
                journalAdapter.updateList(journals)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this, CreateJournalActivity::class.java))
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
