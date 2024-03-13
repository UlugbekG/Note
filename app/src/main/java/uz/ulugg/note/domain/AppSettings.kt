package uz.ulugg.note.domain

import kotlinx.coroutines.flow.Flow

enum class ListType { COLUMN, GRID }

enum class SortBy { ASC, DESC, DATE }

interface AppSettings {
    fun getSortType(): Flow<SortBy>
    suspend fun saveSort(sortBy: SortBy)
    fun getListType(): Flow<ListType>
    suspend fun saveListType(listType: ListType)
}