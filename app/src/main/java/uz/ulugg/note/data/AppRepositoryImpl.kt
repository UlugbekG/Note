package uz.ulugg.note.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import uz.ulugg.note.data.database.AppDatabase
import uz.ulugg.note.data.mapper.toEntity
import uz.ulugg.note.data.mapper.toModel
import uz.ulugg.note.domain.AppRepository
import uz.ulugg.note.domain.DateFormatter
import uz.ulugg.note.domain.models.NoteModel

class AppRepositoryImpl(
    appDatabase: AppDatabase,
    private val dateFormatter: DateFormatter
) : AppRepository {

    private val noteDao = appDatabase.todoDao()

    override suspend fun saveTodo(todo: NoteModel) =
        withContext(Dispatchers.IO) {
            noteDao.insert(todo.toEntity())
        }

    override suspend fun updateTodo(todo: NoteModel) =
        withContext(Dispatchers.IO) {
            noteDao.update(todo.toEntity())
        }

    override suspend fun deleteTodo(id: Long) =
        withContext(Dispatchers.IO) {
            noteDao.delete(id)
        }

    override suspend fun getTodoById(id: Long) =
        withContext(Dispatchers.IO) {
            noteDao.getNote(id).toModel(dateFormatter)
        }

    override fun getAlLTodos(isAsc: Boolean, byDate: Boolean): Flow<List<NoteModel>> {
        val flow =
            if (byDate) noteDao.getNoteList(isAsc)
            else noteDao.getNoteListByDate()

        return flow.map { list ->
            list.map { it.toModel(dateFormatter) }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun clearDb() = withContext(Dispatchers.IO) {
        noteDao.deleteAllTAble()
    }

}