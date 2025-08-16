package com.example.hellokotlinapp.data.repo



import com.example.hellokotlinapp.data.local.AppDatabase
import com.example.hellokotlinapp.data.local.JournalEntity
import com.example.hellokotlinapp.data.remote.SyncRepository
import kotlinx.coroutines.flow.Flow

class JournalRepository(
    private val db: AppDatabase,
    private val sync: SyncRepository?
) {
    fun stream(userId: String): Flow<List<JournalEntity>> = db.journalDao().journalsForUser(userId)

    suspend fun add(userId: String, j: JournalEntity, remoteUid: String?) {
        db.journalDao().insert(j.copy(userId = userId))
        if (remoteUid != null) {
            sync?.pushJournals(remoteUid, listOf(j.copy(userId = userId)))
        }
    }
}
