package com.vere.fit8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.model.WaterRecord
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WaterViewModel @Inject constructor(
    private val repository: Fit8Repository
) : ViewModel() {
    
    private val _todayWaterAmount = MutableStateFlow(0)
    val todayWaterAmount: StateFlow<Int> = _todayWaterAmount.asStateFlow()
    
    private val _todayRecords = MutableStateFlow<List<WaterRecord>>(emptyList())
    val todayRecords: StateFlow<List<WaterRecord>> = _todayRecords.asStateFlow()
    
    private val _addResult = MutableStateFlow(false)
    val addResult: StateFlow<Boolean> = _addResult.asStateFlow()
    
    fun loadTodayData() {
        viewModelScope.launch {
            try {
                // 从DailyRecord加载今日饮水数据
                val today = LocalDate.now()
                val dailyRecord = repository.getDailyRecord(today)
                val currentWaterAmount = dailyRecord?.waterMl ?: 0

                // 更新总量
                _todayWaterAmount.value = currentWaterAmount

                // 生成模拟的记录列表（用于显示历史记录）
                // 实际项目中应该有专门的WaterRecord表来存储详细记录
                val mockRecords = if (currentWaterAmount > 0) {
                    listOf(
                        WaterRecord(
                            id = 1,
                            amount = currentWaterAmount,
                            recordDate = today,
                            recordTime = LocalDateTime.now()
                        )
                    )
                } else {
                    emptyList()
                }

                _todayRecords.value = mockRecords

            } catch (e: Exception) {
                e.printStackTrace()
                // 如果加载失败，使用默认值
                _todayWaterAmount.value = 0
                _todayRecords.value = emptyList()
            }
        }
    }
    
    fun addWaterRecord(amount: Int) {
        viewModelScope.launch {
            try {
                // 保存饮水记录到数据库
                val newRecord = WaterRecord(
                    id = System.currentTimeMillis(),
                    amount = amount,
                    recordDate = LocalDate.now(),
                    recordTime = LocalDateTime.now()
                )

                // 模拟网络延迟
                kotlinx.coroutines.delay(300)

                // 更新记录列表
                val currentRecords = _todayRecords.value.toMutableList()
                currentRecords.add(0, newRecord) // 添加到列表开头
                _todayRecords.value = currentRecords

                // 计算新的总量
                val newTotalAmount = currentRecords.sumOf { it.amount }
                _todayWaterAmount.value = newTotalAmount

                // 同步更新到DailyRecord
                updateDailyRecordWater(newTotalAmount)

                // 添加成功
                _addResult.value = true

            } catch (e: Exception) {
                // 添加失败
                _addResult.value = false
                e.printStackTrace()
            }
        }
    }

    private suspend fun updateDailyRecordWater(totalWaterMl: Int) {
        try {
            val today = LocalDate.now()
            val currentRecord = repository.getDailyRecord(today) ?: createEmptyRecord(today)
            val updatedRecord = currentRecord.copy(
                waterMl = totalWaterMl,
                updatedAt = System.currentTimeMillis()
            )

            repository.saveDailyRecord(updatedRecord)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createEmptyRecord(date: LocalDate): com.vere.fit8.data.model.DailyRecord {
        return com.vere.fit8.data.model.DailyRecord(
            date = date,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
    
    fun deleteWaterRecord(recordId: Long) {
        viewModelScope.launch {
            try {
                // 模拟从数据库删除记录
                val currentRecords = _todayRecords.value.toMutableList()
                currentRecords.removeAll { it.id == recordId }
                _todayRecords.value = currentRecords
                
                // 更新总量
                _todayWaterAmount.value = currentRecords.sumOf { it.amount }
                
            } catch (e: Exception) {
                // 删除失败，可以显示错误消息
            }
        }
    }
}
