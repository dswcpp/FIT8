package com.vere.fit8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.model.DailyRecord
import com.vere.fit8.data.model.UserStats
import com.vere.fit8.data.model.WeeklyPlan
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * 首页ViewModel
 * 管理首页数据和业务逻辑
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Fit8Repository
) : ViewModel() {
    
    private val _todayRecord = MutableStateFlow<DailyRecord?>(null)
    val todayRecord: StateFlow<DailyRecord?> = _todayRecord.asStateFlow()
    
    private val _todayPlan = MutableStateFlow<WeeklyPlan?>(null)
    val todayPlan: StateFlow<WeeklyPlan?> = _todayPlan.asStateFlow()
    
    private val _userStats = MutableStateFlow<UserStats?>(null)
    val userStats: StateFlow<UserStats?> = _userStats.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadTodayData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val today = LocalDate.now()
                
                // 加载今日记录
                val record = repository.getDailyRecord(today) ?: createEmptyRecord(today)
                _todayRecord.value = record
                
                // 加载今日训练计划
                val stats = repository.getUserStats()
                val currentWeek = stats?.currentWeek ?: 1
                val dayOfWeek = today.dayOfWeek.value
                val plan = repository.getDailyPlan(currentWeek, dayOfWeek)
                _todayPlan.value = plan
                
                // 加载用户统计
                _userStats.value = stats
                
            } catch (e: Exception) {
                // 处理错误
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateWeight(weight: Float) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val currentRecord = _todayRecord.value ?: createEmptyRecord(today)
            val updatedRecord = currentRecord.copy(
                weight = weight,
                bmi = calculateBMI(weight, 170f), // 假设身高170cm，实际应该从用户设置获取
                updatedAt = System.currentTimeMillis()
            )
            
            repository.saveDailyRecord(updatedRecord)
            _todayRecord.value = updatedRecord
        }
    }
    
    fun updateBodyFat(bodyFat: Float) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val currentRecord = _todayRecord.value ?: createEmptyRecord(today)
            val updatedRecord = currentRecord.copy(
                bodyFat = bodyFat,
                updatedAt = System.currentTimeMillis()
            )
            
            repository.saveDailyRecord(updatedRecord)
            _todayRecord.value = updatedRecord
        }
    }
    
    fun updateWaterIntake(waterMl: Int) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val currentRecord = _todayRecord.value ?: createEmptyRecord(today)
            val updatedRecord = currentRecord.copy(
                waterMl = waterMl,
                updatedAt = System.currentTimeMillis()
            )
            
            repository.saveDailyRecord(updatedRecord)
            _todayRecord.value = updatedRecord
        }
    }
    
    fun updateSleep(sleepHours: Float) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val currentRecord = _todayRecord.value ?: createEmptyRecord(today)
            val updatedRecord = currentRecord.copy(
                sleepHours = sleepHours,
                updatedAt = System.currentTimeMillis()
            )
            
            repository.saveDailyRecord(updatedRecord)
            _todayRecord.value = updatedRecord
        }
    }
    
    fun completeTraining(durationMin: Int, calories: Int) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val currentRecord = _todayRecord.value ?: createEmptyRecord(today)
            val updatedRecord = currentRecord.copy(
                trainingDurationMin = durationMin,
                trainingCalories = calories,
                updatedAt = System.currentTimeMillis()
            )
            
            repository.saveDailyRecord(updatedRecord)
            _todayRecord.value = updatedRecord
            
            // 更新用户统计
            repository.incrementTotalWorkouts()
            repository.addCaloriesBurned(calories)
        }
    }
    
    fun updateDietStatus(dietOk: Boolean) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val currentRecord = _todayRecord.value ?: createEmptyRecord(today)
            val updatedRecord = currentRecord.copy(
                dietOk = dietOk,
                updatedAt = System.currentTimeMillis()
            )
            
            repository.saveDailyRecord(updatedRecord)
            _todayRecord.value = updatedRecord
        }
    }
    
    private fun createEmptyRecord(date: LocalDate): DailyRecord {
        return DailyRecord(
            date = date,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
    
    private fun calculateBMI(weightKg: Float, heightCm: Float): Float {
        val heightM = heightCm / 100f
        return weightKg / (heightM * heightM)
    }
}
