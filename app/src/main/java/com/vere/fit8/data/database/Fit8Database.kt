package com.vere.fit8.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.vere.fit8.data.converter.Converters
import com.vere.fit8.data.dao.*
import com.vere.fit8.data.model.*

/**
 * 燃力8周应用数据库
 * 包含所有数据表和DAO接口
 */
@Database(
    entities = [
        DailyRecord::class,
        WeeklyPlan::class,
        DietPlan::class,
        MealRecord::class,
        Achievement::class,
        UserStats::class,
        com.vere.fit8.data.model.AppSettings::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Fit8Database : RoomDatabase() {
    
    abstract fun dailyRecordDao(): DailyRecordDao
    abstract fun weeklyPlanDao(): WeeklyPlanDao
    abstract fun dietPlanDao(): DietPlanDao
    abstract fun mealRecordDao(): MealRecordDao
    abstract fun achievementDao(): AchievementDao
    abstract fun userStatsDao(): UserStatsDao
    abstract fun appSettingsDao(): com.vere.fit8.data.dao.AppSettingsDao
    
    companion object {
        @Volatile
        private var INSTANCE: Fit8Database? = null
        
        fun getDatabase(context: Context): Fit8Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Fit8Database::class.java,
                    "fit8_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
