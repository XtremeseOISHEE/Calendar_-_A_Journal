package com.example.hellokotlinapp.ui.journal



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hellokotlinapp.data.local.JournalEntity
import com.example.hellokotlinapp.data.repo.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class JournalViewModel(
    private val repo: JournalRepository,
    private val userIdProvider: () -> String,
    private val remoteUidProvider: () -> String?
) : ViewModel() {
    fun journals(): Flow<List<JournalEntity>> = repo.stream(userIdProvider())
    fun add(j: JournalEntity, onDone: () -> Unit = {}) = viewModelScope.launch {
        repo.add(userIdProvider(), j, remoteUidProvider())
        onDone()
    }
}

/*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hellokotlinapp.R
*/

//class CreateJournalActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_create_journal) // create this layout next
//    }
//}