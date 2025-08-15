package com.vere.fit8.data.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 饮水记录数据模型
 */
data class WaterRecord(
    val id: Long = 0,
    val amount: Int, // 饮水量 (ml)
    val recordDate: LocalDate, // 记录日期
    val recordTime: LocalDateTime, // 记录时间
    val notes: String = "" // 备注
)
