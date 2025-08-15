package com.vere.fit8.data.dao

import androidx.room.*
import com.vere.fit8.data.model.AppSettings
import kotlinx.coroutines.flow.Flow

/**
 * 应用设置数据访问对象
 */
@Dao
interface AppSettingsDao {
    
    @Query("SELECT * FROM app_settings WHERE id = 1")
    suspend fun getSettings(): AppSettings?
    
    @Query("SELECT * FROM app_settings WHERE id = 1")
    fun getSettingsFlow(): Flow<AppSettings?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: AppSettings)
    
    @Update
    suspend fun updateSettings(settings: AppSettings)
    
    // 通知设置相关
    @Query("UPDATE app_settings SET trainingReminderEnabled = :enabled WHERE id = 1")
    suspend fun updateTrainingReminderEnabled(enabled: Boolean)
    
    @Query("UPDATE app_settings SET trainingReminderTime = :time WHERE id = 1")
    suspend fun updateTrainingReminderTime(time: String)
    
    @Query("UPDATE app_settings SET waterReminderEnabled = :enabled WHERE id = 1")
    suspend fun updateWaterReminderEnabled(enabled: Boolean)
    
    @Query("UPDATE app_settings SET waterReminderInterval = :interval WHERE id = 1")
    suspend fun updateWaterReminderInterval(interval: Int)
    
    @Query("UPDATE app_settings SET sleepReminderEnabled = :enabled WHERE id = 1")
    suspend fun updateSleepReminderEnabled(enabled: Boolean)
    
    @Query("UPDATE app_settings SET sleepReminderTime = :time WHERE id = 1")
    suspend fun updateSleepReminderTime(time: String)
    
    // 应用设置相关
    @Query("UPDATE app_settings SET darkModeEnabled = :enabled WHERE id = 1")
    suspend fun updateDarkModeEnabled(enabled: Boolean)
    
    @Query("UPDATE app_settings SET language = :language WHERE id = 1")
    suspend fun updateLanguage(language: String)
    
    @Query("UPDATE app_settings SET autoSyncEnabled = :enabled WHERE id = 1")
    suspend fun updateAutoSyncEnabled(enabled: Boolean)
    
    // 用户信息相关
    @Query("UPDATE app_settings SET userName = :name WHERE id = 1")
    suspend fun updateUserName(name: String)
    
    @Query("UPDATE app_settings SET userHeight = :height WHERE id = 1")
    suspend fun updateUserHeight(height: Float)
    
    @Query("UPDATE app_settings SET userGender = :gender WHERE id = 1")
    suspend fun updateUserGender(gender: String)
    
    @Query("UPDATE app_settings SET userAge = :age WHERE id = 1")
    suspend fun updateUserAge(age: Int)
    
    @Query("UPDATE app_settings SET userGoal = :goal WHERE id = 1")
    suspend fun updateUserGoal(goal: String)
    
    // 其他设置
    @Query("UPDATE app_settings SET soundEnabled = :enabled WHERE id = 1")
    suspend fun updateSoundEnabled(enabled: Boolean)
    
    @Query("UPDATE app_settings SET vibrationEnabled = :enabled WHERE id = 1")
    suspend fun updateVibrationEnabled(enabled: Boolean)
    
    @Query("UPDATE app_settings SET dataPrivacyAccepted = :accepted WHERE id = 1")
    suspend fun updateDataPrivacyAccepted(accepted: Boolean)
    
    @Query("UPDATE app_settings SET updatedAt = :timestamp WHERE id = 1")
    suspend fun updateTimestamp(timestamp: Long)
}
