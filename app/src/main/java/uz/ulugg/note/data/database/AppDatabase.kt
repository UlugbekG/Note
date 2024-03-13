package uz.ulugg.note.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

const val APP_DB_NAME = "note_app_database"

@Database(
    version = 1,
    entities = [NoteEntity::class, ],
    exportSchema = false,
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): NoteDataAccessObject
    companion object {
        fun createAppDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, APP_DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
