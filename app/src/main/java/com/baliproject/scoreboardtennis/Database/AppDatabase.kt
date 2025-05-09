package com.baliproject.scoreboardtennis.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.baliproject.scoreboardtennis.Dao.IpAddressDao
import com.baliproject.scoreboardtennis.Dao.MatchDao
import com.baliproject.scoreboardtennis.Dao.WifiDao
import com.baliproject.scoreboardtennis.Entity.IpAddressEntity
import com.baliproject.scoreboardtennis.Entity.MatchEntity
import com.baliproject.scoreboardtennis.Entity.WifiEntity

@Database(entities = [IpAddressEntity::class, WifiEntity::class, MatchEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ipAddressDao(): IpAddressDao
    abstract fun wifiDao(): WifiDao
    abstract fun matchDao(): MatchDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
    }
}


