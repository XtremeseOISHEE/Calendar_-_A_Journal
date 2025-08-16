package com.example.hellokotlinapp.data.local



import android.content.Context
import androidx.room.*

@Database(
    entities = [UserProfile::class, EventEntity::class, JournalEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun journalDao(): JournalDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "journal.db"
                ).build().also { INSTANCE = it }
            }
    }
}

class Converters {
    @TypeConverter fun fromMood(m: Mood): String = m.name
    @TypeConverter fun toMood(s: String): Mood = Mood.valueOf(s)
}
