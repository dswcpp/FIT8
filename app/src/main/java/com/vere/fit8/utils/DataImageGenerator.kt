package com.vere.fit8.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.vere.fit8.R
import com.vere.fit8.data.model.DailyRecord
import com.vere.fit8.ui.viewmodel.SummaryStats
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 数据图片生成器
 * 用于生成包含健身数据的分享图片
 */
class DataImageGenerator(private val context: Context) {
    
    companion object {
        private const val IMAGE_WIDTH = 1080
        private const val IMAGE_HEIGHT = 2400 // 增加高度以容纳更多内容
        private const val PADDING = 60
        private const val CARD_RADIUS = 30f
        private const val TITLE_SIZE = 56f
        private const val SUBTITLE_SIZE = 38f
        private const val BODY_SIZE = 32f
        private const val LARGE_NUMBER_SIZE = 64f
        private const val CHART_HEIGHT = 400f
    }
    
    private val primaryColor = ContextCompat.getColor(context, R.color.primary)
    private val surfaceColor = ContextCompat.getColor(context, R.color.surface)
    private val onSurfaceColor = ContextCompat.getColor(context, R.color.on_surface)
    private val onSurfaceVariantColor = ContextCompat.getColor(context, R.color.on_surface_variant)
    private val backgroundColor = Color.WHITE
    
    /**
     * 生成专业数据统计图片
     */
    fun generateDataSummaryImage(
        summaryStats: SummaryStats,
        recentRecords: List<DailyRecord>
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 绘制背景
        canvas.drawColor(backgroundColor)

        var currentY = PADDING.toFloat()

        // 绘制标题
        currentY = drawTitle(canvas, currentY)

        // 绘制日期和统计周期
        currentY = drawDateAndPeriod(canvas, currentY, recentRecords)

        // 绘制核心数据概览
        currentY = drawCompactSummary(canvas, currentY, summaryStats)

        // 绘制趋势图表区域
        currentY = drawChartsSection(canvas, currentY, recentRecords)

        // 绘制智能建议
        currentY = drawAdviceSection(canvas, currentY, recentRecords)

        // 绘制底部品牌信息
        drawFooter(canvas)

        return bitmap
    }
    
    private fun drawTitle(canvas: Canvas, startY: Float): Float {
        val paint = Paint().apply {
            color = primaryColor
            textSize = TITLE_SIZE
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }
        
        val title = "Fit8 健身数据报告"
        val textBounds = Rect()
        paint.getTextBounds(title, 0, title.length, textBounds)
        
        val x = (IMAGE_WIDTH - textBounds.width()) / 2f
        val y = startY + textBounds.height()
        
        canvas.drawText(title, x, y, paint)
        
        return y + 60
    }
    
    private fun drawDateAndPeriod(canvas: Canvas, startY: Float, records: List<DailyRecord>): Float {
        val datePaint = Paint().apply {
            color = onSurfaceVariantColor
            textSize = SUBTITLE_SIZE
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }

        val dateText = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
        val textBounds = Rect()
        datePaint.getTextBounds(dateText, 0, dateText.length, textBounds)

        val x = (IMAGE_WIDTH - textBounds.width()) / 2f
        val y = startY + textBounds.height()

        canvas.drawText(dateText, x, y, datePaint)

        // 添加数据周期信息
        val periodText = "基于最近${records.size}天数据分析"
        val periodBounds = Rect()
        datePaint.textSize = BODY_SIZE
        datePaint.getTextBounds(periodText, 0, periodText.length, periodBounds)

        val periodX = (IMAGE_WIDTH - periodBounds.width()) / 2f
        val periodY = y + 40

        canvas.drawText(periodText, periodX, periodY, datePaint)

        return periodY + 60
    }
    
