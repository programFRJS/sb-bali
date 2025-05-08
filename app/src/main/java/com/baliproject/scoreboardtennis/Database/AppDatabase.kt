package com.baliproject.scoreboardtennis.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.baliproject.scoreboardtennis.Dao.IpAddressDao
import com.baliproject.scoreboardtennis.Entity.IpAddressEntity

@Database(entities = [IpAddressEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ipAddressDao(): IpAddressDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build().also { instance = it }
            }
    }
}
