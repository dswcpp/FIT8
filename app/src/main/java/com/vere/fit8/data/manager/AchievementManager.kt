package com.vere.fit8.data.manager

import com.vere.fit8.data.model.Achievement
import com.vere.fit8.data.repository.Fit8Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 成就管理器
 * 处理成就解锁逻辑和通知
 */
@Singleton
class AchievementManager @Inject constructor(
    private val repository: Fit8Repository
) {
    
    private val _achievementUnlocked = MutableSharedFlow<Achievement>()
    val achievementUnlocked: SharedFlow<Achievement> = _achievementUnlocked.asSharedFlow()
    
    /**
     * 检查并解锁打卡相关成就
     */
    suspend fun checkCheckinAchievements(consecutiveDays: Int) {
        val achievements = repository.getLockedAchievements()
        
        achievements.filter { it.type == "CHECKIN" }.forEach { achievement ->
            when (achievement.id) {
                "first_checkin" -> {
                    if (consecutiveDays >= 1) {
                        unlockAchievement(achievement.copy(currentValue = consecutiveDays))
                    }
                }
                "checkin_3_days" -> {
                    if (consecutiveDays >= 3) {
                        unlockAchievement(achievement.copy(currentValue = consecutiveDays))
                    }
                }
                "checkin_7_days" -> {
                    if (consecutiveDays >= 7) {
                        unlockAchievement(achievement.copy(currentValue = consecutiveDays))
                    }
                }
                "checkin_30_days" -> {
                    if (consecutiveDays >= 30) {
                        unlockAchievement(achievement.copy(currentValue = consecutiveDays))
                    }
                }
            }
        }
    }
    
    /**
     * 检查并解锁训练相关成就
     */
    suspend fun checkTrainingAchievements(totalWorkouts: Int, currentWeek: Int) {
        val achievements = repository.getLockedAchievements()
        
        achievements.filter { it.type == "TRAINING" || it.type == "WEEKLY" }.forEach { achievement ->
            when (achievement.id) {
                "first_workout" -> {
                    if (totalWorkouts >= 1) {
                        unlockAchievement(achievement.copy(currentValue = totalWorkouts))
                    }
                }
                "workout_10_times" -> {
                    if (totalWorkouts >= 10) {
                        unlockAchievement(achievement.copy(currentValue = totalWorkouts))
                    }
                }
                "workout_50_times" -> {
                    if (totalWorkouts >= 50) {
                        unlockAchievement(achievement.copy(currentValue = totalWorkouts))
                    }
                }
                "complete_week_1" -> {
                    if (currentWeek >= 2) { // 完成第1周，进入第2周
                        unlockAchievement(achievement.copy(currentValue = 1))
                    }
                }
                "complete_week_4" -> {
                    if (currentWeek >= 5) { // 完成前4周，进入第5周
                        unlockAchievement(achievement.copy(currentValue = 4))
                    }
                }
                "complete_8_weeks" -> {
                    if (currentWeek > 8) { // 完成全部8周
                        unlockAchievement(achievement.copy(currentValue = 8))
                    }
                }
            }
        }
    }
    
    /**
     * 检查并解锁体重相关成就
     */
    suspend fun checkWeightLossAchievements(weightLoss: Float) {
        val achievements = repository.getLockedAchievements()
        
        achievements.filter { it.type == "WEIGHT_LOSS" }.forEach { achievement ->
            when (achievement.id) {
                "weight_loss_2kg" -> {
                    if (weightLoss >= 2f) {
                        unlockAchievement(achievement.copy(currentValue = weightLoss.toInt()))
                    }
                }
                "weight_loss_5kg" -> {
                    if (weightLoss >= 5f) {
                        unlockAchievement(achievement.copy(currentValue = weightLoss.toInt()))
                    }
                }
            }
        }
    }
    
    /**
     * 检查并解锁体脂相关成就
     */
    suspend fun checkBodyFatAchievements(bodyFatReduction: Float) {
        val achievements = repository.getLockedAchievements()
        
        achievements.filter { it.type == "BODY_FAT" }.forEach { achievement ->
            when (achievement.id) {
                "body_fat_reduce_2" -> {
                    if (bodyFatReduction >= 2f) {
                        unlockAchievement(achievement.copy(currentValue = bodyFatReduction.toInt()))
                    }
                }
            }
        }
    }
    
    /**
     * 检查并解锁饮食相关成就
     */
    suspend fun checkDietAchievements(consecutiveDietDays: Int) {
        val achievements = repository.getLockedAchievements()
        
        achievements.filter { it.type == "DIET" }.forEach { achievement ->
            when (achievement.id) {
                "first_diet_record" -> {
                    if (consecutiveDietDays >= 1) {
                        unlockAchievement(achievement.copy(currentValue = consecutiveDietDays))
                    }
                }
                "diet_7_days" -> {
                    if (consecutiveDietDays >= 7) {
                        unlockAchievement(achievement.copy(currentValue = consecutiveDietDays))
                    }
                }
            }
        }
    }
    
    /**
     * 检查并解锁卡路里相关成就
     */
    suspend fun checkCalorieAchievements(caloriesBurned: Int) {
        val achievements = repository.getLockedAchievements()
        
        achievements.filter { it.type == "CALORIES" }.forEach { achievement ->
            when (achievement.id) {
                "calorie_burner_1000" -> {
                    if (caloriesBurned >= 1000) {
                        unlockAchievement(achievement.copy(currentValue = caloriesBurned))
                    }
                }
            }
        }
    }
    
    /**
     * 检查并解锁数据记录相关成就
     */
    suspend fun checkDataRecordAchievements() {
        val achievements = repository.getLockedAchievements()
        
        achievements.filter { it.type == "DATA" }.forEach { achievement ->
            when (achievement.id) {
                "first_weight_record" -> {
                    unlockAchievement(achievement.copy(currentValue = 1))
                }
            }
        }
    }
    
    /**
     * 解锁成就
     */
    private suspend fun unlockAchievement(achievement: Achievement) {
        if (achievement.isUnlocked) return
        
        val unlockedAchievement = achievement.copy(
            isUnlocked = true,
            unlockedAt = LocalDateTime.now()
        )
        
        repository.updateAchievement(unlockedAchievement)
        
        // 添加积分
        repository.addPoints(achievement.points)
        
        // 发送解锁通知
        _achievementUnlocked.emit(unlockedAchievement)
    }
    
    /**
     * 更新成就进度（不解锁）
     */
    suspend fun updateAchievementProgress(achievementId: String, currentValue: Int) {
        val achievements = repository.getLockedAchievements()
        val achievement = achievements.find { it.id == achievementId }
        
        achievement?.let {
            val updatedAchievement = it.copy(currentValue = currentValue)
            repository.updateAchievement(updatedAchievement)
        }
    }
}
