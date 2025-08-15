package com.vere.fit8

import android.app.Application
import com.vere.fit8.data.initializer.AchievementInitializer
import com.vere.fit8.data.initializer.DietPlanInitializer
import com.vere.fit8.data.initializer.ExerciseDetailInitializer
import com.vere.fit8.data.initializer.TrainingPlanInitializer
import com.vere.fit8.data.model.UserStats
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 燃力8周 - 应用程序入口
 * 初始化Hilt依赖注入框架和基础数据
 */
@HiltAndroidApp
class Fit8Application : Application() {

    @Inject
    lateinit var trainingPlanInitializer: TrainingPlanInitializer

    @Inject
    lateinit var dietPlanInitializer: DietPlanInitializer

    @Inject
    lateinit var achievementInitializer: AchievementInitializer

    @Inject
    lateinit var exerciseDetailInitializer: ExerciseDetailInitializer

    @Inject
    lateinit var repository: Fit8Repository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // 初始化基础数据
        initializeData()
    }

    private fun initializeData() {
        applicationScope.launch {
            try {
                // 初始化用户统计数据
                initializeUserStats()

                // 初始化训练计划
                trainingPlanInitializer.initializeTrainingPlans()

                // 初始化饮食计划
                dietPlanInitializer.initializeDietPlans()

                // 初始化成就系统
                achievementInitializer.initializeAchievements()

                // 初始化动作详情
                exerciseDetailInitializer.initializeExerciseDetails()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun initializeUserStats() {
        val existingStats = repository.getUserStats()
        if (existingStats == null) {
            // 创建默认用户统计数据
            val defaultStats = UserStats(
                id = 1,
                totalWorkouts = 0,
                totalDays = 0,
                consecutiveDays = 0,
                maxConsecutiveDays = 0,
                totalCaloriesBurned = 0,
                totalPoints = 0,
                currentWeek = 1,
                totalMinutes = 0,
                badges = 0,
                startDate = null,
                lastActiveDate = null
            )
            repository.saveUserStats(defaultStats)
        }
    }
}
