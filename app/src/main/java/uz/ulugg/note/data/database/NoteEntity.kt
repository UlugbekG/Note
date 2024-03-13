package uz.ulugg.note.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "NOTE")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String = "",
    val priority: Int = NOTE_LOOK_1,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val NOTE_LOOK_1 = 1
        const val NOTE_LOOK_2 = 2
        const val NOTE_LOOK_3 = 3
        const val NOTE_LOOK_4 = 4
        const val NOTE_LOOK_5 = 5
    }
}