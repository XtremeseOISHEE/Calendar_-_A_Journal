package com.example.hellokotlinapp.data.repo



import com.example.hellokotlinapp.data.local.AppDatabase
import com.example.hellokotlinapp.data.local.EventEntity
import com.example.hellokotlinapp.data.remote.SyncRepository
import kotlinx.coroutines.flow.Flow

class EventRepository(
    private val db: AppDatabase,
    private val sync: SyncRepository?
) {
    fun stream(userId: String): Flow<List<EventEntity>> = db.eventDao().eventsForUser(userId)

    suspend fun add(userId: String, e: EventEntity, remoteUid: String?) {
        db.eventDao().insert(e.copy(userId = userId))
        if (remoteUid != null) {
            // Push small set (you could be smarter and push only new)
            val list = db.eventDao().eventsForUser(userId)
            // flows aren't directly list; in practice, query once more or track inserts.
            // For brevity, skip pulling the whole list and just push this single item:
            sync?.pushEvents(remoteUid, listOf(e.copy(userId = userId)))
        }
    }
}
