package com.vere.fit8.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * 成就数据模型
 */
@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey
    val id: String,                     // 成就ID
    val title: String,                  // 成就标题
    val description: String,            // 成就描述
    val iconRes: String,                // 图标资源名
    val type: String,                   // 成就类型
    val targetValue: Int,               // 目标值
    val currentValue: Int = 0,          // 当前进度
    val isUnlocked: Boolean = false,    // 是否已解锁
    val unlockedAt: LocalDateTime? = null, // 解锁时间
    val points: Int = 10                // 成就积分
)

/**
 * 成就类型枚举
 */
enum class AchievementType(val displayName: String) {
    CONSECUTIVE_DAYS("连续打卡"),
    TOTAL_WORKOUTS("总训练次数"),
    WEIGHT_LOSS("减重成就"),
    BODY_FAT_REDUCTION("体脂降低"),
    SPECIAL_EXERCISE("特殊动作"),
    WEEKLY_GOAL("周目标"),
    MONTHLY_GOAL("月目标")
}

/**
 * 用户统计数据模型
 */
@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey
    val id: Int = 1,                    // 固定ID，只有一条记录
    val totalWorkouts: Int = 0,         // 总训练次数
    val totalDays: Int = 0,             // 总打卡天数
    val consecutiveDays: Int = 0,       // 连续打卡天数
    val maxConsecutiveDays: Int = 0,    // 最大连续天数
    val totalCaloriesBurned: Int = 0,   // 总消耗卡路里
    val totalPoints: Int = 0,           // 总积分
    val currentWeek: Int = 1,           // 当前周数
    val startDate: LocalDateTime? = null, // 开始日期
    val lastActiveDate: LocalDateTime? = null // 最后活跃日期
)
