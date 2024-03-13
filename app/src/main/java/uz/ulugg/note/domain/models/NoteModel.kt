package uz.ulugg.note.domain.models

data class NoteModel(
    val id: Long = -1,
    val text: String,
    val noteLook: NoteLook,
    val createdAt: Long,
    val formattedDate: String = "",
) {

    companion object {
        fun tempModel(): NoteModel {
            return NoteModel(
                id = -1,
                text = "The ViewModels (VMs) may theoretically be initialized as " +
                        "class level instance variables using the Kotlin extension " +
                        "library import androidx.fragment.app.viewModels method by " +
                        "viewmodels(). By initializing the VM as a class level " +
                        "instance var it can be accessed within the class.",
                noteLook = NoteLook.LOOK_2,
                createdAt = System.currentTimeMillis(),
                formattedDate = ""
            )
        }
    }
}