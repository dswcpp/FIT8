package com.vere.fit8.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vere.fit8.data.converter.Converters
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 饮食记录数据模型
 * 用户实际的饮食记录
 */
@Entity(tableName = "meal_records")
@TypeConverters(Converters::class)
data class MealRecord(
    @PrimaryKey
    val id: String,                         // 记录ID
    val date: LocalDate,                    // 日期
    val mealType: String,                   // 餐次类型：早餐/午餐/晚餐/加餐
    val foodName: String,                   // 食物名称
    val amount: String,                     // 分量描述
    val calories: Int = 0,                  // 热量（kcal）
    val protein: Float = 0f,                // 蛋白质（g）
    val carbs: Float = 0f,                  // 碳水化合物（g）
    val fat: Float = 0f,                    // 脂肪（g）
    val notes: String = "",                 // 备注
    val photoPath: String = "",             // 照片路径
    val recordType: String = "MANUAL",      // 记录类型：MANUAL/PHOTO/SCAN
    val createdAt: LocalDateTime = LocalDateTime.now(), // 创建时间
    val updatedAt: LocalDateTime = LocalDateTime.now()  // 更新时间
)

/**
 * 记录类型枚举
 */
enum class RecordType(val displayName: String) {
    MANUAL("手动输入"),
    PHOTO("拍照记录"),
    SCAN("扫码识别")
}

/**
 * 营养成分汇总
 */
data class NutritionSummary(
    val totalCalories: Int,
    val totalProtein: Float,
    val totalCarbs: Float,
    val totalFat: Float,
    val targetCalories: Int = 1700,
    val targetProtein: Float = 120f,
    val targetCarbs: Float = 170f,
    val targetFat: Float = 45f
) {
    val caloriesProgress: Int get() = (totalCalories * 100 / targetCalories).coerceAtMost(100)
    val proteinProgress: Int get() = (totalProtein * 100 / targetProtein).toInt().coerceAtMost(100)
    val carbsProgress: Int get() = (totalCarbs * 100 / targetCarbs).toInt().coerceAtMost(100)
    val fatProgress: Int get() = (totalFat * 100 / targetFat).toInt().coerceAtMost(100)
    
    val isCaloriesOnTrack: Boolean get() = totalCalories in (targetCalories * 0.9).toInt()..(targetCalories * 1.1).toInt()
    val isProteinOnTrack: Boolean get() = totalProtein >= targetProtein * 0.8f
    val isCarbsOnTrack: Boolean get() = totalCarbs <= targetCarbs * 1.2f
    val isFatOnTrack: Boolean get() = totalFat <= targetFat * 1.2f
    
    val overallScore: Int get() {
        var score = 0
        if (isCaloriesOnTrack) score += 25
        if (isProteinOnTrack) score += 25
        if (isCarbsOnTrack) score += 25
        if (isFatOnTrack) score += 25
        return score
    }
}
