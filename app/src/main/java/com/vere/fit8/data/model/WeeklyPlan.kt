package com.vere.fit8.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vere.fit8.data.converter.Converters

/**
 * 8周训练计划数据模型
 */
@Entity(tableName = "weekly_plans")
@TypeConverters(Converters::class)
data class WeeklyPlan(
    @PrimaryKey
    val id: String,                         // 计划ID（如：week1_day1）
    val week: Int,                          // 第几周（1-8）
    val dayOfWeek: Int,                     // 星期几（1-7）
    val trainingType: String,               // 训练类型
    val exercises: List<ExerciseTemplate>,  // 动作模板列表
    val estimatedDurationMin: Int,          // 预估时长
    val estimatedCalories: Int,             // 预估消耗
    val description: String = "",           // 训练描述
    val tips: String = ""                   // 训练提示
)

/**
 * 动作模板数据模型
 */
data class ExerciseTemplate(
    val name: String,           // 动作名称
    val nameEn: String = "",    // 英文名称
    val sets: Int,              // 建议组数
    val reps: Int = 0,          // 建议次数
    val durationSec: Int = 0,   // 建议持续时间（秒）
    val restSec: Int = 30,      // 组间休息时间（秒）
    val description: String = "", // 动作描述
    val tips: String = "",      // 动作要点
    val videoUrl: String = "",  // 视频链接
    val imageUrl: String = "",  // 图片链接
    val difficulty: Int = 1,    // 难度等级（1-5）
    val targetMuscles: List<String> = emptyList(), // 目标肌群
    val equipment: String = "无器械" // 所需器械
)

/**
 * 饮食计划数据模型
 */
@Entity(tableName = "diet_plans")
data class DietPlan(
    @PrimaryKey
    val id: String,             // 计划ID
    val week: Int,              // 第几周
    val mealType: String,       // 餐次类型：早餐/午餐/晚餐/加餐
    val foodName: String,       // 食物名称
    val amount: String,         // 分量（如：100g）
    val calories: Int,          // 热量（kcal）
    val protein: Float,         // 蛋白质（g）
    val carbs: Float,           // 碳水化合物（g）
    val fat: Float,             // 脂肪（g）
    val description: String = "" // 描述
)

/**
 * 餐次类型枚举
 */
enum class MealType(val displayName: String) {
    BREAKFAST("早餐"),
    LUNCH("午餐"),
    DINNER("晚餐"),
    SNACK("加餐")
}
