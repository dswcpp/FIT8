package com.vere.fit8.utils

import com.vere.fit8.data.model.DailyRecord
import java.time.LocalDate
import kotlin.math.abs

/**
 * 健身建议生成器
 * 基于历史数据分析生成个性化建议
 */
class FitnessAdviceGenerator {
    
    data class FitnessAdvice(
        val category: String,
        val title: String,
        val content: String,
        val priority: Int, // 1-5, 5最重要
        val icon: String
    )
    
    /**
     * 生成个性化健身建议
     */
    fun generateAdvice(records: List<DailyRecord>): List<FitnessAdvice> {
        val advice = mutableListOf<FitnessAdvice>()
        val recentRecords = records.takeLast(30) // 最近30天
        
        // 训练频率分析
        advice.addAll(analyzeTrainingFrequency(recentRecords))
        
        // 体重趋势分析
        advice.addAll(analyzeWeightTrend(recentRecords))
        
        // 饮水习惯分析
        advice.addAll(analyzeWaterIntake(recentRecords))
        
        // 睡眠质量分析
        advice.addAll(analyzeSleepQuality(recentRecords))
        
        // 训练强度分析
        advice.addAll(analyzeTrainingIntensity(recentRecords))
        
        // 坚持度分析
        advice.addAll(analyzeConsistency(recentRecords))
        
        // 按优先级排序，返回前5个最重要的建议
        return advice.sortedByDescending { it.priority }.take(5)
    }
    
    private fun analyzeTrainingFrequency(records: List<DailyRecord>): List<FitnessAdvice> {
        val advice = mutableListOf<FitnessAdvice>()
        val trainingDays = records.count { it.trainingDurationMin > 0 }
        val totalDays = records.size
        val frequency = if (totalDays > 0) trainingDays.toFloat() / totalDays else 0f
        
        when {
            frequency < 0.3 -> {
                advice.add(FitnessAdvice(
                    category = "训练频率",
                    title = "增加训练频率",
                    content = "您最近${totalDays}天只训练了${trainingDays}天。建议每周至少训练3-4次，保持规律的运动习惯。",
                    priority = 5,
                    icon = "🏃"
                ))
            }
            frequency > 0.8 -> {
                advice.add(FitnessAdvice(
                    category = "训练频率",
                    title = "注意休息恢复",
                    content = "您的训练频率很高！记得安排适当的休息日，让肌肉充分恢复，避免过度训练。",
                    priority = 4,
                    icon = "😴"
                ))
            }
            else -> {
                advice.add(FitnessAdvice(
                    category = "训练频率",
                    title = "保持良好节奏",
                    content = "您的训练频率很棒！继续保持这个节奏，稳步提升健身效果。",
                    priority = 2,
                    icon = "👍"
                ))
            }
        }
        
        return advice
    }
    
    private fun analyzeWeightTrend(records: List<DailyRecord>): List<FitnessAdvice> {
        val advice = mutableListOf<FitnessAdvice>()
        val weightRecords = records.mapNotNull { it.weight }.takeLast(14)
        
        if (weightRecords.size < 5) {
            advice.add(FitnessAdvice(
                category = "体重管理",
                title = "记录体重数据",
                content = "建议每天记录体重，这样可以更好地跟踪身体变化趋势。",
                priority = 3,
                icon = "⚖️"
            ))
            return advice
        }
        
        val firstWeight = weightRecords.take(5).average()
        val lastWeight = weightRecords.takeLast(5).average()
        val weightChange = lastWeight - firstWeight
        
        when {
            weightChange > 2 -> {
                advice.add(FitnessAdvice(
                    category = "体重管理",
                    title = "控制体重增长",
                    content = "最近体重上升了${String.format("%.1f", weightChange)}斤。建议调整饮食结构，增加有氧运动。",
                    priority = 4,
                    icon = "📈"
                ))
            }
            weightChange < -2 -> {
                advice.add(FitnessAdvice(
                    category = "体重管理",
                    title = "注意营养补充",
                    content = "最近体重下降了${String.format("%.1f", abs(weightChange))}斤。确保营养充足，避免过度减重。",
                    priority = 4,
                    icon = "📉"
                ))
            }
            else -> {
                advice.add(FitnessAdvice(
                    category = "体重管理",
                    title = "体重控制良好",
                    content = "您的体重保持稳定，继续保持健康的生活方式！",
                    priority = 1,
                    icon = "✅"
                ))
            }
        }
        
        return advice
    }
    
    private fun analyzeWaterIntake(records: List<DailyRecord>): List<FitnessAdvice> {
        val advice = mutableListOf<FitnessAdvice>()
        val waterRecords = records.filter { it.waterMl > 0 }
        val avgWater = if (waterRecords.isNotEmpty()) waterRecords.map { it.waterMl }.average() else 0.0
        
        when {
            avgWater < 1200 -> {
                advice.add(FitnessAdvice(
                    category = "饮水习惯",
                    title = "增加饮水量",
                    content = "您的日均饮水量约${avgWater.toInt()}ml，建议每天至少饮水1500ml，促进新陈代谢。",
                    priority = 4,
                    icon = "💧"
                ))
            }
            avgWater > 3000 -> {
                advice.add(FitnessAdvice(
                    category = "饮水习惯",
                    title = "适量饮水",
                    content = "您的饮水量很充足，注意不要过量饮水，保持适度即可。",
                    priority = 2,
                    icon = "💧"
                ))
            }
            else -> {
                advice.add(FitnessAdvice(
                    category = "饮水习惯",
                    title = "饮水习惯良好",
                    content = "您的饮水量很合适，继续保持这个好习惯！",
                    priority = 1,
                    icon = "💧"
                ))
            }
        }
        
        return advice
    }
    
