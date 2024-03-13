package uz.ulugg.note

import android.app.Application
import uz.ulugg.note.data.AppRepositoryImpl
import uz.ulugg.note.data.AppSettingsImpl
import uz.ulugg.note.data.DateFormatterImpl
import uz.ulugg.note.data.database.AppDatabase
import uz.ulugg.note.data.database.AppDatabase.Companion.createAppDatabase
import uz.ulugg.note.domain.AppRepository
import uz.ulugg.note.domain.AppSettings
import uz.ulugg.note.domain.DateFormatter

class App : Application() {

    private lateinit var appDatabase: AppDatabase
    private lateinit var repository: AppRepository
    private lateinit var dateFormatter: DateFormatter
    private lateinit var appSettings: AppSettings

    override fun onCreate() {
        super.onCreate()
        dateFormatter = DateFormatterImpl()
        appDatabase = createAppDatabase(this)
        appSettings = AppSettingsImpl(this)
        repository = AppRepositoryImpl(appDatabase, dateFormatter)
    }

    fun getDateFormatter(): DateFormatter = dateFormatter
    fun getAppDatabase(): AppDatabase = appDatabase
    fun getAppSettings(): AppSettings = appSettings
    fun getRepository(): AppRepository = repository

}