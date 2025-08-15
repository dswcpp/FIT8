package com.vere.fit8.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        com.vere.fit8.data.model.AppSettings::class,
        ProgressPhoto::class,
        ExerciseDetail::class
    ],
    version = 6,
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
    abstract fun progressPhotoDao(): com.vere.fit8.data.dao.ProgressPhotoDao
    abstract fun exerciseDetailDao(): com.vere.fit8.data.dao.ExerciseDetailDao
    
    companion object {
        @Volatile
        private var INSTANCE: Fit8Database? = null

        // 数据库迁移：从版本3到版本4，添加userAvatar字段
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 为app_settings表添加userAvatar字段
                database.execSQL("ALTER TABLE app_settings ADD COLUMN userAvatar TEXT")
            }
        }

        // 数据库迁移：从版本4到版本5，添加progress_photos表
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 创建progress_photos表
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS progress_photos (
                        id INTEGER PRIMARY KEY NOT NULL,
                        filePath TEXT NOT NULL,
                        takenDate INTEGER NOT NULL,
                        weight REAL,
                        bodyFat REAL,
                        notes TEXT NOT NULL DEFAULT '',
                        isDeleted INTEGER NOT NULL DEFAULT 0,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        // 数据库迁移：从版本5到版本6，添加exercise_details表
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 创建exercise_details表
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS exercise_details (
                        id TEXT PRIMARY KEY NOT NULL,
                        name TEXT NOT NULL,
                        category TEXT NOT NULL,
                        difficulty TEXT NOT NULL,
                        equipment TEXT NOT NULL,
                        primaryMuscles TEXT NOT NULL,
                        secondaryMuscles TEXT NOT NULL,
                        description TEXT NOT NULL,
                        benefits TEXT NOT NULL,
                        instructions TEXT NOT NULL,
                        commonMistakes TEXT NOT NULL,
                        tips TEXT NOT NULL,
                        variations TEXT NOT NULL,
                        safetyNotes TEXT NOT NULL,
                        breathingPattern TEXT NOT NULL,
                        anatomyAnalysis TEXT NOT NULL,
                        biomechanics TEXT NOT NULL,
                        progressions TEXT NOT NULL,
                        regressions TEXT NOT NULL,
                        videoUrl TEXT,
                        imageUrls TEXT NOT NULL,
                        caloriesBurnedPerMinute INTEGER NOT NULL DEFAULT 0,
                        recommendedSets TEXT NOT NULL DEFAULT '',
                        recommendedReps TEXT NOT NULL DEFAULT '',
                        restTime TEXT NOT NULL DEFAULT '',
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }
        
        fun getDatabase(context: Context): Fit8Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Fit8Database::class.java,
                    "fit8_database"
                )
                .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                .fallbackToDestructiveMigration() // 作为最后的备选方案
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