    private fun drawSummaryCard(canvas: Canvas, startY: Float, stats: SummaryStats): Float {
        val cardHeight = 600f
        val cardRect = RectF(
            PADDING.toFloat(),
            startY,
            (IMAGE_WIDTH - PADDING).toFloat(),
            startY + cardHeight
        )
        
        // 绘制卡片背景
        val cardPaint = Paint().apply {
            color = surfaceColor
            isAntiAlias = true
        }
        canvas.drawRoundRect(cardRect, CARD_RADIUS, CARD_RADIUS, cardPaint)
        
        // 绘制卡片内容
        var contentY = startY + 80
        
        // 标题
        val titlePaint = Paint().apply {
            color = onSurfaceColor
            textSize = SUBTITLE_SIZE
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }
        canvas.drawText("数据概览", PADDING + 60f, contentY, titlePaint)
        contentY += 100
        
        // 数据项
        val dataPaint = Paint().apply {
            color = primaryColor
            textSize = LARGE_NUMBER_SIZE
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }
        
        val labelPaint = Paint().apply {
            color = onSurfaceVariantColor
            textSize = BODY_SIZE
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }
        
        // 第一行：训练次数和连续天数
        val col1X = PADDING + 120f
        val col2X = IMAGE_WIDTH / 2f + 60f
        
        canvas.drawText("${stats.totalWorkouts}", col1X, contentY, dataPaint)
        canvas.drawText("次", col1X + 120, contentY, labelPaint)
        canvas.drawText("总训练", col1X, contentY + 50, labelPaint)
        
        canvas.drawText("${stats.consecutiveDays}", col2X, contentY, dataPaint)
        canvas.drawText("天", col2X + 120, contentY, labelPaint)
        canvas.drawText("连续训练", col2X, contentY + 50, labelPaint)
        
        contentY += 150
        
        // 第二行：体重和体脂
        val weightText = if (stats.currentWeight > 0) "${stats.currentWeight}斤" else "暂无"
        val bodyFatText = if (stats.currentBodyFat > 0) "${stats.currentBodyFat}%" else "暂无"
        
        canvas.drawText(weightText, col1X, contentY, dataPaint)
        canvas.drawText("当前体重", col1X, contentY + 50, labelPaint)
        
        canvas.drawText(bodyFatText, col2X, contentY, dataPaint)
        canvas.drawText("当前体脂", col2X, contentY + 50, labelPaint)
        
        contentY += 150
        
        // 第三行：饮水量和卡路里
        canvas.drawText("${stats.todayWaterIntake}ml", col1X, contentY, dataPaint)
        canvas.drawText("今日饮水", col1X, contentY + 50, labelPaint)
        
        canvas.drawText("${stats.totalCalories}", col2X, contentY, dataPaint)
        canvas.drawText("总卡路里", col2X, contentY + 50, labelPaint)
        
        return startY + cardHeight + 80
    }
    
    private fun drawRecentRecords(canvas: Canvas, startY: Float, records: List<DailyRecord>): Float {
        if (records.isEmpty()) return startY
        
        val titlePaint = Paint().apply {
            color = onSurfaceColor
            textSize = SUBTITLE_SIZE
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }
        
        canvas.drawText("最近训练记录", PADDING + 60f, startY + 40, titlePaint)
        
        var currentY = startY + 120
        val recordPaint = Paint().apply {
            color = onSurfaceColor
            textSize = BODY_SIZE
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }
        
        val datePaint = Paint().apply {
            color = onSurfaceVariantColor
            textSize = BODY_SIZE
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }
        
        records.take(5).forEach { record ->
            val dateStr = record.date.format(DateTimeFormatter.ofPattern("MM/dd"))
            val trainingInfo = "${record.trainingDurationMin}分钟 · ${record.trainingCalories}卡路里"
            
            canvas.drawText(dateStr, PADDING + 60f, currentY, datePaint)
            canvas.drawText(trainingInfo, PADDING + 200f, currentY, recordPaint)
            
            currentY += 60
        }
        
        return currentY + 40
    }
    
    private fun drawFooter(canvas: Canvas) {
        val footerPaint = Paint().apply {
            color = onSurfaceVariantColor
            textSize = BODY_SIZE
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }
        
        val footerText = "由 Fit8 健身助手生成"
        val textBounds = Rect()
        footerPaint.getTextBounds(footerText, 0, footerText.length, textBounds)
        
        val x = (IMAGE_WIDTH - textBounds.width()) / 2f
        val y = IMAGE_HEIGHT - PADDING - 40f
        
        canvas.drawText(footerText, x, y, footerPaint)
    }

