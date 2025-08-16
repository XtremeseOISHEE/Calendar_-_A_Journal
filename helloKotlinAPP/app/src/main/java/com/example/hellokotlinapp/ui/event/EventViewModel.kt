package com.example.hellokotlinapp.ui.event


/*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hellokotlinapp.R
*/


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hellokotlinapp.data.local.EventEntity
import com.example.hellokotlinapp.data.repo.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventViewModel(
    private val repo: EventRepository,
    private val userIdProvider: () -> String,     // "local" or uid
    private val remoteUidProvider: () -> String?  // null if not logged in
) : ViewModel() {
    fun events(): Flow<List<EventEntity>> = repo.stream(userIdProvider())

    fun addEvent(e: EventEntity, onDone: () -> Unit = {}) = viewModelScope.launch {
        repo.add(userIdProvider(), e, remoteUidProvider())
        onDone()
    }
}

/*

class CreateEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event) // create this layout next
    }
}

*/
