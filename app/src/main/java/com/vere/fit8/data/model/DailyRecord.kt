package com.vere.fit8.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vere.fit8.data.converter.Converters
import java.time.LocalDate

/**
 * 每日打卡记录数据模型
 * 对应用户每天的完整健身数据
 */
@Entity(tableName = "daily_records")
@TypeConverters(Converters::class)
data class DailyRecord(
    @PrimaryKey
    val date: LocalDate,                    // 日期
    val weight: Float? = null,              // 体重（斤）
    val bodyFat: Float? = null,             // 体脂率（%）
    val basalMetabolism: Int? = null,       // 基础代谢（kcal）
    val bmi: Float? = null,                 // BMI（自动计算）
    val trainingType: String? = null,       // 训练类型：力量/有氧/HIIT/休息
    val trainingList: List<TrainingExercise> = emptyList(), // 训练动作列表
    val trainingDurationMin: Int = 0,       // 训练总时长（分钟）
    val trainingCalories: Int = 0,          // 训练消耗（kcal）
    val waterMl: Int = 0,                   // 饮水量（ml）
    val sleepHours: Float = 0f,             // 睡眠时长（小时）
    val mood: Int = 3,                      // 心情状态（1-5级）
    val dietOk: Boolean = false,            // 饮食达标
    val notes: String = "",                 // 备注
    val createdAt: Long = System.currentTimeMillis(), // 创建时间
    val updatedAt: Long = System.currentTimeMillis()  // 更新时间
)

/**
 * 训练动作数据模型
 */
data class TrainingExercise(
    val name: String,           // 动作名称
    val targetSets: Int = 0,    // 目标组数
    val targetReps: Int = 0,    // 目标次数
    val targetDurationSec: Int = 0, // 目标持续时间（秒）
    val completedSets: Int = 0, // 已完成组数
    val setRecords: List<SetRecord> = emptyList(), // 每组的详细记录
    val completed: Boolean = false, // 是否完全完成
    val startTime: Long? = null,    // 开始时间
    val endTime: Long? = null       // 结束时间
)

/**
 * 单组训练记录
 */
data class SetRecord(
    val setNumber: Int,         // 第几组
    val actualReps: Int = 0,    // 实际完成次数
    val actualDurationSec: Int = 0, // 实际持续时间
    val restDurationSec: Int = 0,   // 实际休息时间
    val completed: Boolean = false, // 本组是否完成
    val timestamp: Long = System.currentTimeMillis() // 完成时间
)

/**
 * 训练类型枚举
 */
enum class TrainingType(val displayName: String) {
    STRENGTH("力量训练"),
    CARDIO("有氧运动"),
    HIIT("HIIT训练"),
    REST("休息日"),
    YOGA("瑜伽拉伸")
}

/**
 * 心情状态枚举
 */
enum class MoodLevel(val value: Int, val displayName: String, val emoji: String) {
    VERY_BAD(1, "很糟糕", "😞"),
    BAD(2, "不太好", "😕"),
    NORMAL(3, "一般", "😐"),
    GOOD(4, "不错", "😊"),
    EXCELLENT(5, "很棒", "😄")
}
