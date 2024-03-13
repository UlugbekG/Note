package uz.ulugg.note.domain

import kotlinx.coroutines.flow.Flow
import uz.ulugg.note.domain.models.NoteModel

interface AppRepository {

    suspend fun saveTodo(todo: NoteModel)

    suspend fun updateTodo(todo: NoteModel)

    suspend fun deleteTodo(id: Long)

    suspend fun getTodoById(id: Long): NoteModel

    fun getAlLTodos(isAsc: Boolean, byDate: Boolean): Flow<List<NoteModel>>

    suspend fun clearDb()

}