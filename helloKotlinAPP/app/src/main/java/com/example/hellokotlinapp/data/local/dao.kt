package com.example.hellokotlinapp.data.local



import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert suspend fun insert(event: EventEntity): Long
    @Query("SELECT * FROM events WHERE userId = :userId ORDER BY dateIso, timeIso")
    fun eventsForUser(userId: String): Flow<List<EventEntity>>
    @Delete suspend fun delete(event: EventEntity)
}

@Dao
interface JournalDao {
    @Insert suspend fun insert(journal: JournalEntity): Long
    @Query("SELECT * FROM journals WHERE userId = :userId ORDER BY createdAt DESC")
    fun journalsForUser(userId: String): Flow<List<JournalEntity>>
    @Delete suspend fun delete(journal: JournalEntity)
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfile)
    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    suspend fun get(uid: String): UserProfile?
}
