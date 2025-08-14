package com.vere.fit8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.model.ExerciseTemplate
import com.vere.fit8.data.model.WeeklyPlan
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
 * 训练ViewModel
 * 管理训练计划、训练状态和计时器
 */
@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val repository: Fit8Repository
) : ViewModel() {
    
    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> = _currentDate.asStateFlow()
    
    private val _todayPlan = MutableStateFlow<WeeklyPlan?>(null)
    val todayPlan: StateFlow<WeeklyPlan?> = _todayPlan.asStateFlow()
    
    private val _exercises = MutableStateFlow<List<ExerciseTemplate>>(emptyList())
    val exercises: StateFlow<List<ExerciseTemplate>> = _exercises.asStateFlow()
    
    private val _trainingState = MutableStateFlow<TrainingState>(TrainingState.NotStarted)
    val trainingState: StateFlow<TrainingState> = _trainingState.asStateFlow()
    
    private val _currentExercise = MutableStateFlow<ExerciseTemplate?>(null)
    val currentExercise: StateFlow<ExerciseTemplate?> = _currentExercise.asStateFlow()
    
    private val _timer = MutableStateFlow("00:00")
    val timer: StateFlow<String> = _timer.asStateFlow()
    
    private val _restTimer = MutableStateFlow("00:00")
    val restTimer: StateFlow<String> = _restTimer.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // 训练状态变量
    private var currentExerciseIndex = 0
    private var currentSet = 1
    private var trainingStartTime = 0L
    private var totalTrainingTime = 0L
    private var timerJob: Job? = null
    private var restTimerJob: Job? = null
    
    fun loadTodayTraining() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val date = _currentDate.value
                val currentWeek = getCurrentWeek()
                val dayOfWeek = date.dayOfWeek.value
                
                // 加载今日训练计划
                val plan = repository.getDailyPlan(currentWeek, dayOfWeek)
                _todayPlan.value = plan
                
                // 设置训练动作列表
                plan?.let {
                    _exercises.value = it.exercises
                    if (it.exercises.isNotEmpty()) {
                        _currentExercise.value = it.exercises[0]
                    }
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun previousDay() {
        _currentDate.value = _currentDate.value.minusDays(1)
        resetTraining()
        loadTodayTraining()
    }
    
    fun nextDay() {
        val tomorrow = LocalDate.now().plusDays(1)
        if (_currentDate.value.isBefore(tomorrow)) {
            _currentDate.value = _currentDate.value.plusDays(1)
            resetTraining()
            loadTodayTraining()
        }
    }
    
    fun startTraining() {
        if (_exercises.value.isEmpty()) return
        
        trainingStartTime = System.currentTimeMillis()
        currentExerciseIndex = 0
        currentSet = 1
        _currentExercise.value = _exercises.value[0]
        _trainingState.value = TrainingState.InProgress
        
        startTimer()
    }
    
    fun pauseTraining() {
        _trainingState.value = TrainingState.Paused
        stopTimer()
    }
    
    fun resumeTraining() {
        _trainingState.value = TrainingState.InProgress
        startTimer()
    }
    
    fun completeExercise(exercise: ExerciseTemplate) {
        val currentExercise = _currentExercise.value ?: return
        
        if (currentSet < currentExercise.sets) {
            // 还有组数未完成，开始休息
            currentSet++
            startRest(currentExercise.restSec)
        } else {
            // 当前动作完成，进入下一个动作
            nextExercise()
        }
    }
    
    fun nextExercise() {
        currentExerciseIndex++
        currentSet = 1
        
        if (currentExerciseIndex >= _exercises.value.size) {
            // 所有动作完成
            completeTraining()
        } else {
            // 进入下一个动作
            _currentExercise.value = _exercises.value[currentExerciseIndex]
        }
    }
    
    fun skipCurrentExercise() {
        nextExercise()
    }
    
    fun completeTraining() {
        stopTimer()
        stopRestTimer()
        
        val totalDuration = ((System.currentTimeMillis() - trainingStartTime) / 1000 / 60).toInt()
        val estimatedCalories = _todayPlan.value?.estimatedCalories ?: 0
        
        _trainingState.value = TrainingState.Completed(totalDuration, estimatedCalories)
        
        // 保存训练记录
        saveTrainingRecord(totalDuration, estimatedCalories)
    }
    
    private fun startRest(restSeconds: Int) {
        _trainingState.value = TrainingState.Resting
        
        restTimerJob?.cancel()
        restTimerJob = viewModelScope.launch {
            var remainingTime = restSeconds
            
            while (remainingTime > 0) {
                val minutes = remainingTime / 60
                val seconds = remainingTime % 60
                _restTimer.value = String.format("%02d:%02d", minutes, seconds)
                
                delay(1000)
                remainingTime--
            }
            
            // 休息结束，继续训练
            _trainingState.value = TrainingState.InProgress
            _restTimer.value = "00:00"
        }
    }
    
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_trainingState.value is TrainingState.InProgress) {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = (currentTime - trainingStartTime) / 1000
                val minutes = elapsedTime / 60
                val seconds = elapsedTime % 60
                
                _timer.value = String.format("%02d:%02d", minutes, seconds)
                
                delay(1000)
            }
        }
    }
    
    private fun stopTimer() {
        timerJob?.cancel()
    }
    
    private fun stopRestTimer() {
        restTimerJob?.cancel()
    }
    
    private fun resetTraining() {
        stopTimer()
        stopRestTimer()
        _trainingState.value = TrainingState.NotStarted
        _timer.value = "00:00"
        _restTimer.value = "00:00"
        currentExerciseIndex = 0
        currentSet = 1
    }
    
    private fun saveTrainingRecord(duration: Int, calories: Int) {
        viewModelScope.launch {
            // 这里可以保存训练记录到数据库
            // 更新用户统计数据
            repository.incrementTotalWorkouts()
            repository.addCaloriesBurned(calories)
        }
    }
    
    fun getCurrentWeek(): Int {
        // 计算当前是第几周
        val startDate = LocalDate.of(2025, 8, 1) // 假设8月1日开始
        val currentDate = _currentDate.value
        val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, currentDate)
        return ((daysBetween / 7) % 8 + 1).toInt().coerceIn(1, 8)
    }
    
    fun getCurrentSet(): Int = currentSet
    
    override fun onCleared() {
        super.onCleared()
        stopTimer()
        stopRestTimer()
    }
}

/**
 * 训练状态密封类
 */
sealed class TrainingState {
    object NotStarted : TrainingState()
    object InProgress : TrainingState()
    object Paused : TrainingState()
    object Resting : TrainingState()
    data class Completed(val totalDuration: Int, val totalCalories: Int) : TrainingState()
}
