package com.vere.fit8.data.dao

import androidx.room.*
import com.vere.fit8.data.model.WeeklyPlan
import kotlinx.coroutines.flow.Flow

/**
 * 周计划数据访问对象
 */
@Dao
interface WeeklyPlanDao {
    
    @Query("SELECT * FROM weekly_plans ORDER BY week, dayOfWeek")
    fun getAllPlans(): Flow<List<WeeklyPlan>>
    
    @Query("SELECT * FROM weekly_plans WHERE week = :week ORDER BY dayOfWeek")
    suspend fun getPlansByWeek(week: Int): List<WeeklyPlan>
    
    @Query("SELECT * FROM weekly_plans WHERE week = :week ORDER BY dayOfWeek")
    fun getPlansByWeekFlow(week: Int): Flow<List<WeeklyPlan>>
    
    @Query("SELECT * FROM weekly_plans WHERE week = :week AND dayOfWeek = :dayOfWeek")
    suspend fun getPlanByWeekAndDay(week: Int, dayOfWeek: Int): WeeklyPlan?
    
    @Query("SELECT * FROM weekly_plans WHERE week = :week AND dayOfWeek = :dayOfWeek")
    fun getPlanByWeekAndDayFlow(week: Int, dayOfWeek: Int): Flow<WeeklyPlan?>
    
    @Query("SELECT * FROM weekly_plans WHERE id = :id")
    suspend fun getPlanById(id: String): WeeklyPlan?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: WeeklyPlan)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlans(plans: List<WeeklyPlan>)
    
    @Update
    suspend fun updatePlan(plan: WeeklyPlan)
    
    @Delete
    suspend fun deletePlan(plan: WeeklyPlan)
    
    @Query("DELETE FROM weekly_plans")
    suspend fun deleteAllPlans()
}
