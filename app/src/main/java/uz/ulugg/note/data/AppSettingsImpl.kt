package uz.ulugg.note.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import uz.ulugg.note.domain.AppSettings
import uz.ulugg.note.domain.ListType
import uz.ulugg.note.domain.SortBy

class AppSettingsImpl(context: Context) : AppSettings {

    private companion object {
        const val SETTINGS_PREFS_NAME = "app_settings"
        const val KEY_LIST_TYPE = "list_type"
        const val KEY_SORT_BY = "sort_by"

        const val SORT_ASC = 1
        const val SORT_DESC = 2
        const val SORT_DATE = 3

        const val TYPE_COLUMN = 1
        const val TYPE_GRID = 2
    }

    private val sharedPreferences =
        context.getSharedPreferences(SETTINGS_PREFS_NAME, Context.MODE_PRIVATE)

    private val listTypeLiveData = MutableLiveData<ListType>()
    private val sortByLiveData = MutableLiveData<SortBy>()

    override fun getSortType(): Flow<SortBy> = sortByLiveData.apply {
        value = sharedPreferences.getInt(KEY_SORT_BY, SORT_DATE).getSortBy()
    }
        .asFlow()

    override suspend fun saveSort(sortBy: SortBy): Unit =
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .putInt(KEY_SORT_BY, sortBy.getValue())
                .apply()

            sortByLiveData.postValue(
                sharedPreferences.getInt(KEY_SORT_BY, SORT_DATE).getSortBy()
            )
        }

    override fun getListType(): Flow<ListType> = listTypeLiveData
        .apply {
            value = sharedPreferences.getInt(KEY_LIST_TYPE, TYPE_COLUMN).getListType()
        }
        .asFlow()

    override suspend fun saveListType(listType: ListType): Unit =
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .putInt(KEY_LIST_TYPE, listType.getValue())
                .apply()

            listTypeLiveData.postValue(
                sharedPreferences.getInt(KEY_LIST_TYPE, TYPE_COLUMN).getListType()
            )
        }

    private fun Int.getSortBy(): SortBy {
        return when (this) {
            SORT_ASC -> SortBy.ASC
            SORT_DESC -> SortBy.DESC
            SORT_DATE -> SortBy.DATE
            else -> SortBy.DATE
        }
    }

    private fun SortBy.getValue(): Int {
        return when (this) {
            SortBy.ASC -> SORT_ASC
            SortBy.DESC -> SORT_DESC
            SortBy.DATE -> SORT_DATE
        }
    }

    private fun Int.getListType(): ListType {
        return if (this == TYPE_COLUMN) ListType.COLUMN else ListType.GRID
    }

    private fun ListType.getValue(): Int {
        return if (this == ListType.COLUMN) TYPE_COLUMN else TYPE_GRID
    }
}