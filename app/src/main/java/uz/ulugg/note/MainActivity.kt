package uz.ulugg.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.NoteTheme
import uz.ulugg.note.screens.EditNoteScreen
import uz.ulugg.note.screens.MainScreen
import uz.ulugg.note.screens.NewNoteScreen
import uz.ulugg.note.screens.NoteDetailScreen

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appClass = (application as App)
            viewModel =
                ViewModelProvider(
                    this,
                    ViewModelFactory {
                        MainViewModel(
                            repository = appClass.getRepository(),
                            appSettings = appClass.getAppSettings(),
                        )
                    })[MainViewModel::class.java]
            NoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    Navigation(
                        navController = navController,
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = Destinations.MainScreen.label) {
        mainScreen(navController, viewModel)
        newNoteScreen(navController, viewModel)
        noteDetailScreen(navController, viewModel)
        editNoteScreen(navController, viewModel)
    }
}

fun NavGraphBuilder.newNoteScreen(navController: NavHostController, viewModel: MainViewModel) {
    composable(Destinations.NewNoteScreen.label) {
        NewNoteScreen(
            viewModel = viewModel,
            popUp = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.noteDetailScreen(navController: NavHostController, viewModel: MainViewModel) {
    composable(
        Destinations.NoteDetailScreen.label,
        arguments = listOf(navArgument("id") { type = NavType.LongType })
    ) { backStackEntry ->
        NoteDetailScreen(
            noteId = backStackEntry.arguments?.getLong("id") ?: -1,
            viewModel = viewModel,
            popUp = { navController.popBackStack() },
            navigateToEditNoteScreen = {
                navController.navigate(Destinations.EditNoteScreen.navigateWithArgs(it))
            }
        )
    }
}

fun NavGraphBuilder.mainScreen(navController: NavHostController, viewModel: MainViewModel) {
    composable(Destinations.MainScreen.label) {
        MainScreen(
            viewModel = viewModel,
            navigateCreateNoteScreen = {
                navController.navigate(Destinations.NewNoteScreen.label)
            },
            navigateToDetailScreen = { id ->
                navController.navigate(Destinations.NoteDetailScreen.navigateWithArgs(id))
            },
            popUp = {
                navController.popBackStack()
            }
        )
    }
}

fun NavGraphBuilder.editNoteScreen(navController: NavHostController, viewModel: MainViewModel) {
    composable(
        route = Destinations.EditNoteScreen.label,
        arguments = listOf(navArgument("id") {
            type = NavType.LongType
        })
    ) { backStackEntry ->
        EditNoteScreen(
            noteId = backStackEntry.arguments?.getLong("id") ?: -1,
            viewModel = viewModel
        ) {
            navController.popBackStack()
        }
    }
}

sealed class Destinations(val label: String) {
    data object MainScreen : Destinations("main_screen")
    data object NewNoteScreen : Destinations("new_note_screen")
    data object NoteDetailScreen : Destinations("note_detail_screen/{id}") {
        fun navigateWithArgs(id: Long): String = "note_detail_screen/${id}"
    }

    data object EditNoteScreen : Destinations("edit_note_screen/{id}") {
        fun navigateWithArgs(id: Long): String = "edit_note_screen/${id}"
    }
}



