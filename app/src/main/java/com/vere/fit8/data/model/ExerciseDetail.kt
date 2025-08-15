package com.vere.fit8.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * 动作详情数据模型
 * 包含动作的科学详细讲解分析
 */
@Entity(tableName = "exercise_details")
data class ExerciseDetail(
    @PrimaryKey
    val id: String,                          // 动作ID，与ExerciseTemplate的id对应
    val name: String,                        // 动作名称
    val category: String,                    // 动作分类（胸部、背部、腿部等）
    val difficulty: String,                  // 难度等级（初级、中级、高级）
    val equipment: List<String>,             // 所需器械
    val primaryMuscles: List<String>,        // 主要锻炼肌群
    val secondaryMuscles: List<String>,      // 次要锻炼肌群
    val description: String,                 // 动作简介
    val benefits: List<String>,              // 动作益处
    val instructions: List<String>,          // 动作步骤
    val commonMistakes: List<String>,        // 常见错误
    val tips: List<String>,                  // 专业建议
    val variations: List<String>,            // 动作变式
    val safetyNotes: List<String>,           // 安全注意事项
    val breathingPattern: String,            // 呼吸模式
    val anatomyAnalysis: String,             // 解剖学分析
    val biomechanics: String,                // 生物力学分析
    val progressions: List<String>,          // 进阶方式
    val regressions: List<String>,           // 退阶方式
    val videoUrl: String? = null,            // 示范视频URL
    val imageUrls: List<String> = emptyList(), // 示范图片URLs
    val caloriesBurnedPerMinute: Int = 0,    // 每分钟消耗卡路里
    val recommendedSets: String = "",        // 推荐组数
    val recommendedReps: String = "",        // 推荐次数
    val restTime: String = "",               // 推荐休息时间
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Serializable

/**
 * 动作分类枚举
 */
enum class ExerciseCategory(val displayName: String) {
    CHEST("胸部"),
    BACK("背部"),
    SHOULDERS("肩部"),
    ARMS("手臂"),
    LEGS("腿部"),
    CORE("核心"),
    CARDIO("有氧"),
    FULL_BODY("全身"),
    STRETCHING("拉伸")
}

/**
 * 难度等级枚举
 */
enum class DifficultyLevel(val displayName: String, val level: Int) {
    BEGINNER("初级", 1),
    INTERMEDIATE("中级", 2),
    ADVANCED("高级", 3),
    EXPERT("专家级", 4)
}

/**
 * 器械类型枚举
 */
enum class EquipmentType(val displayName: String) {
    BODYWEIGHT("徒手"),
    DUMBBELLS("哑铃"),
    BARBELL("杠铃"),
    RESISTANCE_BANDS("弹力带"),
    KETTLEBELL("壶铃"),
    CABLE_MACHINE("拉力器"),
    BENCH("卧推凳"),
    PULL_UP_BAR("引体向上杆"),
    YOGA_MAT("瑜伽垫"),
    FOAM_ROLLER("泡沫轴")
}

/**
 * 肌群类型枚举
 */
enum class MuscleGroup(val displayName: String) {
    PECTORALS("胸大肌"),
    LATISSIMUS_DORSI("背阔肌"),
    RHOMBOIDS("菱形肌"),
    TRAPEZIUS("斜方肌"),
    DELTOIDS("三角肌"),
    BICEPS("肱二头肌"),
    TRICEPS("肱三头肌"),
    QUADRICEPS("股四头肌"),
    HAMSTRINGS("腘绳肌"),
    GLUTES("臀大肌"),
    CALVES("小腿肌"),
    RECTUS_ABDOMINIS("腹直肌"),
    OBLIQUES("腹斜肌"),
    ERECTOR_SPINAE("竖脊肌"),
    SERRATUS_ANTERIOR("前锯肌")
}
