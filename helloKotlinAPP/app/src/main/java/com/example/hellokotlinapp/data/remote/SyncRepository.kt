package com.example.hellokotlinapp.data.remote



import com.example.hellokotlinapp.data.local.EventEntity
import com.example.hellokotlinapp.data.local.JournalEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SyncRepository(private val db: FirebaseFirestore) {

    suspend fun pushEvents(uid: String, events: List<EventEntity>) {
        val batch = db.batch()
        val col = db.collection("users").document(uid).collection("events")
        events.forEach {
            val doc = if (it.id == 0) col.document() else col.document(it.id.toString())
            batch.set(doc, it)
        }
        batch.commit().await()
    }

    suspend fun pushJournals(uid: String, journals: List<JournalEntity>) {
        val batch = db.batch()
        val col = db.collection("users").document(uid).collection("journals")
        journals.forEach {
            val doc = if (it.id == 0) col.document() else col.document(it.id.toString())
            batch.set(doc, it)
        }
        batch.commit().await()
    }

    // Pulling (basic)
    suspend fun pullEvents(uid: String): List<EventEntity> {
        val snap = db.collection("users").document(uid).collection("events").get().await()
        return snap.documents.mapNotNull { it.toObject(EventEntity::class.java) }
    }

    suspend fun pullJournals(uid: String): List<JournalEntity> {
        val snap = db.collection("users").document(uid).collection("journals").get().await()
        return snap.documents.mapNotNull { it.toObject(JournalEntity::class.java) }
    }
}