    private fun analyzeSleepQuality(records: List<DailyRecord>): List<FitnessAdvice> {
        val advice = mutableListOf<FitnessAdvice>()
        val sleepRecords = records.mapNotNull { it.sleepHours }.filter { it > 0 }
        
        if (sleepRecords.isEmpty()) {
            advice.add(FitnessAdvice(
                category = "睡眠质量",
                title = "记录睡眠时间",
                content = "建议记录每日睡眠时间，充足的睡眠对健身效果很重要。",
                priority = 3,
                icon = "😴"
            ))
            return advice
        }
        
        val avgSleep = sleepRecords.average()
        
        when {
            avgSleep < 6 -> {
                advice.add(FitnessAdvice(
                    category = "睡眠质量",
                    title = "增加睡眠时间",
                    content = "您的平均睡眠时间约${String.format("%.1f", avgSleep)}小时，建议每天睡眠7-9小时。",
                    priority = 5,
                    icon = "😴"
                ))
            }
            avgSleep > 10 -> {
                advice.add(FitnessAdvice(
                    category = "睡眠质量",
                    title = "调整睡眠时间",
                    content = "您的睡眠时间较长，可以适当调整作息，保持7-9小时的优质睡眠。",
                    priority = 3,
                    icon = "😴"
                ))
            }
            else -> {
                advice.add(FitnessAdvice(
                    category = "睡眠质量",
                    title = "睡眠质量良好",
                    content = "您的睡眠时间很合适，继续保持规律的作息！",
                    priority = 1,
                    icon = "😴"
                ))
            }
        }
        
        return advice
    }
    
    private fun analyzeTrainingIntensity(records: List<DailyRecord>): List<FitnessAdvice> {
        val advice = mutableListOf<FitnessAdvice>()
        val trainingRecords = records.filter { it.trainingDurationMin > 0 }
        
        if (trainingRecords.isEmpty()) return advice
        
        val avgDuration = trainingRecords.map { it.trainingDurationMin }.average()
        val avgCalories = trainingRecords.map { it.trainingCalories }.average()
        
        when {
            avgDuration < 20 -> {
                advice.add(FitnessAdvice(
                    category = "训练强度",
                    title = "延长训练时间",
                    content = "您的平均训练时间约${avgDuration.toInt()}分钟，建议每次训练30-60分钟效果更佳。",
                    priority = 4,
                    icon = "⏱️"
                ))
            }
            avgDuration > 90 -> {
                advice.add(FitnessAdvice(
                    category = "训练强度",
                    title = "优化训练效率",
                    content = "您的训练时间较长，可以提高训练强度，缩短训练时间，提升效率。",
                    priority = 3,
                    icon = "⏱️"
                ))
            }
            else -> {
                advice.add(FitnessAdvice(
                    category = "训练强度",
                    title = "训练强度合适",
                    content = "您的训练时长很合适，继续保持这个强度！",
                    priority = 1,
                    icon = "⏱️"
                ))
            }
        }
        
        return advice
    }
    
    private fun analyzeConsistency(records: List<DailyRecord>): List<FitnessAdvice> {
        val advice = mutableListOf<FitnessAdvice>()
        
        // 分析最近的连续性
        var currentStreak = 0
        var maxStreak = 0
        var tempStreak = 0
        
        records.reversed().forEach { record ->
            if (record.trainingDurationMin > 0 || record.waterMl > 0) {
                if (currentStreak == 0) currentStreak = tempStreak + 1
                tempStreak++
                maxStreak = maxOf(maxStreak, tempStreak)
            } else {
                tempStreak = 0
            }
        }
        
        when {
            currentStreak < 3 -> {
                advice.add(FitnessAdvice(
                    category = "坚持度",
                    title = "建立运动习惯",
                    content = "建议制定简单的运动计划，从每天10分钟开始，逐步建立运动习惯。",
                    priority = 5,
                    icon = "🎯"
                ))
            }
            currentStreak >= 7 -> {
                advice.add(FitnessAdvice(
                    category = "坚持度",
                    title = "坚持得很棒",
                    content = "您已经连续坚持${currentStreak}天了！继续保持，您正在养成很好的健康习惯。",
                    priority = 1,
                    icon = "🏆"
                ))
            }
            else -> {
                advice.add(FitnessAdvice(
                    category = "坚持度",
                    title = "继续保持",
                    content = "您已经坚持${currentStreak}天了，再坚持几天就能形成稳定的习惯！",
                    priority = 2,
                    icon = "💪"
                ))
            }
        }
        
        return advice
    }
}
