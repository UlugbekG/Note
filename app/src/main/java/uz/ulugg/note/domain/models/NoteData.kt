package uz.ulugg.note.domain.models

data class NoteData(
    val note: NoteModel,
    val isSelected: Boolean = false,
)