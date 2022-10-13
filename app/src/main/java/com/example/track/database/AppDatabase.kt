package com.example.track.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.track.database.AppDatabase.Companion.DATABASE_VERSION
import com.example.track.database.AppDatabase.Companion.EXPORT_SCHEME


@Database(
    entities = arrayOf(Feature::class),
    version = DATABASE_VERSION,
    exportSchema = EXPORT_SCHEME
)
abstract class AppDatabase : RoomDatabase(){
    private var INSTANCE: AppDatabase? = null
    abstract fun featuresDao(): FeaturesDAO

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "featureSaved"
        const val EXPORT_SCHEME = false
    }

    open fun getDatabase(context: Context): AppDatabase? {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "features"
            ) // To simplify the codelab, allow queries on the main thread.
                // Don't do this on a real app! See PersistenceBasicSample for an example.
                .allowMainThreadQueries()
                .build()
        }
        return INSTANCE
    }

    open fun destroyInstance() {
        INSTANCE = null
    }
}