package uz.ulugg.note.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uz.ulugg.note.MainViewModel
import uz.ulugg.note.domain.models.NoteLook
import uz.ulugg.note.domain.models.getColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    popUp: () -> Unit,
) {
    var selectedNoteLook by remember { mutableStateOf(NoteLook.LOOK_1) }
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "New Note") },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.saveNote(text, selectedNoteLook) {
                                popUp()
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                            )
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = popUp) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = null,
                        )
                    }
                }
            )
        },
    ) { pp ->
        Column(
            modifier = Modifier.padding(pp)
        ) {
            PrioritySelection(
                modifier = Modifier.padding(8.dp),
                noteLook = selectedNoteLook,
                onChange = { priority ->
                    selectedNoteLook = priority
                }
            )
            TextField(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f),
                value = text,
                shape = RoundedCornerShape(8.dp),
                onValueChange = { text = it },
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    focusedContainerColor = selectedNoteLook.getColor(),
                    unfocusedContainerColor = selectedNoteLook.getColor(),
                    disabledContainerColor = selectedNoteLook.getColor(),
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }
    }
}


