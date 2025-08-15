package com.vere.fit8.utils

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import com.vere.fit8.R
import com.vere.fit8.data.model.DailyRecord
import kotlin.math.max
import kotlin.math.min

/**
 * 图表图片生成器
 * 用于生成趋势图表和数据可视化
 */
class ChartImageGenerator(private val context: Context) {
    
    private val primaryColor = ContextCompat.getColor(context, R.color.primary)
    private val successColor = ContextCompat.getColor(context, R.color.success)
    private val warningColor = ContextCompat.getColor(context, R.color.warning)
    private val surfaceColor = ContextCompat.getColor(context, R.color.surface)
    private val onSurfaceColor = ContextCompat.getColor(context, R.color.on_surface)
    private val onSurfaceVariantColor = ContextCompat.getColor(context, R.color.on_surface_variant)
    
    /**
     * 绘制体重趋势图
     */
    fun drawWeightTrendChart(
        canvas: Canvas,
        rect: RectF,
        records: List<DailyRecord>,
        title: String = "体重趋势"
    ) {
        val weightRecords = records.filter { it.weight != null && it.weight!! > 0 }
            .takeLast(30) // 最近30天
        
        if (weightRecords.isEmpty()) {
            drawEmptyChart(canvas, rect, title)
            return
        }
        
        drawChartBackground(canvas, rect, title)
        
        val chartArea = RectF(
            rect.left + 80,
            rect.top + 80,
            rect.right - 40,
            rect.bottom - 60
        )
        
        drawWeightLine(canvas, chartArea, weightRecords)
        drawWeightAxis(canvas, chartArea, weightRecords)
    }
    
    /**
     * 绘制训练强度图表
     */
    fun drawTrainingIntensityChart(
        canvas: Canvas,
        rect: RectF,
        records: List<DailyRecord>,
        title: String = "训练强度"
    ) {
        val trainingRecords = records.filter { it.trainingDurationMin > 0 }
            .takeLast(14) // 最近14天
        
        if (trainingRecords.isEmpty()) {
            drawEmptyChart(canvas, rect, title)
            return
        }
        
        drawChartBackground(canvas, rect, title)
        
        val chartArea = RectF(
            rect.left + 80,
            rect.top + 80,
            rect.right - 40,
            rect.bottom - 60
        )
        
        drawTrainingBars(canvas, chartArea, trainingRecords)
        drawTrainingAxis(canvas, chartArea, trainingRecords)
    }
    
    /**
     * 绘制综合健康评分雷达图
     */
    fun drawHealthRadarChart(
        canvas: Canvas,
        rect: RectF,
        records: List<DailyRecord>,
        title: String = "健康评分"
    ) {
        drawChartBackground(canvas, rect, title)
        
        val center = PointF(rect.centerX(), rect.centerY() + 20)
        val radius = min(rect.width(), rect.height()) * 0.3f
        
        // 计算各项评分
        val scores = calculateHealthScores(records)
        
        drawRadarBackground(canvas, center, radius)
        drawRadarData(canvas, center, radius, scores)
        drawRadarLabels(canvas, center, radius, scores)
    }
    
    private fun drawChartBackground(canvas: Canvas, rect: RectF, title: String) {
        // 绘制背景
        val backgroundPaint = Paint().apply {
            color = surfaceColor
            isAntiAlias = true
        }
        canvas.drawRoundRect(rect, 20f, 20f, backgroundPaint)
        
        // 绘制标题
        val titlePaint = Paint().apply {
            color = onSurfaceColor
            textSize = 42f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }
        
        val titleBounds = Rect()
        titlePaint.getTextBounds(title, 0, title.length, titleBounds)
        val titleX = rect.left + 40
        val titleY = rect.top + 50
        
        canvas.drawText(title, titleX, titleY, titlePaint)
    }
    
    private fun drawEmptyChart(canvas: Canvas, rect: RectF, title: String) {
        drawChartBackground(canvas, rect, title)
        
        val emptyPaint = Paint().apply {
            color = onSurfaceVariantColor
            textSize = 32f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }
        
        val emptyText = "暂无数据"
        val textBounds = Rect()
        emptyPaint.getTextBounds(emptyText, 0, emptyText.length, textBounds)
        
        val x = rect.centerX() - textBounds.width() / 2
        val y = rect.centerY()
        
        canvas.drawText(emptyText, x, y, emptyPaint)
    }
    
