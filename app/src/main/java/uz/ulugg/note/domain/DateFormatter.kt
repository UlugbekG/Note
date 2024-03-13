package uz.ulugg.note.domain

interface DateFormatter {
    fun formatDate(date: Long): String
}