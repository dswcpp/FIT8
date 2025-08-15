package com.vere.fit8.data.initializer

import com.vere.fit8.data.model.Achievement
import com.vere.fit8.data.repository.Fit8Repository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 成就系统初始化器
 * 创建预设的成就列表
 */
@Singleton
class AchievementInitializer @Inject constructor(
    private val repository: Fit8Repository
) {
    
    suspend fun initializeAchievements() {
        // 检查是否已经初始化过
        val existingAchievements = repository.getUnlockedAchievements() + repository.getLockedAchievements()
        if (existingAchievements.isNotEmpty()) {
            return // 已经初始化过，跳过
        }
        
        // 创建成就列表
        val achievements = createAchievementList()
        
        // 保存到数据库
        repository.saveAchievements(achievements)
    }
    
    private fun createAchievementList(): List<Achievement> {
        return listOf(
            // 打卡相关成就
            Achievement(
                id = "first_checkin",
                title = "初来乍到",
                description = "完成第一次打卡",
                type = "CHECKIN",
                targetValue = 1,
                currentValue = 0,
                points = 10,
                iconRes = "ic_first_checkin",
                isUnlocked = false
            ),
            Achievement(
                id = "checkin_3_days",
                title = "三天坚持",
                description = "连续打卡3天",
                type = "CHECKIN",
                targetValue = 3,
                currentValue = 0,
                points = 30,
                iconRes = "ic_checkin_streak",
                isUnlocked = false
            ),
            Achievement(
                id = "checkin_7_days",
                title = "一周达人",
                description = "连续打卡7天",
                type = "CHECKIN",
                targetValue = 7,
                currentValue = 0,
                points = 70,
                iconRes = "ic_week_master",
                isUnlocked = false
            ),
            Achievement(
                id = "checkin_30_days",
                title = "月度冠军",
                description = "连续打卡30天",
                type = "CHECKIN",
                targetValue = 30,
                currentValue = 0,
                points = 300,
                iconRes = "ic_month_champion",
                isUnlocked = false
            ),

            // 训练相关成就
            Achievement(
                id = "first_workout",
                title = "训练新手",
                description = "完成第一次训练",
                type = "TRAINING",
                targetValue = 1,
                currentValue = 0,
                points = 20,
                iconRes = "ic_first_workout",
                isUnlocked = false
            ),
            Achievement(
                id = "workout_10_times",
                title = "训练达人",
                description = "完成10次训练",
                type = "TRAINING",
                targetValue = 10,
                currentValue = 0,
                points = 100,
                iconRes = "ic_training_master",
                isUnlocked = false
            ),
            Achievement(
                id = "workout_50_times",
                title = "训练专家",
                description = "完成50次训练",
                type = "TRAINING",
                targetValue = 50,
                currentValue = 0,
                points = 500,
                iconRes = "ic_training_expert",
                isUnlocked = false
            ),
            Achievement(
                id = "complete_week_1",
                title = "第一周完成",
                description = "完成第1周的所有训练",
                type = "WEEKLY",
                targetValue = 1,
                currentValue = 0,
                points = 150,
                iconRes = "ic_week_complete",
                isUnlocked = false
            ),
            Achievement(
                id = "complete_week_4",
                title = "半程英雄",
                description = "完成前4周训练",
                type = "WEEKLY",
                targetValue = 4,
                currentValue = 0,
                points = 400,
                iconRes = "ic_half_hero",
                isUnlocked = false
            ),
            Achievement(
                id = "complete_8_weeks",
                title = "燃力冠军",
                description = "完成完整的8周训练计划",
                type = "WEEKLY",
                targetValue = 8,
                currentValue = 0,
                points = 1000,
                iconRes = "ic_champion",
                isUnlocked = false
            ),
            
            // 身体数据相关成就
            Achievement(
                id = "first_weight_record",
                title = "记录开始",
                description = "第一次记录体重",
                type = "DATA",
                targetValue = 1,
                currentValue = 0,
                points = 10,
                iconRes = "ic_first_record",
                isUnlocked = false
            ),
            Achievement(
                id = "weight_loss_2kg",
                title = "减重先锋",
                description = "体重减少2斤",
                type = "WEIGHT_LOSS",
                targetValue = 2,
                currentValue = 0,
                points = 200,
                iconRes = "ic_weight_loss",
                isUnlocked = false
            ),
            Achievement(
                id = "weight_loss_5kg",
                title = "减重达人",
                description = "体重减少5斤",
                type = "WEIGHT_LOSS",
                targetValue = 5,
                currentValue = 0,
                points = 500,
                iconRes = "ic_weight_master",
                isUnlocked = false
            ),
            Achievement(
                id = "body_fat_reduce_2",
                title = "体脂杀手",
                description = "体脂率降低2%",
                type = "BODY_FAT",
                targetValue = 2,
                currentValue = 0,
                points = 300,
                iconRes = "ic_body_fat_killer",
                isUnlocked = false
            ),
            
            // 饮食相关成就
            Achievement(
                id = "first_diet_record",
                title = "饮食记录员",
                description = "第一次记录饮食",
                type = "DIET",
                targetValue = 1,
                currentValue = 0,
                points = 15,
                iconRes = "ic_diet_recorder",
                isUnlocked = false
            ),
            Achievement(
                id = "diet_7_days",
                title = "饮食管家",
                description = "连续7天记录饮食",
                type = "DIET",
                targetValue = 7,
                currentValue = 0,
                points = 150,
                iconRes = "ic_diet_manager",
                isUnlocked = false
            ),

            // 特殊成就
            Achievement(
                id = "early_bird",
                title = "早起鸟儿",
                description = "早上6点前完成训练",
                type = "SPECIAL",
                targetValue = 1,
                currentValue = 0,
                points = 50,
                iconRes = "ic_early_bird",
                isUnlocked = false
            ),
            Achievement(
                id = "night_owl",
                title = "夜猫子",
                description = "晚上10点后完成训练",
                type = "SPECIAL",
                targetValue = 1,
                currentValue = 0,
                points = 50,
                iconRes = "ic_night_owl",
                isUnlocked = false
            ),
            Achievement(
                id = "perfectionist",
                title = "完美主义者",
                description = "一天内完成所有任务（训练+饮食+数据记录）",
                type = "SPECIAL",
                targetValue = 1,
                currentValue = 0,
                points = 100,
                iconRes = "ic_perfectionist",
                isUnlocked = false
            ),
            Achievement(
                id = "calorie_burner_1000",
                title = "卡路里杀手",
                description = "单次训练消耗1000卡路里",
                type = "CALORIES",
                targetValue = 1000,
                currentValue = 0,
                points = 200,
                iconRes = "ic_calorie_killer",
                isUnlocked = false
            )
        )
    }
}
