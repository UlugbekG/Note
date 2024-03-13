package uz.ulugg.note.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import uz.ulugg.note.domain.models.NoteLook
import uz.ulugg.note.domain.models.getColor


@Composable
fun PrioritySelection(
    noteLook: NoteLook,
    modifier: Modifier = Modifier,
    onChange: (NoteLook) -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        NotePItem(
            color = NoteLook.LOOK_1.getColor(),
            isSelected = noteLook == NoteLook.LOOK_1,
            onClick = { onChange(NoteLook.LOOK_1) }
        )
        NotePItem(
            color = NoteLook.LOOK_2.getColor(),
            isSelected = noteLook == NoteLook.LOOK_2,
            onClick = { onChange(NoteLook.LOOK_2) }
        )
        NotePItem(
            color = NoteLook.LOOK_3.getColor(),
            isSelected = noteLook == NoteLook.LOOK_3,
            onClick = { onChange(NoteLook.LOOK_3) }
        )
        NotePItem(
            color = NoteLook.LOOK_4.getColor(),
            isSelected = noteLook == NoteLook.LOOK_4,
            onClick = { onChange(NoteLook.LOOK_4) }
        )
        NotePItem(
            color = NoteLook.LOOK_5.getColor(),
            isSelected = noteLook == NoteLook.LOOK_5,
            onClick = { onChange(NoteLook.LOOK_5) }
        )
    }
}

@Composable
fun NotePItem(
    color: Color,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 15.dp,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(onClick = onClick)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            clipPath(clipPath) {
                drawRoundRect(
                    color = color,
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(
                            color.toArgb(), 0x000000, 0.2f
                        )
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -50f),
                    size = Size(cutCornerSize.toPx() + 50f, cutCornerSize.toPx() + 50f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            if (isSelected) Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
            )
        }
    }
}