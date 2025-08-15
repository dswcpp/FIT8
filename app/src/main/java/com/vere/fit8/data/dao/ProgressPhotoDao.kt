package com.vere.fit8.data.dao

import androidx.room.*
import com.vere.fit8.data.model.ProgressPhoto
import kotlinx.coroutines.flow.Flow

/**
 * 进步照片数据访问对象
 */
@Dao
interface ProgressPhotoDao {
    
    @Query("SELECT * FROM progress_photos WHERE isDeleted = 0 ORDER BY takenDate DESC")
    fun getAllPhotosFlow(): Flow<List<ProgressPhoto>>
    
    @Query("SELECT * FROM progress_photos WHERE isDeleted = 0 ORDER BY takenDate DESC")
    suspend fun getAllPhotos(): List<ProgressPhoto>
    
    @Query("SELECT * FROM progress_photos WHERE id = :id AND isDeleted = 0")
    suspend fun getPhotoById(id: Long): ProgressPhoto?
    
    @Query("SELECT COUNT(*) FROM progress_photos WHERE isDeleted = 0")
    suspend fun getPhotoCount(): Int
    
    @Insert
    suspend fun insertPhoto(photo: ProgressPhoto): Long
    
    @Update
    suspend fun updatePhoto(photo: ProgressPhoto)
    
    @Query("UPDATE progress_photos SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun softDeletePhoto(id: Long, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM progress_photos WHERE id = :id")
    suspend fun deletePhoto(id: Long)
    
    @Query("DELETE FROM progress_photos WHERE isDeleted = 1")
    suspend fun deleteAllSoftDeletedPhotos()
    
    @Query("SELECT * FROM progress_photos WHERE takenDate BETWEEN :startDate AND :endDate AND isDeleted = 0 ORDER BY takenDate DESC")
    suspend fun getPhotosByDateRange(startDate: Long, endDate: Long): List<ProgressPhoto>
    
    @Query("UPDATE progress_photos SET weight = :weight, updatedAt = :timestamp WHERE id = :id")
    suspend fun updatePhotoWeight(id: Long, weight: Float?, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE progress_photos SET bodyFat = :bodyFat, updatedAt = :timestamp WHERE id = :id")
    suspend fun updatePhotoBodyFat(id: Long, bodyFat: Float?, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE progress_photos SET notes = :notes, updatedAt = :timestamp WHERE id = :id")
    suspend fun updatePhotoNotes(id: Long, notes: String, timestamp: Long = System.currentTimeMillis())
}
