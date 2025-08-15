package com.vere.fit8.data.dao

import androidx.room.*
import com.vere.fit8.data.model.ExerciseDetail
import kotlinx.coroutines.flow.Flow

/**
 * 动作详情数据访问对象
 */
@Dao
interface ExerciseDetailDao {
    
    @Query("SELECT * FROM exercise_details ORDER BY name ASC")
    fun getAllExerciseDetailsFlow(): Flow<List<ExerciseDetail>>
    
    @Query("SELECT * FROM exercise_details ORDER BY name ASC")
    suspend fun getAllExerciseDetails(): List<ExerciseDetail>
    
    @Query("SELECT * FROM exercise_details WHERE id = :id")
    suspend fun getExerciseDetailById(id: String): ExerciseDetail?
    
    @Query("SELECT * FROM exercise_details WHERE id = :id")
    fun getExerciseDetailByIdFlow(id: String): Flow<ExerciseDetail?>
    
    @Query("SELECT * FROM exercise_details WHERE category = :category ORDER BY name ASC")
    suspend fun getExerciseDetailsByCategory(category: String): List<ExerciseDetail>
    
    @Query("SELECT * FROM exercise_details WHERE category = :category ORDER BY name ASC")
    fun getExerciseDetailsByCategoryFlow(category: String): Flow<List<ExerciseDetail>>
    
    @Query("SELECT * FROM exercise_details WHERE difficulty = :difficulty ORDER BY name ASC")
    suspend fun getExerciseDetailsByDifficulty(difficulty: String): List<ExerciseDetail>
    
    @Query("SELECT * FROM exercise_details WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    suspend fun searchExerciseDetails(query: String): List<ExerciseDetail>
    
    @Query("SELECT * FROM exercise_details WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchExerciseDetailsFlow(query: String): Flow<List<ExerciseDetail>>
    
    @Query("SELECT DISTINCT category FROM exercise_details ORDER BY category ASC")
    suspend fun getAllCategories(): List<String>
    
    @Query("SELECT DISTINCT difficulty FROM exercise_details ORDER BY difficulty ASC")
    suspend fun getAllDifficulties(): List<String>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseDetail(exerciseDetail: ExerciseDetail)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseDetails(exerciseDetails: List<ExerciseDetail>)
    
    @Update
    suspend fun updateExerciseDetail(exerciseDetail: ExerciseDetail)
    
    @Delete
    suspend fun deleteExerciseDetail(exerciseDetail: ExerciseDetail)
    
    @Query("DELETE FROM exercise_details WHERE id = :id")
    suspend fun deleteExerciseDetailById(id: String)
    
    @Query("DELETE FROM exercise_details")
    suspend fun deleteAllExerciseDetails()
}
