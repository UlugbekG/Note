package uz.ulugg.note

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch
import uz.ulugg.note.domain.AppRepository
import uz.ulugg.note.domain.AppSettings
import uz.ulugg.note.domain.ListType
import uz.ulugg.note.domain.SortBy
import uz.ulugg.note.domain.models.NoteLook
import uz.ulugg.note.domain.models.NoteData
import uz.ulugg.note.domain.models.NoteModel

class MainViewModel(
    private val repository: AppRepository,
    private val appSettings: AppSettings,
) : ViewModel() {

    val currentNote = mutableStateOf<NoteModel?>(null)
    private val _selectionModeFlow = MutableStateFlow<SelectionMode>(SelectionMode.Disabled)
    private val _listTypeFlow = appSettings.getListType()
    private val _sortFlow = appSettings.getSortType()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _notes = _sortFlow.flatMapLatest {
        val ascOrDescSort = it == SortBy.ASC
        val byDate = it == SortBy.DATE
        repository.getAlLTodos(ascOrDescSort, byDate)
    }

    val state = combine(
        _notes,
        _sortFlow,
        _selectionModeFlow,
        _listTypeFlow,
        ::merge
    ).asLiveData()

    private fun merge(
        notes: List<NoteModel>,
        sortBy: SortBy,
        selectionMode: SelectionMode,
        listType: ListType
    ): State {
        val isSelectionModeEnabled = selectionMode is SelectionMode.Enabled
        val ids =
            if (isSelectionModeEnabled) (selectionMode as SelectionMode.Enabled).selectedIds
            else emptyList()
        val list = notes.map { model ->
            NoteData(
                note = model,
                isSelected = ids.contains(model.id)
            )
        }
        return State(
            notes = list,
            isSelectionEnabled = isSelectionModeEnabled,
            listType = listType,
            sortBy = sortBy,
        )
    }

    fun changeSortType(sortBy: SortBy) {
        viewModelScope.launch {
            appSettings.saveSort(sortBy)
        }
    }

    fun changeListType() {
        viewModelScope.launch {
            val listType = state.value?.listType
            if (listType == ListType.COLUMN) {
                appSettings.saveListType(ListType.GRID)
            } else {
                appSettings.saveListType(ListType.COLUMN)
            }
        }
    }

    fun disableSelectionMode() {
        _selectionModeFlow.value = SelectionMode.Disabled
    }

    fun onToggle(id: Long) {
        val selectionMode = _selectionModeFlow.value
        if (selectionMode is SelectionMode.Enabled) {
            val selectedIds = selectionMode.selectedIds
            if (selectedIds.contains(id)) {
                selectedIds.remove(id)
            } else {
                selectedIds.add(id)
            }
            _selectionModeFlow.value = SelectionMode.Enabled(selectedIds)
        } else {
            _selectionModeFlow.value = SelectionMode.Enabled(mutableSetOf(id))
        }
    }

    fun saveNote(
        text: String,
        noteLook: NoteLook,
        close: () -> Unit,
    ) {
        viewModelScope.launch {
            if (text.isBlank()) return@launch
            val newTodo = NoteModel(
                id = 0L,
                text = text,
                noteLook = noteLook,
                createdAt = System.currentTimeMillis()
            )
            repository.saveTodo(newTodo)
            close()
        }
    }


    fun getNoteById(id: Long) {
        viewModelScope.launch {
            try {
                val note = repository.getTodoById(id)
                currentNote.value = note
            } catch (e: Exception) {
                // TODO:
            }
        }
    }

    fun update(text: String, noteLook: NoteLook, close: () -> Unit) {
        viewModelScope.launch {
            val note = currentNote.value ?: return@launch
            if (text.isBlank()) return@launch
            repository.updateTodo(note.copy(text = text, noteLook = noteLook))
            close()
        }
    }

    fun deleteSelected() {

    }

    fun deleteById(id: Long) {
        viewModelScope.launch {
            repository.deleteTodo(id)
        }
    }

    sealed class SelectionMode {
        data object Disabled : SelectionMode()
        class Enabled(
            val selectedIds: MutableSet<Long> = mutableSetOf()
        ) : SelectionMode()
    }

    data class State(
        val notes: List<NoteData>,
        val isSelectionEnabled: Boolean,
        val listType: ListType,
        val sortBy: SortBy,
    )
}



