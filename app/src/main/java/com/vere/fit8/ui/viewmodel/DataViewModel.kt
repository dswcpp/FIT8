package com.vere.fit8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vere.fit8.data.repository.Fit8Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * 数据统计ViewModel
 * 管理图表数据和统计信息
 */
@HiltViewModel
class DataViewModel @Inject constructor(
    private val repository: Fit8Repository
) : ViewModel() {
    
    private val _weightData = MutableStateFlow<List<ChartDataPoint>>(emptyList())
    val weightData: StateFlow<List<ChartDataPoint>> = _weightData.asStateFlow()
    
    private val _bodyFatData = MutableStateFlow<List<ChartDataPoint>>(emptyList())
    val bodyFatData: StateFlow<List<ChartDataPoint>> = _bodyFatData.asStateFlow()
    
    private val _trainingStats = MutableStateFlow<List<TrainingStatItem>>(emptyList())
    val trainingStats: StateFlow<List<TrainingStatItem>> = _trainingStats.asStateFlow()
    
    private val _summaryStats = MutableStateFlow(SummaryStats())
    val summaryStats: StateFlow<SummaryStats> = _summaryStats.asStateFlow()
    
    private val _currentTimeRange = MutableStateFlow(TimeRange.MONTH)
    val currentTimeRange: StateFlow<TimeRange> = _currentTimeRange.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val timeRange = _currentTimeRange.value
                val (startDate, endDate) = getDateRange(timeRange)
                
                // 加载体重数据
                loadWeightData(startDate, endDate)
                
                // 加载体脂数据
                loadBodyFatData(startDate, endDate)
                
                // 加载训练统计
                loadTrainingStats(startDate, endDate)
                
                // 加载汇总统计
                loadSummaryStats()
                
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun setTimeRange(timeRange: TimeRange) {
        _currentTimeRange.value = timeRange
        loadData()
    }
    
    fun exportData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // 获取所有数据
                val allRecords = repository.getAllDailyRecordsForExport()
                val userStats = repository.getUserStats()

                // 生成CSV内容
                val csvContent = generateCSVContent(allRecords, userStats)

                // 触发导出事件
                _exportEvent.value = ExportEvent.Success(csvContent)

            } catch (e: Exception) {
                e.printStackTrace()
                _exportEvent.value = ExportEvent.Error("导出失败：${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun exportDataAsImage() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // 获取汇总数据
                val summaryStats = _summaryStats.value

                // 获取最近的记录
                val recentRecords = repository.getAllDailyRecordsForExport().take(10)

                // 触发图片导出事件
                _exportEvent.value = ExportEvent.ImageSuccess(summaryStats, recentRecords)

            } catch (e: Exception) {
                e.printStackTrace()
                _exportEvent.value = ExportEvent.Error("图片导出失败：${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _exportEvent = MutableStateFlow<ExportEvent?>(null)
    val exportEvent: StateFlow<ExportEvent?> = _exportEvent.asStateFlow()

    fun clearExportEvent() {
        _exportEvent.value = null
    }

    private fun generateCSVContent(records: List<com.vere.fit8.data.model.DailyRecord>, userStats: com.vere.fit8.data.model.UserStats?): String {
        val csv = StringBuilder()

        // CSV头部
        csv.appendLine("日期,体重(斤),体脂(%),训练时长(分钟),训练卡路里,饮水量(ml),睡眠时长(小时),备注")

        // 数据行
        records.forEach { record ->
            csv.appendLine("${record.date},${record.weight ?: ""},${record.bodyFat ?: ""},${record.trainingDurationMin},${record.trainingCalories},${record.waterMl},${record.sleepHours ?: ""},\"${record.notes}\""
            )
        }

        // 添加汇总统计
        csv.appendLine()
        csv.appendLine("=== 汇总统计 ===")
        userStats?.let { stats ->
            csv.appendLine("总训练次数,${stats.totalWorkouts}")
            csv.appendLine("总训练天数,${stats.totalDays}")
            csv.appendLine("连续训练天数,${stats.consecutiveDays}")
            csv.appendLine("总消耗卡路里,${stats.totalCaloriesBurned}")
        }

        return csv.toString()
    }
    
    private suspend fun loadWeightData(startDate: LocalDate, endDate: LocalDate) {
        val records = repository.getDailyRecords(startDate, endDate)
        val timeRange = _currentTimeRange.value

        // 生成完整的日期范围数据点
        val allDates = generateDateRange(startDate, endDate)
        val weightData = allDates.map { date ->
            val record = records.find { it.date == date }
            ChartDataPoint(
                value = record?.weight ?: 0f,
                label = formatDateLabel(date, timeRange),
                date = date,
                hasData = record?.weight != null
            )
        }

        _weightData.value = weightData
    }
    
    private suspend fun loadBodyFatData(startDate: LocalDate, endDate: LocalDate) {
        val records = repository.getDailyRecords(startDate, endDate)
        val timeRange = _currentTimeRange.value

        // 生成完整的日期范围数据点
        val allDates = generateDateRange(startDate, endDate)
        val bodyFatData = allDates.map { date ->
            val record = records.find { it.date == date }
            ChartDataPoint(
                value = record?.bodyFat ?: 0f,
                label = formatDateLabel(date, timeRange),
                date = date,
                hasData = record?.bodyFat != null
            )
        }

        _bodyFatData.value = bodyFatData
    }
    
    private suspend fun loadTrainingStats(startDate: LocalDate, endDate: LocalDate) {
        val records = repository.getDailyRecords(startDate, endDate)
        val timeRange = _currentTimeRange.value

        // 生成完整的日期范围数据点
        val allDates = generateDateRange(startDate, endDate)
        val trainingStats = allDates.map { date ->
            val record = records.find { it.date == date }
            val trainingMinutes = record?.trainingDurationMin ?: 0

            TrainingStatItem(
                label = formatDateLabel(date, timeRange),
                completionRate = if (trainingMinutes > 0) 100f else 0f,
                totalDays = 1,
                trainingDays = if (trainingMinutes > 0) 1 else 0,
                trainingMinutes = trainingMinutes,
                date = date
            )
        }

        _trainingStats.value = trainingStats
    }
    
    private suspend fun loadSummaryStats() {
        val userStats = repository.getUserStats()
        val today = LocalDate.now()

        // 获取当前（最新）体重和体脂
        val currentWeight = repository.getLatestWeight() ?: 0f
        val currentBodyFat = repository.getLatestBodyFat() ?: 0f

        // 获取当日饮水量
        val todayWaterIntake = repository.getTodayWaterIntake() ?: 0

        _summaryStats.value = SummaryStats(
            totalWorkouts = userStats?.totalWorkouts ?: 0,
            totalDays = userStats?.totalDays ?: 0,
            consecutiveDays = userStats?.consecutiveDays ?: 0,
            totalCalories = userStats?.totalCaloriesBurned ?: 0,
            currentWeight = currentWeight,
            currentBodyFat = currentBodyFat,
            todayWaterIntake = todayWaterIntake
        )
    }
    
    private fun getDateRange(timeRange: TimeRange): Pair<LocalDate, LocalDate> {
        val today = LocalDate.now()
        return when (timeRange) {
            TimeRange.WEEK -> {
                // 显示最近7天
                today.minusDays(6) to today
            }
            TimeRange.MONTH -> {
                // 显示最近30天
                today.minusDays(29) to today
            }
            TimeRange.THREE_MONTHS -> {
                // 显示最近90天
                today.minusDays(89) to today
            }
            TimeRange.ALL -> {
                // 显示最近365天
                today.minusDays(364) to today
            }
        }
    }

    /**
     * 生成日期范围内的所有日期
     */
    private fun generateDateRange(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var currentDate = startDate

        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }

        return dates
    }

    /**
     * 根据时间范围格式化日期标签
     */
    private fun formatDateLabel(date: LocalDate, timeRange: TimeRange): String {
        return when (timeRange) {
            TimeRange.WEEK -> {
                // 一周内显示星期几
                when (date.dayOfWeek.value) {
                    1 -> "周一"
                    2 -> "周二"
                    3 -> "周三"
                    4 -> "周四"
                    5 -> "周五"
                    6 -> "周六"
                    7 -> "周日"
                    else -> date.format(DateTimeFormatter.ofPattern("MM/dd"))
                }
            }
            TimeRange.MONTH -> {
                // 一个月内显示日期
                date.format(DateTimeFormatter.ofPattern("MM/dd"))
            }
            TimeRange.THREE_MONTHS -> {
                // 三个月内每隔几天显示一次日期
                if (date.dayOfMonth % 3 == 1) {
                    date.format(DateTimeFormatter.ofPattern("MM/dd"))
                } else {
                    ""
                }
            }
            TimeRange.ALL -> {
                // 全年显示月份
                if (date.dayOfMonth == 1) {
                    date.format(DateTimeFormatter.ofPattern("MM月"))
                } else {
                    ""
                }
            }
        }
    }
    
    private fun getWeekOfYear(date: LocalDate): Int {
        return date.dayOfYear / 7 + 1
    }
    
    enum class TimeRange {
        WEEK, MONTH, THREE_MONTHS, ALL
    }
}

/**
 * 图表数据点
 */
data class ChartDataPoint(
    val value: Float,
    val label: String,
    val date: LocalDate,
    val hasData: Boolean = true
)

/**
 * 训练统计项目
 */
data class TrainingStatItem(
    val label: String,
    val completionRate: Float,
    val totalDays: Int,
    val trainingDays: Int,
    val trainingMinutes: Int = 0,
    val date: LocalDate? = null
)

/**
 * 汇总统计数据
 */
data class SummaryStats(
    val totalWorkouts: Int = 0,
    val totalDays: Int = 0,
    val consecutiveDays: Int = 0,
    val totalCalories: Int = 0,
    val currentWeight: Float = 0f,
    val currentBodyFat: Float = 0f,
    val todayWaterIntake: Int = 0
)

/**
 * 导出事件
 */
sealed class ExportEvent {
    data class Success(val csvContent: String) : ExportEvent()
    data class ImageSuccess(val summaryStats: SummaryStats, val recentRecords: List<com.vere.fit8.data.model.DailyRecord>) : ExportEvent()
    data class Error(val message: String) : ExportEvent()
}
