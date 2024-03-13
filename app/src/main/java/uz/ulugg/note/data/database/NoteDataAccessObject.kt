package uz.ulugg.note.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDataAccessObject {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: NoteEntity)

    @Update
    suspend fun update(todo: NoteEntity)

    @Query("SELECT * FROM NOTE WHERE id = :id")
    suspend fun getNote(id: Long): NoteEntity

    @Query(
        "SELECT * FROM NOTE ORDER BY CASE WHEN :isAsc = 1 THEN text END ASC, " +
                "CASE WHEN :isAsc = 0 THEN text END DESC"
    )
    fun getNoteList(isAsc: Boolean): Flow<List<NoteEntity>>

    @Query("SELECT * FROM NOTE ORDER BY createdAt")
    fun getNoteListByDate(): Flow<List<NoteEntity>>

    @Query("DELETE FROM NOTE WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM NOTE")
    suspend fun deleteAllTAble()

}