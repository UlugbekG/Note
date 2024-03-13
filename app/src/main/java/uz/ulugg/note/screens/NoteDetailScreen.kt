package uz.ulugg.note.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ulugg.note.MainViewModel
import uz.ulugg.note.domain.models.getColor
import uz.ulugg.note.ui.theme.Aqua

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Long,
    viewModel: MainViewModel,
    popUp: () -> Unit,
    navigateToEditNoteScreen: (Long) -> Unit,
) {
    val note = viewModel.currentNote.value
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, note) {
        viewModel.getNoteById(noteId)
        onDispose {
            viewModel.currentNote.value = null
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "${note?.formattedDate}") },
                navigationIcon = {
                    IconButton(onClick = popUp) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navigateToEditNoteScreen(noteId) }) {
                        Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit")
                    }
                }
            )
        }
    ) { pd ->
        Text(
            text = note?.text ?: "note not found",
            modifier = Modifier
                .padding(pd)
                .padding(8.dp)
                .clip(ShapeDefaults.Small)
                .background(note?.noteLook?.getColor() ?: Aqua)
                .padding(12.dp)
                .fillMaxSize()

        )
    }
}