    private fun drawWeightLine(canvas: Canvas, chartArea: RectF, records: List<DailyRecord>) {
        if (records.size < 2) return
        
        val weights = records.mapNotNull { it.weight }
        val minWeight = weights.minOrNull() ?: return
        val maxWeight = weights.maxOrNull() ?: return
        val weightRange = maxWeight - minWeight
        
        if (weightRange == 0f) return
        
        val linePaint = Paint().apply {
            color = primaryColor
            strokeWidth = 6f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        
        val pointPaint = Paint().apply {
            color = primaryColor
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        
        val path = Path()
        var isFirst = true
        
        records.forEachIndexed { index, record ->
            record.weight?.let { weight ->
                val x = chartArea.left + (index.toFloat() / (records.size - 1)) * chartArea.width()
                val y = chartArea.bottom - ((weight - minWeight) / weightRange) * chartArea.height()
                
                if (isFirst) {
                    path.moveTo(x, y)
                    isFirst = false
                } else {
                    path.lineTo(x, y)
                }
                
                // 绘制数据点
                canvas.drawCircle(x, y, 8f, pointPaint)
            }
        }
        
        canvas.drawPath(path, linePaint)
    }
    
    private fun drawWeightAxis(canvas: Canvas, chartArea: RectF, records: List<DailyRecord>) {
        val axisPaint = Paint().apply {
            color = onSurfaceVariantColor
            textSize = 24f
            isAntiAlias = true
        }
        
        val weights = records.mapNotNull { it.weight }
        val minWeight = weights.minOrNull() ?: return
        val maxWeight = weights.maxOrNull() ?: return
        
        // Y轴标签
        canvas.drawText("${minWeight.toInt()}斤", chartArea.left - 70, chartArea.bottom, axisPaint)
        canvas.drawText("${maxWeight.toInt()}斤", chartArea.left - 70, chartArea.top + 20, axisPaint)
        
        // X轴标签（显示日期）
        if (records.isNotEmpty()) {
            val firstDate = records.first().date.dayOfMonth.toString()
            val lastDate = records.last().date.dayOfMonth.toString()
            
            canvas.drawText(firstDate, chartArea.left, chartArea.bottom + 40, axisPaint)
            canvas.drawText(lastDate, chartArea.right - 30, chartArea.bottom + 40, axisPaint)
        }
    }
    
    private fun drawTrainingBars(canvas: Canvas, chartArea: RectF, records: List<DailyRecord>) {
        val maxDuration = records.maxOfOrNull { it.trainingDurationMin } ?: return
        if (maxDuration == 0) return
        
        val barWidth = chartArea.width() / records.size * 0.6f
        val spacing = chartArea.width() / records.size * 0.4f
        
        val barPaint = Paint().apply {
            color = successColor
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        
        records.forEachIndexed { index, record ->
            val barHeight = (record.trainingDurationMin.toFloat() / maxDuration) * chartArea.height()
            val x = chartArea.left + index * (barWidth + spacing) + spacing / 2
            val y = chartArea.bottom - barHeight
            
            val barRect = RectF(x, y, x + barWidth, chartArea.bottom)
            canvas.drawRoundRect(barRect, 8f, 8f, barPaint)
        }
    }
    
    private fun drawTrainingAxis(canvas: Canvas, chartArea: RectF, records: List<DailyRecord>) {
        val axisPaint = Paint().apply {
            color = onSurfaceVariantColor
            textSize = 24f
            isAntiAlias = true
        }
        
        val maxDuration = records.maxOfOrNull { it.trainingDurationMin } ?: return
        
        // Y轴标签
        canvas.drawText("0分钟", chartArea.left - 70, chartArea.bottom, axisPaint)
        canvas.drawText("${maxDuration}分钟", chartArea.left - 70, chartArea.top + 20, axisPaint)
    }
    
    private fun calculateHealthScores(records: List<DailyRecord>): Map<String, Float> {
        val recentRecords = records.takeLast(30)
        
        // 训练频率评分 (0-100)
        val trainingDays = recentRecords.count { it.trainingDurationMin > 0 }
        val trainingScore = min(100f, (trainingDays / 30f) * 100f)
        
        // 体重稳定性评分
        val weights = recentRecords.mapNotNull { it.weight }.takeLast(10)
        val weightScore = if (weights.size >= 3) {
            val variance = weights.map { it - weights.average() }.map { it * it }.average()
            max(0f, 100f - variance.toFloat() * 10)
        } else 50f
        
        // 饮水规律评分
        val waterDays = recentRecords.count { it.waterMl >= 1500 }
        val waterScore = min(100f, (waterDays / 30f) * 100f)
        
        // 睡眠质量评分
        val sleepRecords = recentRecords.mapNotNull { it.sleepHours }.filter { it > 0 }
        val sleepScore = if (sleepRecords.isNotEmpty()) {
            val avgSleep = sleepRecords.average()
            when {
                avgSleep >= 7 && avgSleep <= 9 -> 100f
                avgSleep >= 6 && avgSleep <= 10 -> 80f
                else -> 50f
            }
        } else 50f
        
        // 整体坚持度评分
        val consistencyScore = min(100f, (recentRecords.size / 30f) * 100f)
        
        return mapOf(
            "训练" to trainingScore,
            "体重" to weightScore,
            "饮水" to waterScore,
            "睡眠" to sleepScore,
            "坚持" to consistencyScore
        )
    }
    
    private fun drawRadarBackground(canvas: Canvas, center: PointF, radius: Float) {
        val gridPaint = Paint().apply {
            color = onSurfaceVariantColor
            strokeWidth = 2f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        
        // 绘制同心圆
        for (i in 1..5) {
            val r = radius * i / 5
            canvas.drawCircle(center.x, center.y, r, gridPaint)
        }
        
        // 绘制五条轴线
        val angleStep = 360f / 5
        for (i in 0 until 5) {
            val angle = Math.toRadians((i * angleStep - 90).toDouble())
            val endX = center.x + radius * Math.cos(angle).toFloat()
            val endY = center.y + radius * Math.sin(angle).toFloat()
            canvas.drawLine(center.x, center.y, endX, endY, gridPaint)
        }
    }
    
    private fun drawRadarData(canvas: Canvas, center: PointF, radius: Float, scores: Map<String, Float>) {
        val dataPaint = Paint().apply {
            color = Color.argb(100, Color.red(primaryColor), Color.green(primaryColor), Color.blue(primaryColor))
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        
        val linePaint = Paint().apply {
            color = primaryColor
            strokeWidth = 4f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        
        val path = Path()
        val scoreList = scores.values.toList()
        
        scoreList.forEachIndexed { index, score ->
            val angle = Math.toRadians((index * 72 - 90).toDouble()) // 360/5 = 72度
            val distance = radius * (score / 100f)
            val x = center.x + distance * Math.cos(angle).toFloat()
            val y = center.y + distance * Math.sin(angle).toFloat()
            
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()
        
        canvas.drawPath(path, dataPaint)
        canvas.drawPath(path, linePaint)
    }
    
    private fun drawRadarLabels(canvas: Canvas, center: PointF, radius: Float, scores: Map<String, Float>) {
        val labelPaint = Paint().apply {
            color = onSurfaceColor
            textSize = 28f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        
        scores.keys.forEachIndexed { index, label ->
            val angle = Math.toRadians((index * 72 - 90).toDouble())
            val labelRadius = radius + 50
            val x = center.x + labelRadius * Math.cos(angle).toFloat()
            val y = center.y + labelRadius * Math.sin(angle).toFloat()
            
            canvas.drawText(label, x, y, labelPaint)
            
            // 绘制分数
            val score = scores[label]?.toInt() ?: 0
            val scorePaint = Paint().apply {
                color = onSurfaceVariantColor
                textSize = 24f
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("$score", x, y + 30, scorePaint)
        }
    }
}
