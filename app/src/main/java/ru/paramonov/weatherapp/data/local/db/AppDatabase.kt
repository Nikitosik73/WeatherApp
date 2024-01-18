package ru.paramonov.weatherapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.paramonov.weatherapp.data.local.db.dao.FavoriteCitiesDao
import ru.paramonov.weatherapp.data.local.model.CityDbModel

@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteCitiesDao(): FavoriteCitiesDao

    companion object {

        private const val DB_NAME = "AppDatabase.db"
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {
            instance?.let { database -> return database }

            synchronized(LOCK) {
                instance?.let { database -> return database }

                val database: AppDatabase = Room.databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = DB_NAME
                ).build()

                instance = database
                return database
            }
        }
    }
}