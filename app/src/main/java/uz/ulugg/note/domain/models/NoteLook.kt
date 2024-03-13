package uz.ulugg.note.domain.models

import androidx.compose.ui.graphics.Color
import uz.ulugg.note.ui.theme.Aqua
import uz.ulugg.note.ui.theme.Canary
import uz.ulugg.note.ui.theme.PalePink
import uz.ulugg.note.ui.theme.TropicalBlue
import uz.ulugg.note.ui.theme.Whisper

enum class NoteLook {
    LOOK_1,
    LOOK_2,
    LOOK_3,
    LOOK_4,
    LOOK_5,
}

fun NoteLook.getColor(): Color = when (this) {
    NoteLook.LOOK_1 -> Canary
    NoteLook.LOOK_2 -> PalePink
    NoteLook.LOOK_3 -> Aqua
    NoteLook.LOOK_4 -> TropicalBlue
    NoteLook.LOOK_5 -> Whisper
}