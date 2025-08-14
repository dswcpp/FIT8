package com.vere.fit8.data.dao

import androidx.room.*
import com.vere.fit8.data.model.Achievement
import com.vere.fit8.data.model.UserStats
import kotlinx.coroutines.flow.Flow

/**
 * 成就数据访问对象
 */
@Dao
interface AchievementDao {
    
    @Query("SELECT * FROM achievements ORDER BY isUnlocked DESC, points DESC")
    fun getAllAchievements(): Flow<List<Achievement>>
    
    @Query("SELECT * FROM achievements WHERE isUnlocked = 1 ORDER BY unlockedAt DESC")
    suspend fun getUnlockedAchievements(): List<Achievement>
    
    @Query("SELECT * FROM achievements WHERE isUnlocked = 0 ORDER BY points ASC")
    suspend fun getLockedAchievements(): List<Achievement>
    
    @Query("SELECT * FROM achievements WHERE id = :id")
    suspend fun getAchievementById(id: String): Achievement?
    
    @Query("SELECT * FROM achievements WHERE type = :type")
    suspend fun getAchievementsByType(type: String): List<Achievement>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: Achievement)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<Achievement>)
    
    @Update
    suspend fun updateAchievement(achievement: Achievement)
    
    @Delete
    suspend fun deleteAchievement(achievement: Achievement)
    
    @Query("DELETE FROM achievements")
    suspend fun deleteAllAchievements()
}

/**
 * 用户统计数据访问对象
 */
@Dao
interface UserStatsDao {
    
    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getUserStats(): UserStats?
    
    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getUserStatsFlow(): Flow<UserStats?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserStats(stats: UserStats)
    
    @Update
    suspend fun updateUserStats(stats: UserStats)
    
    @Query("UPDATE user_stats SET totalWorkouts = totalWorkouts + 1 WHERE id = 1")
    suspend fun incrementTotalWorkouts()
    
    @Query("UPDATE user_stats SET totalDays = totalDays + 1 WHERE id = 1")
    suspend fun incrementTotalDays()
    
    @Query("UPDATE user_stats SET consecutiveDays = :days WHERE id = 1")
    suspend fun updateConsecutiveDays(days: Int)
    
    @Query("UPDATE user_stats SET maxConsecutiveDays = :days WHERE id = 1")
    suspend fun updateMaxConsecutiveDays(days: Int)
    
    @Query("UPDATE user_stats SET totalCaloriesBurned = totalCaloriesBurned + :calories WHERE id = 1")
    suspend fun addCaloriesBurned(calories: Int)
    
    @Query("UPDATE user_stats SET totalPoints = totalPoints + :points WHERE id = 1")
    suspend fun addPoints(points: Int)
    
    @Query("DELETE FROM user_stats")
    suspend fun deleteUserStats()
}