    private fun drawCompactSummary(canvas: Canvas, startY: Float, stats: SummaryStats): Float {
        val cardHeight = 200f
        val cardRect = RectF(
            PADDING.toFloat(),
            startY,
            (IMAGE_WIDTH - PADDING).toFloat(),
            startY + cardHeight
        )

        // 绘制卡片背景
        val cardPaint = Paint().apply {
            color = surfaceColor
            isAntiAlias = true
        }
        canvas.drawRoundRect(cardRect, CARD_RADIUS, CARD_RADIUS, cardPaint)

        // 绘制核心数据（横向排列）
        val dataPaint = Paint().apply {
            color = primaryColor
            textSize = LARGE_NUMBER_SIZE
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val labelPaint = Paint().apply {
            color = onSurfaceVariantColor
            textSize = BODY_SIZE
            typeface = Typeface.DEFAULT
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val itemWidth = (IMAGE_WIDTH - 2 * PADDING) / 4f
        val centerY = startY + cardHeight / 2

        // 训练次数
        val x1 = PADDING + itemWidth * 0.5f
        canvas.drawText("${stats.totalWorkouts}", x1, centerY - 20, dataPaint)
        canvas.drawText("训练次数", x1, centerY + 30, labelPaint)

        // 连续天数
        val x2 = PADDING + itemWidth * 1.5f
        canvas.drawText("${stats.consecutiveDays}", x2, centerY - 20, dataPaint)
        canvas.drawText("连续天数", x2, centerY + 30, labelPaint)

        // 当前体重
        val x3 = PADDING + itemWidth * 2.5f
        val weightText = if (stats.currentWeight > 0) "${stats.currentWeight.toInt()}" else "-"
        canvas.drawText(weightText, x3, centerY - 20, dataPaint)
        canvas.drawText("体重(斤)", x3, centerY + 30, labelPaint)

        // 今日饮水
        val x4 = PADDING + itemWidth * 3.5f
        canvas.drawText("${stats.todayWaterIntake}", x4, centerY - 20, dataPaint)
        canvas.drawText("饮水(ml)", x4, centerY + 30, labelPaint)

        return startY + cardHeight + 40
    }

    private fun drawChartsSection(canvas: Canvas, startY: Float, records: List<DailyRecord>): Float {
        val chartGenerator = ChartImageGenerator(context)
        var currentY = startY

        // 体重趋势图
        val weightChartRect = RectF(
            PADDING.toFloat(),
            currentY,
            (IMAGE_WIDTH - PADDING).toFloat(),
            currentY + CHART_HEIGHT
        )
        chartGenerator.drawWeightTrendChart(canvas, weightChartRect, records)
        currentY += CHART_HEIGHT + 30

        // 训练强度图和健康雷达图并排
        val halfWidth = (IMAGE_WIDTH - 3 * PADDING) / 2f

        val trainingChartRect = RectF(
            PADDING.toFloat(),
            currentY,
            PADDING + halfWidth,
            currentY + CHART_HEIGHT
        )
        chartGenerator.drawTrainingIntensityChart(canvas, trainingChartRect, records)

        val radarChartRect = RectF(
            PADDING * 2 + halfWidth,
            currentY,
            (IMAGE_WIDTH - PADDING).toFloat(),
            currentY + CHART_HEIGHT
        )
        chartGenerator.drawHealthRadarChart(canvas, radarChartRect, records)

        return currentY + CHART_HEIGHT + 40
    }

    private fun drawAdviceSection(canvas: Canvas, startY: Float, records: List<DailyRecord>): Float {
        val adviceGenerator = FitnessAdviceGenerator()
        val adviceList = adviceGenerator.generateAdvice(records)

        if (adviceList.isEmpty()) return startY

        // 标题
        val titlePaint = Paint().apply {
            color = onSurfaceColor
            textSize = SUBTITLE_SIZE
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        canvas.drawText("💡 智能建议", PADDING + 40f, startY + 40, titlePaint)
        var currentY = startY + 100

        // 绘制建议卡片
        adviceList.take(3).forEach { advice ->
            currentY = drawAdviceCard(canvas, currentY, advice)
        }

        return currentY + 40
    }

    private fun drawAdviceCard(canvas: Canvas, startY: Float, advice: FitnessAdviceGenerator.FitnessAdvice): Float {
        val cardHeight = 120f
        val cardRect = RectF(
            PADDING.toFloat(),
            startY,
            (IMAGE_WIDTH - PADDING).toFloat(),
            startY + cardHeight
        )

        // 绘制卡片背景
        val cardPaint = Paint().apply {
            color = surfaceColor
            isAntiAlias = true
        }
        canvas.drawRoundRect(cardRect, CARD_RADIUS, CARD_RADIUS, cardPaint)

        // 绘制优先级指示器
        val priorityColor = when (advice.priority) {
            5 -> Color.parseColor("#FF5722") // 红色 - 紧急
            4 -> Color.parseColor("#FF9800") // 橙色 - 重要
            3 -> Color.parseColor("#FFC107") // 黄色 - 中等
            else -> Color.parseColor("#4CAF50") // 绿色 - 一般
        }

        val indicatorPaint = Paint().apply {
            color = priorityColor
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val indicatorRect = RectF(
            PADDING.toFloat(),
            startY,
            PADDING + 20f,
            startY + cardHeight
        )
        canvas.drawRoundRect(indicatorRect, CARD_RADIUS, CARD_RADIUS, indicatorPaint)

        // 绘制图标和标题
        val titlePaint = Paint().apply {
            color = onSurfaceColor
            textSize = BODY_SIZE + 4
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        val iconAndTitle = "${advice.icon} ${advice.title}"
        canvas.drawText(iconAndTitle, PADDING + 50f, startY + 40, titlePaint)

        // 绘制内容（多行文本）
        val contentPaint = Paint().apply {
            color = onSurfaceVariantColor
            textSize = BODY_SIZE - 2
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }

        val maxWidth = IMAGE_WIDTH - 2 * PADDING - 60
        val lines = wrapText(advice.content, contentPaint, maxWidth.toFloat())

        lines.take(2).forEachIndexed { index, line ->
            canvas.drawText(line, PADDING + 50f, startY + 70 + index * 30, contentPaint)
        }

        return startY + cardHeight + 20
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = ""

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val textWidth = paint.measureText(testLine)

            if (textWidth <= maxWidth) {
                currentLine = testLine
            } else {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine)
                    currentLine = word
                } else {
                    lines.add(word)
                }
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines
    }
}
