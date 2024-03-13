package uz.ulugg.note.data

import android.annotation.SuppressLint
import uz.ulugg.note.domain.DateFormatter
import java.text.SimpleDateFormat
import java.util.Date

class DateFormatterImpl : DateFormatter {
    private val pattern = "MMM dd, yyyy | HH:mm a"
    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat(pattern)
    override fun formatDate(date: Long): String = simpleDateFormat.format(Date(date))
}