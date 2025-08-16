package com.example.hellokotlinapp.ui.event

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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EventListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val vm by lazy {
        EventViewModel(
            repo = com.example.hellokotlinapp.data.repo.EventRepository(
                com.example.hellokotlinapp.data.local.AppDatabase.get(this),
                com.example.hellokotlinapp.data.remote.SyncRepository(com.google.firebase.firestore.FirebaseFirestore.getInstance())
            ),
            userIdProvider = { auth.currentUser?.uid ?: "local" },
            remoteUidProvider = { auth.currentUser?.uid }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        recyclerView = findViewById(R.id.rvEvents)
        recyclerView.layoutManager = LinearLayoutManager(this)
        eventAdapter = EventAdapter(emptyList())
        recyclerView.adapter = eventAdapter

        // Fetch Firebase events
        lifecycleScope.launch {
            vm.events().collectLatest { events ->
                eventAdapter.updateList(events)
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
                startActivity(Intent(this, CreateEventActivity::class.java))
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
