package com.vere.fit8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.model.ExerciseTemplate
import com.vere.fit8.data.model.SetRecord
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * 训练动作执行ViewModel
 * 管理单个动作的计数或计时逻辑
 */
@HiltViewModel
class ExerciseExecutionViewModel @Inject constructor(
    private val repository: Fit8Repository
) : ViewModel() {
    
    private val _exercise = MutableStateFlow<ExerciseTemplate?>(null)
    val exercise: StateFlow<ExerciseTemplate?> = _exercise.asStateFlow()
    
    private val _currentSet = MutableStateFlow(1)
    val currentSet: StateFlow<Int> = _currentSet.asStateFlow()
    
    private val _currentCount = MutableStateFlow(0)
    val currentCount: StateFlow<Int> = _currentCount.asStateFlow()
    
    private val _targetCount = MutableStateFlow(0)
    val targetCount: StateFlow<Int> = _targetCount.asStateFlow()
    
    private val _currentTime = MutableStateFlow(0)
    val currentTime: StateFlow<Int> = _currentTime.asStateFlow()
    
    private val _timerProgress = MutableStateFlow(0)
    val timerProgress: StateFlow<Int> = _timerProgress.asStateFlow()
    
    private val _isCountingMode = MutableStateFlow(true)
    val isCountingMode: StateFlow<Boolean> = _isCountingMode.asStateFlow()
    
    private val _isSetCompleted = MutableStateFlow(false)
    val isSetCompleted: StateFlow<Boolean> = _isSetCompleted.asStateFlow()
    
    private var timerJob: Job? = null
    
    fun initializeExercise(exercise: ExerciseTemplate, currentSet: Int) {
        _exercise.value = exercise
        _currentSet.value = currentSet
        
        // 判断是计数还是计时模式
        if (exercise.reps > 0) {
            // 计数模式
            _isCountingMode.value = true
            _targetCount.value = exercise.reps
            _currentCount.value = 0
        } else if (exercise.durationSec > 0) {
            // 计时模式
            _isCountingMode.value = false
            _currentTime.value = 0
            startTimer(exercise.durationSec)
        }
    }
    
    fun incrementCount() {
        if (_isCountingMode.value) {
            val current = _currentCount.value
            val target = _targetCount.value
            
            if (current < target) {
                _currentCount.value = current + 1
                
                // 检查是否完成目标次数
                if (_currentCount.value >= target) {
                    completeCurrentSet()
                }
            }
        }
    }
    
    fun decrementCount() {
        if (_isCountingMode.value && _currentCount.value > 0) {
            _currentCount.value = _currentCount.value - 1
        }
    }
    
    private fun startTimer(durationSec: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in 0..durationSec) {
                _currentTime.value = i
                _timerProgress.value = (i * 100) / durationSec
                
                if (i == durationSec) {
                    // 计时完成
                    completeCurrentSet()
                    break
                }
                delay(1000)
            }
        }
    }
    
    fun completeCurrentSet() {
        val exercise = _exercise.value ?: return
        val currentSetNum = _currentSet.value

        // 创建组记录
        val setRecord = SetRecord(
            setNumber = currentSetNum,
            actualReps = if (_isCountingMode.value) _currentCount.value else 0,
            actualDurationSec = if (!_isCountingMode.value) _currentTime.value else 0,
            completed = true,
            timestamp = System.currentTimeMillis()
        )

        // 保存到数据库
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                repository.saveTrainingRecord(today, exercise.name, setRecord)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        _isSetCompleted.value = true
    }
    
    fun skipCurrentSet() {
        timerJob?.cancel()
        completeCurrentSet()
    }
    
    fun startRest(restSeconds: Int) {
        // 开始休息计时
        viewModelScope.launch {
            delay(restSeconds * 1000L)
            startNextSet()
        }
    }
    
    fun startNextSet() {
        val exercise = _exercise.value ?: return
        _currentSet.value = _currentSet.value + 1
        _isSetCompleted.value = false
        
        if (_isCountingMode.value) {
            _currentCount.value = 0
        } else {
            _currentTime.value = 0
            _timerProgress.value = 0
            startTimer(exercise.durationSec)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
