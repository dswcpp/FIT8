package com.vere.fit8.data.dao

import androidx.room.*
import com.vere.fit8.data.model.DietPlan
import kotlinx.coroutines.flow.Flow

/**
 * 饮食计划数据访问对象
 */
@Dao
interface DietPlanDao {
    
    @Query("SELECT * FROM diet_plans ORDER BY week, mealType")
    fun getAllDietPlans(): Flow<List<DietPlan>>
    
    @Query("SELECT * FROM diet_plans WHERE week = :week ORDER BY mealType")
    suspend fun getDietPlansByWeek(week: Int): List<DietPlan>
    
    @Query("SELECT * FROM diet_plans WHERE week = :week ORDER BY mealType")
    fun getDietPlansByWeekFlow(week: Int): Flow<List<DietPlan>>
    
    @Query("SELECT * FROM diet_plans WHERE week = :week AND mealType = :mealType")
    suspend fun getDietPlansByWeekAndMeal(week: Int, mealType: String): List<DietPlan>
    
    @Query("SELECT * FROM diet_plans WHERE id = :id")
    suspend fun getDietPlanById(id: String): DietPlan?
    
    @Query("SELECT SUM(calories) FROM diet_plans WHERE week = :week")
    suspend fun getTotalCaloriesByWeek(week: Int): Int
    
    @Query("SELECT SUM(protein) FROM diet_plans WHERE week = :week")
    suspend fun getTotalProteinByWeek(week: Int): Float
    
    @Query("SELECT SUM(carbs) FROM diet_plans WHERE week = :week")
    suspend fun getTotalCarbsByWeek(week: Int): Float
    
    @Query("SELECT SUM(fat) FROM diet_plans WHERE week = :week")
    suspend fun getTotalFatByWeek(week: Int): Float
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDietPlan(dietPlan: DietPlan)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDietPlans(dietPlans: List<DietPlan>)
    
    @Update
    suspend fun updateDietPlan(dietPlan: DietPlan)
    
    @Delete
    suspend fun deleteDietPlan(dietPlan: DietPlan)
    
    @Query("DELETE FROM diet_plans")
    suspend fun deleteAllDietPlans()
}
