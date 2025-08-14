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
    private val userStatsDao: UserStatsDao
) {
    
    // ==================== 每日记录相关 ====================
    
    fun getAllDailyRecords(): Flow<List<DailyRecord>> = dailyRecordDao.getAllRecords()
    
    suspend fun getDailyRecord(date: LocalDate): DailyRecord? = dailyRecordDao.getRecordByDate(date)
    
    fun getDailyRecordFlow(date: LocalDate): Flow<DailyRecord?> = dailyRecordDao.getRecordByDateFlow(date)
    
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
    
    // ==================== 饮食计划相关 ====================
    
    fun getAllDietPlans(): Flow<List<DietPlan>> = dietPlanDao.getAllDietPlans()
    
    suspend fun getDietPlans(week: Int): List<DietPlan> = dietPlanDao.getDietPlansByWeek(week)
    
    fun getDietPlansFlow(week: Int): Flow<List<DietPlan>> = dietPlanDao.getDietPlansByWeekFlow(week)
    
    suspend fun getDietPlansByMeal(week: Int, mealType: String): List<DietPlan> =
        dietPlanDao.getDietPlansByWeekAndMeal(week, mealType)
    
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
    
    suspend fun addPoints(points: Int) = userStatsDao.addPoints(points)
}
