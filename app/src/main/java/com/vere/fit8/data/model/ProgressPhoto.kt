package com.vere.fit8.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vere.fit8.data.converter.Converters
import java.io.Serializable
import java.time.LocalDateTime

/**
 * 进步照片数据模型
 * 记录用户的健身进步照片和相关数据
 */
@Entity(tableName = "progress_photos")
@TypeConverters(Converters::class)
data class ProgressPhoto(
    @PrimaryKey
    val id: Long,                           // 照片ID
    val filePath: String,                   // 照片文件路径
    val takenDate: LocalDateTime,           // 拍摄日期时间
    val weight: Float? = null,              // 拍摄时的体重（斤）
    val bodyFat: Float? = null,             // 拍摄时的体脂率（%）
    val notes: String = "",                 // 备注信息
    val isDeleted: Boolean = false,         // 软删除标记
    val createdAt: Long = System.currentTimeMillis(), // 创建时间
    val updatedAt: Long = System.currentTimeMillis()  // 更新时间
) : Serializable

/**
 * 照片对比数据模型
 * 用于前后对比展示
 */
data class PhotoComparison(
    val beforePhoto: ProgressPhoto,         // 之前的照片
    val afterPhoto: ProgressPhoto,          // 之后的照片
    val weightChange: Float? = null,        // 体重变化
    val bodyFatChange: Float? = null,       // 体脂率变化
    val daysBetween: Long                   // 间隔天数
) : Serializable

/**
 * 照片统计数据
 */
data class PhotoStats(
    val totalPhotos: Int,                   // 总照片数
    val firstPhotoDate: LocalDateTime?,     // 第一张照片日期
    val latestPhotoDate: LocalDateTime?,    // 最新照片日期
    val totalDays: Long,                    // 总记录天数
    val averageInterval: Double             // 平均拍摄间隔（天）
) : Serializable
