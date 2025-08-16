package com.example.hellokotlinapp.data.local



import androidx.room.*
import java.util.*

@Entity(tableName = "users")
data class UserProfile(
    @PrimaryKey val uid: String,             // Firebase UID
    val fullName: String,
    val username: String,
    val email: String,
    val birthdate: String                    // ISO yyyy-MM-dd
)

@Entity(
    tableName = "events",
    indices = [Index("userId")]
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,          // Firebase UID or "local" if not logged in
    val title: String,
    val dateIso: String,         // yyyy-MM-dd
    val timeIso: String,         // HH:mm
    val reminderAtMillis: Long   // trigger time
)

enum class Mood { HAPPY, SAD, CALM, ANGRY, EXCITED }

@Entity(
    tableName = "journals",
    indices = [Index("userId")]
)
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val text: String,
    val imageUri: String?,       // persisted content URI string
    val mood: Mood,
    val createdAt: Long = System.currentTimeMillis()
)
