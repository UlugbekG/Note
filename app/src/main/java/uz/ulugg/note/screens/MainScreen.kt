package uz.ulugg.note.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.PopupMenu
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import uz.ulugg.note.MainViewModel
import uz.ulugg.note.R
import uz.ulugg.note.domain.ListType
import uz.ulugg.note.domain.SortBy
import uz.ulugg.note.domain.models.NoteData
import uz.ulugg.note.domain.models.getColor


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navigateCreateNoteScreen: () -> Unit,
    navigateToDetailScreen: (Long) -> Unit,
    popUp: () -> Unit,
) {
    val listState = rememberLazyListState()
    val fabVisibility by derivedStateOf { listState.firstVisibleItemIndex == 0 }
    val state = viewModel.state.observeAsState().value
    val notes = state?.notes ?: emptyList()
    val listType = state?.listType ?: ListType.COLUMN
    val sortBy = state?.sortBy ?: SortBy.DATE

    val isSelectionMode = state?.isSelectionEnabled ?: false
    var expanded by remember { mutableStateOf(false) }

    BackHandler(true) {
        if (isSelectionMode) {
            viewModel.disableSelectionMode()
        } else {
            popUp()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Note") },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.changeListType()
                        },
                        content = {
                            Icon(
                                painter =
                                if (listType == ListType.GRID) painterResource(id = R.drawable.ic_linear)
                                else painterResource(id = R.drawable.ic_grid),
                                contentDescription = null
                            )
                        }
                    )
                    IconButton(
                        onClick = {
                            expanded = true
                        },
                        content = {
                            Icon(
                                painter = painterResource(R.drawable.ic_filter),
                                contentDescription = null
                            )
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                if (sortBy == SortBy.ASC) Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                viewModel.changeSortType(SortBy.ASC)
                                expanded = false
                            },
                            interactionSource = MutableInteractionSource(),
                            text = {

                                Text("ascending order")
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                if (sortBy == SortBy.DESC) Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                viewModel.changeSortType(SortBy.DESC)
                                expanded = false
                            },
                            interactionSource = MutableInteractionSource(),
                            text = {
                                Text("descending order")
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                if (sortBy == SortBy.DATE) Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                viewModel.changeSortType(SortBy.DATE)
                                expanded = false
                            },
                            interactionSource = MutableInteractionSource(),
                            text = {
                                Text("by date")
                            }
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            AddPaymentFab(
                modifier = Modifier.padding(bottom = 40.dp),
                isVisibleBecauseOfScrolling = fabVisibility,
                onClick = navigateCreateNoteScreen
            )
        }) { p ->
        if (listType == ListType.COLUMN) {
            LazyColumn(
                modifier = Modifier.padding(p),
                state = listState,
            ) {
                items(notes) {
                    NoteItem(
                        noteData = it,
                        isSelectionModeEnabled = isSelectionMode,
                        onClick = navigateToDetailScreen,
                        onToggle = { id ->
                            viewModel.onToggle(id)
                        },
                    )
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(notes) { note ->
                        NoteItem(
                            noteData = note,
                            isSelectionModeEnabled = isSelectionMode,
                            onClick = navigateToDetailScreen,
                            onToggle = { id ->
                                viewModel.onToggle(id)
                            },
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(p)
            )
        }
    }

}

@Composable
private fun AddPaymentFab(
    modifier: Modifier,
    isVisibleBecauseOfScrolling: Boolean,
    onClick: () -> Unit,
) {
    val density = LocalDensity.current
    AnimatedVisibility(modifier = modifier,
        visible = isVisibleBecauseOfScrolling,
        enter = slideInVertically { with(density) { 40.dp.roundToPx() } } + fadeIn(),
        exit = fadeOut(animationSpec = keyframes {
            this.durationMillis = 100
        })
    ) {
        FloatingActionButton(
            onClick = onClick,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp
            )
        ) {
            Icon(Icons.Filled.Add, "Add Payment")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    noteData: NoteData,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    isSelectionModeEnabled: Boolean,
    onClick: (Long) -> Unit,
    onToggle: (Long) -> Unit,
) {
    Box(modifier = modifier.padding(4.dp)) {
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
                    color = noteData.note.noteLook.getColor(),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(
                            noteData.note.noteLook.getColor().toArgb(), 0x000000, 0.2f
                        )
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = { onClick(noteData.note.id) },
                    onLongClick = { onToggle(noteData.note.id) }
                )
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
            Text(text = noteData.note.formattedDate)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
            Divider()
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
            Text(
                text = noteData.note.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
            if (isSelectionModeEnabled)
                Checkbox(
                    checked = noteData.isSelected,
                    onCheckedChange = {
                        onToggle(noteData.note.id)
                    }
                )
        }
    }
}
