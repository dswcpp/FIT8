package com.vere.fit8.di

import android.content.Context
import androidx.room.Room
import com.vere.fit8.data.dao.*
import com.vere.fit8.data.database.Fit8Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据库依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Fit8Database {
        return Room.databaseBuilder(
            context.applicationContext,
            Fit8Database::class.java,
            "fit8_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    @Provides
    fun provideDailyRecordDao(database: Fit8Database): DailyRecordDao {
        return database.dailyRecordDao()
    }
    
    @Provides
    fun provideWeeklyPlanDao(database: Fit8Database): WeeklyPlanDao {
        return database.weeklyPlanDao()
    }
    
    @Provides
    fun provideDietPlanDao(database: Fit8Database): DietPlanDao {
        return database.dietPlanDao()
    }

    @Provides
    fun provideMealRecordDao(database: Fit8Database): MealRecordDao {
        return database.mealRecordDao()
    }

    @Provides
    fun provideAchievementDao(database: Fit8Database): AchievementDao {
        return database.achievementDao()
    }
    
    @Provides
    fun provideUserStatsDao(database: Fit8Database): UserStatsDao {
        return database.userStatsDao()
    }

    @Provides
    fun provideAppSettingsDao(database: Fit8Database): com.vere.fit8.data.dao.AppSettingsDao {
        return database.appSettingsDao()
    }
}
