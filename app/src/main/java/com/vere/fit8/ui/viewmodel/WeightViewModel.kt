package com.vere.fit8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.model.DailyRecord
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val repository: Fit8Repository
) : ViewModel() {

    private val _currentWeight = MutableStateFlow(0.0)
    val currentWeight: StateFlow<Double> = _currentWeight.asStateFlow()

    private val _currentBodyFat = MutableStateFlow(0.0)
    val currentBodyFat: StateFlow<Double> = _currentBodyFat.asStateFlow()

    private val _saveResult = MutableStateFlow(false)
    val saveResult: StateFlow<Boolean> = _saveResult.asStateFlow()

    fun loadCurrentData() {
        viewModelScope.launch {
            try {
                // 从数据库加载最近的体重和体脂数据
                val recentRecord = repository.getDailyRecord(LocalDate.now())
                    ?: repository.getWeightRecords().lastOrNull()

                recentRecord?.let { record ->
                    record.weight?.let { _currentWeight.value = it.toDouble() }
                    record.bodyFat?.let { _currentBodyFat.value = it.toDouble() }
                }
            } catch (e: Exception) {
                // 加载失败，使用默认值
            }
        }
    }

    fun saveBodyData(weight: Double, bodyFat: Double?, date: LocalDate, notes: String) {
        viewModelScope.launch {
            try {
                // 获取或创建当日记录
                val existingRecord = repository.getDailyRecord(date)
                val updatedRecord = if (existingRecord != null) {
                    // 更新现有记录
                    existingRecord.copy(
                        weight = weight.toFloat(),
                        bodyFat = bodyFat?.toFloat(),
                        notes = if (notes.isNotEmpty()) notes else existingRecord.notes,
                        updatedAt = System.currentTimeMillis()
                    )
                } else {
                    // 创建新记录
                    DailyRecord(
                        date = date,
                        weight = weight.toFloat(),
                        bodyFat = bodyFat?.toFloat(),
                        notes = notes
                    )
                }

                // 保存到数据库
                if (existingRecord != null) {
                    repository.updateDailyRecord(updatedRecord)
                } else {
                    repository.saveDailyRecord(updatedRecord)
                }

                // 更新当前显示的数据
                _currentWeight.value = weight
                bodyFat?.let { _currentBodyFat.value = it }

                // 保存成功
                _saveResult.value = true

            } catch (e: Exception) {
                // 保存失败
                _saveResult.value = false
            }
        }
    }
}
