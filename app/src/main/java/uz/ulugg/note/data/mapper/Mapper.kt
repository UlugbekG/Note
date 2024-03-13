package uz.ulugg.note.data.mapper

import uz.ulugg.note.data.database.NoteEntity
import uz.ulugg.note.domain.DateFormatter
import uz.ulugg.note.domain.models.NoteLook
import uz.ulugg.note.domain.models.NoteModel


fun NoteEntity.toModel(dateFormatter: DateFormatter): NoteModel {

    val newNoteLook = when (priority) {
        NoteEntity.NOTE_LOOK_1 -> {
            NoteLook.LOOK_1
        }

        NoteEntity.NOTE_LOOK_2 -> {
            NoteLook.LOOK_2
        }

        NoteEntity.NOTE_LOOK_3 -> {
            NoteLook.LOOK_3
        }

        NoteEntity.NOTE_LOOK_4 -> {
            NoteLook.LOOK_4
        }

        NoteEntity.NOTE_LOOK_5 -> {
            NoteLook.LOOK_5
        }

        else -> {
            NoteLook.LOOK_1
        }
    }

    return NoteModel(
        id = id,
        text = text,
        noteLook = newNoteLook,
        createdAt = createdAt,
        formattedDate = dateFormatter.formatDate(createdAt)
    )
}

fun NoteModel.toEntity(): NoteEntity {

    val newNoteLook = when (noteLook) {
        NoteLook.LOOK_1 -> {
            NoteEntity.NOTE_LOOK_1
        }

        NoteLook.LOOK_2 -> {
            NoteEntity.NOTE_LOOK_2
        }

        NoteLook.LOOK_3 -> {
            NoteEntity.NOTE_LOOK_3
        }

        NoteLook.LOOK_4 -> {
            NoteEntity.NOTE_LOOK_4
        }

        NoteLook.LOOK_5 -> {
            NoteEntity.NOTE_LOOK_5
        }
    }

    return NoteEntity(
        id = id,
        text = text,
        priority = newNoteLook,
        createdAt = createdAt,
    )
}