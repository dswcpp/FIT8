package com.vere.fit8.data.dao

import androidx.room.*
import com.vere.fit8.data.model.MealRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * 饮食记录数据访问对象
 */
@Dao
interface MealRecordDao {
    
    @Query("SELECT * FROM meal_records ORDER BY date DESC, createdAt DESC")
    fun getAllMealRecords(): Flow<List<MealRecord>>
    
    @Query("SELECT * FROM meal_records WHERE date = :date ORDER BY mealType, createdAt ASC")
    suspend fun getMealRecordsByDate(date: LocalDate): List<MealRecord>
    
    @Query("SELECT * FROM meal_records WHERE date = :date ORDER BY mealType, createdAt ASC")
    fun getMealRecordsByDateFlow(date: LocalDate): Flow<List<MealRecord>>
    
    @Query("SELECT * FROM meal_records WHERE date = :date AND mealType = :mealType ORDER BY createdAt ASC")
    suspend fun getMealRecordsByDateAndType(date: LocalDate, mealType: String): List<MealRecord>
    
    @Query("SELECT * FROM meal_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC, createdAt DESC")
    suspend fun getMealRecordsBetweenDates(startDate: LocalDate, endDate: LocalDate): List<MealRecord>
    
    @Query("SELECT * FROM meal_records WHERE id = :id")
    suspend fun getMealRecordById(id: String): MealRecord?
    
    @Query("SELECT SUM(calories) FROM meal_records WHERE date = :date")
    suspend fun getTotalCaloriesByDate(date: LocalDate): Int
    
    @Query("SELECT SUM(protein) FROM meal_records WHERE date = :date")
    suspend fun getTotalProteinByDate(date: LocalDate): Float
    
    @Query("SELECT SUM(carbs) FROM meal_records WHERE date = :date")
    suspend fun getTotalCarbsByDate(date: LocalDate): Float
    
    @Query("SELECT SUM(fat) FROM meal_records WHERE date = :date")
    suspend fun getTotalFatByDate(date: LocalDate): Float
    
    @Query("SELECT AVG(calories) FROM meal_records WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getAverageCaloriesBetweenDates(startDate: LocalDate, endDate: LocalDate): Float
    
    @Query("SELECT COUNT(*) FROM meal_records WHERE date = :date")
    suspend fun getMealRecordCountByDate(date: LocalDate): Int
    
    @Query("SELECT DISTINCT mealType FROM meal_records WHERE date = :date")
    suspend fun getMealTypesByDate(date: LocalDate): List<String>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealRecord(record: MealRecord)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealRecords(records: List<MealRecord>)
    
    @Update
    suspend fun updateMealRecord(record: MealRecord)
    
    @Delete
    suspend fun deleteMealRecord(record: MealRecord)
    
    @Query("DELETE FROM meal_records WHERE id = :id")
    suspend fun deleteMealRecordById(id: String)
    
    @Query("DELETE FROM meal_records WHERE date = :date")
    suspend fun deleteMealRecordsByDate(date: LocalDate)
    
    @Query("DELETE FROM meal_records")
    suspend fun deleteAllMealRecords()
}
