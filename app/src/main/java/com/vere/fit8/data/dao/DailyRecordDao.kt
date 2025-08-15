package com.vere.fit8.data.dao

import androidx.room.*
import com.vere.fit8.data.model.DailyRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * 每日记录数据访问对象
 */
@Dao
interface DailyRecordDao {
    
    @Query("SELECT * FROM daily_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<DailyRecord>>
    
    @Query("SELECT * FROM daily_records WHERE date = :date")
    suspend fun getRecordByDate(date: LocalDate): DailyRecord?
    
    @Query("SELECT * FROM daily_records WHERE date = :date")
    fun getRecordByDateFlow(date: LocalDate): Flow<DailyRecord?>
    
    @Query("SELECT * FROM daily_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getRecordsBetweenDates(startDate: LocalDate, endDate: LocalDate): List<DailyRecord>

    @Query("SELECT * FROM daily_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getRecordsBetweenDatesFlow(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyRecord>>
    
    @Query("SELECT * FROM daily_records WHERE date >= :startDate ORDER BY date DESC LIMIT :limit")
    suspend fun getRecentRecords(startDate: LocalDate, limit: Int): List<DailyRecord>
    
    @Query("SELECT COUNT(*) FROM daily_records WHERE trainingDurationMin > 0")
    suspend fun getTotalWorkoutDays(): Int
    
    @Query("SELECT AVG(weight) FROM daily_records WHERE weight IS NOT NULL AND date BETWEEN :startDate AND :endDate")
    suspend fun getAverageWeight(startDate: LocalDate, endDate: LocalDate): Float?
    
    @Query("SELECT AVG(bodyFat) FROM daily_records WHERE bodyFat IS NOT NULL AND date BETWEEN :startDate AND :endDate")
    suspend fun getAverageBodyFat(startDate: LocalDate, endDate: LocalDate): Float?
    
    @Query("SELECT SUM(trainingCalories) FROM daily_records WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalCaloriesBurned(startDate: LocalDate, endDate: LocalDate): Int
    
    @Query("SELECT * FROM daily_records WHERE weight IS NOT NULL ORDER BY date DESC LIMIT 30")
    suspend fun getWeightRecords(): List<DailyRecord>
    
    @Query("SELECT * FROM daily_records WHERE bodyFat IS NOT NULL ORDER BY date DESC LIMIT 30")
    suspend fun getBodyFatRecords(): List<DailyRecord>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: DailyRecord)
    
    @Update
    suspend fun updateRecord(record: DailyRecord)
    
    @Delete
    suspend fun deleteRecord(record: DailyRecord)
    
    @Query("DELETE FROM daily_records WHERE date = :date")
    suspend fun deleteRecordByDate(date: LocalDate)
    
    @Query("DELETE FROM daily_records")
    suspend fun deleteAllRecords()

    @Query("SELECT AVG(weight) FROM daily_records WHERE date BETWEEN :startDate AND :endDate AND weight IS NOT NULL")
    suspend fun getAverageWeightBetweenDates(startDate: LocalDate, endDate: LocalDate): Float?

    @Query("SELECT AVG(bodyFat) FROM daily_records WHERE date BETWEEN :startDate AND :endDate AND bodyFat IS NOT NULL")
    suspend fun getAverageBodyFatBetweenDates(startDate: LocalDate, endDate: LocalDate): Float?
}
