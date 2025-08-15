package com.vere.fit8.data.repository

import com.vere.fit8.data.dao.*
import com.vere.fit8.data.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 燃力8周应用数据仓库
 * 统一管理所有数据访问逻辑
 */
@Singleton
class Fit8Repository @Inject constructor(
    private val dailyRecordDao: DailyRecordDao,
    private val weeklyPlanDao: WeeklyPlanDao,
    private val dietPlanDao: DietPlanDao,
    private val mealRecordDao: MealRecordDao,
    private val achievementDao: AchievementDao,
    private val userStatsDao: UserStatsDao,
    private val appSettingsDao: com.vere.fit8.data.dao.AppSettingsDao
) {
    
    // ==================== 每日记录相关 ====================
    
    fun getAllDailyRecords(): Flow<List<DailyRecord>> = dailyRecordDao.getAllRecords()
    
    suspend fun getDailyRecord(date: LocalDate): DailyRecord? = dailyRecordDao.getRecordByDate(date)
    
    fun getDailyRecordFlow(date: LocalDate): Flow<DailyRecord?> = dailyRecordDao.getRecordByDateFlow(date)
    
    suspend fun getDailyRecords(startDate: LocalDate, endDate: LocalDate): List<DailyRecord> =
        dailyRecordDao.getRecordsBetweenDates(startDate, endDate)

    suspend fun getWeeklyRecords(startDate: LocalDate, endDate: LocalDate): List<DailyRecord> =
        dailyRecordDao.getRecordsBetweenDates(startDate, endDate)

    fun getWeeklyRecordsFlow(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyRecord>> =
        dailyRecordDao.getRecordsBetweenDatesFlow(startDate, endDate)
    
    suspend fun saveDailyRecord(record: DailyRecord) = dailyRecordDao.insertRecord(record)
    
    suspend fun updateDailyRecord(record: DailyRecord) = dailyRecordDao.updateRecord(record)
    
    suspend fun deleteDailyRecord(date: LocalDate) = dailyRecordDao.deleteRecordByDate(date)
    
    suspend fun getWeightRecords(): List<DailyRecord> = dailyRecordDao.getWeightRecords()
    
    suspend fun getBodyFatRecords(): List<DailyRecord> = dailyRecordDao.getBodyFatRecords()
    
    suspend fun getTotalWorkoutDays(): Int = dailyRecordDao.getTotalWorkoutDays()
    
    suspend fun getAverageWeight(startDate: LocalDate, endDate: LocalDate): Float? =
        dailyRecordDao.getAverageWeight(startDate, endDate)
    
    suspend fun getAverageBodyFat(startDate: LocalDate, endDate: LocalDate): Float? =
        dailyRecordDao.getAverageBodyFat(startDate, endDate)
    
    suspend fun getTotalCaloriesBurned(startDate: LocalDate, endDate: LocalDate): Int =
        dailyRecordDao.getTotalCaloriesBurned(startDate, endDate)
    
    // ==================== 训练计划相关 ====================
    
    fun getAllWeeklyPlans(): Flow<List<WeeklyPlan>> = weeklyPlanDao.getAllPlans()
    
    suspend fun getWeeklyPlans(week: Int): List<WeeklyPlan> = weeklyPlanDao.getPlansByWeek(week)
    
    fun getWeeklyPlansFlow(week: Int): Flow<List<WeeklyPlan>> = weeklyPlanDao.getPlansByWeekFlow(week)
    
    suspend fun getDailyPlan(week: Int, dayOfWeek: Int): WeeklyPlan? =
        weeklyPlanDao.getPlanByWeekAndDay(week, dayOfWeek)
    
    fun getDailyPlanFlow(week: Int, dayOfWeek: Int): Flow<WeeklyPlan?> =
        weeklyPlanDao.getPlanByWeekAndDayFlow(week, dayOfWeek)
    
    suspend fun saveWeeklyPlan(plan: WeeklyPlan) = weeklyPlanDao.insertPlan(plan)
    
    suspend fun saveWeeklyPlans(plans: List<WeeklyPlan>) = weeklyPlanDao.insertPlans(plans)

    // ==================== 训练记录相关 ====================

    suspend fun saveTrainingRecord(date: LocalDate, exerciseName: String, setRecord: SetRecord) {
        val dailyRecord = getDailyRecord(date) ?: createEmptyDailyRecord(date)
        val updatedTrainingList = dailyRecord.trainingList.toMutableList()

        // 查找对应的训练动作
        val exerciseIndex = updatedTrainingList.indexOfFirst { it.name == exerciseName }
        if (exerciseIndex >= 0) {
            // 更新现有动作记录
            val exercise = updatedTrainingList[exerciseIndex]
            val updatedSetRecords = exercise.setRecords.toMutableList()

            // 添加或更新组记录
            val setIndex = updatedSetRecords.indexOfFirst { it.setNumber == setRecord.setNumber }
            if (setIndex >= 0) {
                updatedSetRecords[setIndex] = setRecord
            } else {
                updatedSetRecords.add(setRecord)
            }

            // 更新动作记录
            val updatedExercise = exercise.copy(
                completedSets = updatedSetRecords.count { it.completed },
                setRecords = updatedSetRecords,
                completed = updatedSetRecords.size >= exercise.targetSets && updatedSetRecords.all { it.completed }
            )
            updatedTrainingList[exerciseIndex] = updatedExercise
        } else {
            // 创建新的动作记录
            val newExercise = TrainingExercise(
                name = exerciseName,
                targetSets = 3, // 默认值，实际应该从训练计划获取
                completedSets = if (setRecord.completed) 1 else 0,
                setRecords = listOf(setRecord),
                completed = false
            )
            updatedTrainingList.add(newExercise)
        }

        // 保存更新后的记录
        val updatedDailyRecord = dailyRecord.copy(trainingList = updatedTrainingList)
        saveDailyRecord(updatedDailyRecord)
    }

    suspend fun getExerciseProgress(date: LocalDate, exerciseName: String): TrainingExercise? {
        val dailyRecord = getDailyRecord(date)
        return dailyRecord?.trainingList?.find { it.name == exerciseName }
    }

    private fun createEmptyDailyRecord(date: LocalDate): DailyRecord {
        return DailyRecord(date = date)
    }

    // ==================== 饮食计划相关 ====================
    
    fun getAllDietPlans(): Flow<List<DietPlan>> = dietPlanDao.getAllDietPlans()
    
    suspend fun getDietPlans(week: Int): List<DietPlan> = dietPlanDao.getDietPlansByWeek(week)
    
    fun getDietPlansFlow(week: Int): Flow<List<DietPlan>> = dietPlanDao.getDietPlansByWeekFlow(week)
    
    suspend fun getDietPlansByMeal(week: Int, mealType: String): List<DietPlan> =
        dietPlanDao.getDietPlansByWeekAndMeal(week, mealType)

    suspend fun getDietPlansByDay(week: Int, dayOfWeek: Int): List<DietPlan> =
        dietPlanDao.getDietPlansByWeekAndDay(week, dayOfWeek)

    fun getDietPlansByDayFlow(week: Int, dayOfWeek: Int): Flow<List<DietPlan>> =
        dietPlanDao.getDietPlansByWeekAndDayFlow(week, dayOfWeek)

    suspend fun saveDietPlan(dietPlan: DietPlan) = dietPlanDao.insertDietPlan(dietPlan)
    
    suspend fun saveDietPlans(dietPlans: List<DietPlan>) = dietPlanDao.insertDietPlans(dietPlans)

    // ==================== 饮食记录相关 ====================

    fun getAllMealRecords(): Flow<List<MealRecord>> = mealRecordDao.getAllMealRecords()

    suspend fun getMealRecords(date: LocalDate): List<MealRecord> = mealRecordDao.getMealRecordsByDate(date)

    fun getMealRecordsFlow(date: LocalDate): Flow<List<MealRecord>> = mealRecordDao.getMealRecordsByDateFlow(date)

    suspend fun getMealRecordsByType(date: LocalDate, mealType: String): List<MealRecord> =
        mealRecordDao.getMealRecordsByDateAndType(date, mealType)

    suspend fun saveMealRecord(record: MealRecord) = mealRecordDao.insertMealRecord(record)

    suspend fun saveMealRecords(records: List<MealRecord>) = mealRecordDao.insertMealRecords(records)

    suspend fun updateMealRecord(record: MealRecord) = mealRecordDao.updateMealRecord(record)

    suspend fun deleteMealRecord(record: MealRecord) = mealRecordDao.deleteMealRecord(record)

    suspend fun getTotalCaloriesByDate(date: LocalDate): Int = mealRecordDao.getTotalCaloriesByDate(date)

    suspend fun getTotalProteinByDate(date: LocalDate): Float = mealRecordDao.getTotalProteinByDate(date)

    suspend fun getTotalCarbsByDate(date: LocalDate): Float = mealRecordDao.getTotalCarbsByDate(date)

    suspend fun getTotalFatByDate(date: LocalDate): Float = mealRecordDao.getTotalFatByDate(date)

    // ==================== 成就系统相关 ====================
    
    fun getAllAchievements(): Flow<List<Achievement>> = achievementDao.getAllAchievements()
    
    suspend fun getUnlockedAchievements(): List<Achievement> = achievementDao.getUnlockedAchievements()

    suspend fun getLockedAchievements(): List<Achievement> = achievementDao.getLockedAchievements()

    suspend fun saveAchievement(achievement: Achievement) = achievementDao.insertAchievement(achievement)

    suspend fun saveAchievements(achievements: List<Achievement>) = achievementDao.insertAchievements(achievements)

    suspend fun updateAchievement(achievement: Achievement) = achievementDao.updateAchievement(achievement)
    
    // ==================== 用户统计相关 ====================
    
    suspend fun getUserStats(): UserStats? = userStatsDao.getUserStats()
    
    fun getUserStatsFlow(): Flow<UserStats?> = userStatsDao.getUserStatsFlow()
    
    suspend fun saveUserStats(stats: UserStats) = userStatsDao.insertUserStats(stats)
    
    suspend fun updateUserStats(stats: UserStats) = userStatsDao.updateUserStats(stats)
    
    suspend fun incrementTotalWorkouts() = userStatsDao.incrementTotalWorkouts()
    
    suspend fun incrementTotalDays() = userStatsDao.incrementTotalDays()
    
    suspend fun updateConsecutiveDays(days: Int) = userStatsDao.updateConsecutiveDays(days)
    
    suspend fun addCaloriesBurned(calories: Int) = userStatsDao.addCaloriesBurned(calories)

    suspend fun addPoints(points: Int) {
        val stats = getUserStats()
        if (stats != null) {
            val updatedStats = stats.copy(totalPoints = stats.totalPoints + points)
            userStatsDao.updateUserStats(updatedStats)
        }
    }

    // ==================== 应用设置相关 ====================

    suspend fun getAppSettings(): com.vere.fit8.data.model.AppSettings? = appSettingsDao.getSettings()

    fun getAppSettingsFlow(): kotlinx.coroutines.flow.Flow<com.vere.fit8.data.model.AppSettings?> = appSettingsDao.getSettingsFlow()

    suspend fun saveAppSettings(settings: com.vere.fit8.data.model.AppSettings) = appSettingsDao.insertSettings(settings)

    suspend fun updateAppSettings(settings: com.vere.fit8.data.model.AppSettings) = appSettingsDao.updateSettings(settings)

    // 通知设置
    suspend fun updateTrainingReminderEnabled(enabled: Boolean) = appSettingsDao.updateTrainingReminderEnabled(enabled)

    suspend fun updateTrainingReminderTime(time: String) = appSettingsDao.updateTrainingReminderTime(time)

    suspend fun updateWaterReminderEnabled(enabled: Boolean) = appSettingsDao.updateWaterReminderEnabled(enabled)

    suspend fun updateWaterReminderInterval(interval: Int) = appSettingsDao.updateWaterReminderInterval(interval)

    suspend fun updateSleepReminderEnabled(enabled: Boolean) = appSettingsDao.updateSleepReminderEnabled(enabled)

    suspend fun updateSleepReminderTime(time: String) = appSettingsDao.updateSleepReminderTime(time)

    // 应用设置
    suspend fun updateDarkModeEnabled(enabled: Boolean) = appSettingsDao.updateDarkModeEnabled(enabled)

    suspend fun updateLanguage(language: String) = appSettingsDao.updateLanguage(language)

    suspend fun updateAutoSyncEnabled(enabled: Boolean) = appSettingsDao.updateAutoSyncEnabled(enabled)

    // 用户信息
    suspend fun updateUserName(name: String) = appSettingsDao.updateUserName(name)

    suspend fun updateUserHeight(height: Float) = appSettingsDao.updateUserHeight(height)

    suspend fun updateUserGender(gender: String) = appSettingsDao.updateUserGender(gender)

    suspend fun updateUserAge(age: Int) = appSettingsDao.updateUserAge(age)

    suspend fun updateUserGoal(goal: String) = appSettingsDao.updateUserGoal(goal)

    // 数据管理
    suspend fun resetAllUserData() {
        // 清除所有用户数据，但保留设置
        dailyRecordDao.deleteAllRecords()
        mealRecordDao.deleteAllMealRecords()
        // 重置用户统计
        val defaultStats = com.vere.fit8.data.model.UserStats()
        userStatsDao.insertUserStats(defaultStats)
    }
}
