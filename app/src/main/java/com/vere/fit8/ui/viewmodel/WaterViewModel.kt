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
            // 模拟从数据库加载今日饮水数据
            val mockRecords = listOf(
                WaterRecord(
                    id = 1,
                    amount = 250,
                    recordDate = LocalDate.now(),
                    recordTime = LocalDateTime.now().minusHours(2)
                ),
                WaterRecord(
                    id = 2,
                    amount = 500,
                    recordDate = LocalDate.now(),
                    recordTime = LocalDateTime.now().minusHours(1)
                )
            )
            
            _todayRecords.value = mockRecords
            _todayWaterAmount.value = mockRecords.sumOf { it.amount }
        }
    }
    
    fun addWaterRecord(amount: Int) {
        viewModelScope.launch {
            try {
                // 模拟保存饮水记录到数据库
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
                
                // 更新总量
                _todayWaterAmount.value = currentRecords.sumOf { it.amount }
                
                // 添加成功
                _addResult.value = true
                
            } catch (e: Exception) {
                // 添加失败
                _addResult.value = false
            }
        }
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
