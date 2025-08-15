package com.vere.fit8.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 应用设置数据模型
 */
@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey
    val id: Int = 1, // 固定ID，只有一条记录
    
    // 通知设置
    val trainingReminderEnabled: Boolean = true,
    val trainingReminderTime: String = "18:00",
    val waterReminderEnabled: Boolean = true,
    val waterReminderInterval: Int = 2, // 小时
    val sleepReminderEnabled: Boolean = true,
    val sleepReminderTime: String = "22:00",
    
    // 应用设置
    val darkModeEnabled: Boolean = false,
    val language: String = "zh", // zh, en
    val autoSyncEnabled: Boolean = true,
    
    // 用户信息
    val userName: String = "燃力用户",
    val userHeight: Float = 170f, // cm
    val userGender: String = "male", // male, female
    val userAge: Int = 25,
    val userGoal: String = "减脂塑形",
    val userAvatar: String? = null, // 头像路径
    
    // 其他设置
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val dataPrivacyAccepted: Boolean = false,
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * 用户个人信息数据模型
 */
data class UserProfile(
    val name: String,
    val height: Float, // cm
    val gender: String, // male, female
    val age: Int,
    val goal: String, // 减脂塑形, 增肌, 保持健康
    val activityLevel: String, // 低, 中, 高
    val targetWeight: Float? = null, // kg
    val joinDate: String
)

/**
 * 通知设置数据模型
 */
data class NotificationSettings(
    val trainingEnabled: Boolean,
    val trainingTime: String,
    val waterEnabled: Boolean,
    val waterInterval: Int,
    val sleepEnabled: Boolean,
    val sleepTime: String,
    val soundEnabled: Boolean,
    val vibrationEnabled: Boolean
)

/**
 * 应用偏好设置数据模型
 */
data class AppPreferences(
    val darkMode: Boolean,
    val language: String,
    val autoSync: Boolean,
    val dataPrivacy: Boolean
)
